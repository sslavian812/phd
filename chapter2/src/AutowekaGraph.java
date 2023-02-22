import autoweka.Experiment;
import autoweka.ExperimentBatch;
import autoweka.TrajectoryMerger;
import autoweka.TrajectoryParser;

import autoweka.smac.SMACExperimentConstructor;
import autoweka.tools.GetBestFromTrajectoryGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class AutowekaGraph {

    public static void main(String[] args) {
        // args[0]    -- algo: svm, rf, dt, per, knn, lr
        // args[1...] -- seeds

        List<String> seeds = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            seeds.add(args[i]);
        }

        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();
        List<String> datasets = new ArrayList<>();
        int counter = 0;
        for (File file : listOfFiles) {
            String name = file.toString();
            if (name.substring(name.length() - 5).equals(".arff") && name.contains("_test")) {
                if (counter == 0) {
                    name = name.substring(2, name.length() - 10);
                    datasets.add(name);
                    counter = 1;
                } else {
                    counter = 0;
                }
            }
        }

        for (int i = 0; i < datasets.size(); i++) {
            String expName = datasets.get(i);
            for (String seed : seeds) {
                try (PrintWriter writer = new PrintWriter("autoweka_result_" + expName + "_seed_" + seed + ".txt", "UTF-8")) {
                    //writer.println("ExpName, AlgoName, Timeout, ErrorEstimate, BestAlgo, BestParams");

                    long begTime = System.currentTimeMillis();
                    ExperimentBatch.ExperimentComponent expComp = new ExperimentBatch.ExperimentComponent();
                    expComp.name = "Autoweka";
                    expComp.constructor = "autoweka.smac.SMACExperimentConstructor";
                    expComp.instanceGenerator = "autoweka.instancegenerators.CrossValidation";
                    expComp.instanceGeneratorArgs = "seed=0:numFolds=10";
                    expComp.tunerTimeout = 1380F;
                    expComp.trainTimeout = 1380F;
                    expComp.memory = "4000m";
                    expComp.extraProps = "executionMode=SMAC:initialIncumbent=RANDOM:initialN=1";
                    expComp.constructorArgs = new ArrayList<>();
                    expComp.constructorArgs.add("-nometa");
                    expComp.constructorArgs.add("-noensemble");

                    expComp.resultMetric = "errorRate";

                    List<String> allowedClassifiers = new ArrayList();
                    switch (args[0]) {
                        case "knn": allowedClassifiers.add("weka.classifiers.lazy.IBk"); break;
                        case "rf" : allowedClassifiers.add("weka.classifiers.trees.RandomForest"); break;
                        case "lr" : allowedClassifiers.add("weka.classifiers.functions.Logistic"); break;
                        case "svm": allowedClassifiers.add("weka.classifiers.functions.SMO"); break;
                        case "per": allowedClassifiers.add("weka.classifiers.functions.MultilayerPerceptron"); break;
                        case "dt" : allowedClassifiers.add("weka.classifiers.trees.J48"); break;
                        default:    allowedClassifiers.add("weka.classifiers.functions.SMO"); break;
                    }
                    expComp.allowedClassifiers = allowedClassifiers;

                    ExperimentBatch.DatasetComponent datasetComp = new ExperimentBatch.DatasetComponent();
                    datasetComp.name = expName;
                    datasetComp.setTrainTestArff(expName + "_train.arff", expName + "_test.arff");

                    Experiment exp = ExperimentBatch.createExperiment(expComp, datasetComp);
                    SMACExperimentConstructor.buildSingle(expComp.constructor, exp, new LinkedList(expComp.constructorArgs));

                    String expFolder = "experiments" + File.separator + exp.name;

                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            TrajectoryParser.main(new String[]{"-single", expFolder, seed});

                            try {
                                TrajectoryMerger.main(new String[]{expFolder});
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            GetBestFromTrajectoryGroup gbfg = new GetBestFromTrajectoryGroup(expFolder + File.separator + exp.name + ".trajectories");

                            long endTime = System.currentTimeMillis();
                            writer.println(expName + ",autoweka," + (endTime - begTime) / 1000 + "," + gbfg.errorEstimate + ","
                                    + gbfg.classifierClass + "," + gbfg.classifierArgs);
                            writer.flush();
                        }
                    }, 180000, 30000);

                    Experiment.main(new String[]{"-noexit", expFolder, seed});

                    timer.cancel();

                    TrajectoryParser.main(new String[]{"-single", expFolder, seed});

                    try {
                        TrajectoryMerger.main(new String[]{expFolder});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("=========================\nResult:\n");
                    GetBestFromTrajectoryGroup gbfg = new GetBestFromTrajectoryGroup("experiments" + File.separator + exp.name + File.separator + exp.name + ".trajectories");

                    String result = "Raw args = " + gbfg.rawArgs + "\nArgs = " + gbfg.classifierArgs + "\nError estimate = " + gbfg.errorEstimate;
                    System.out.println(result);
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void rerun() {

        List<String> datasets = Arrays.asList("car", "gcredits", "wine", "yeast", "krvskp", "semeion", "abalon", "dexter", "waveform",
                "shuttle", "madelon", "secom", "amazon", "dorothea", "convex", "cup", "guisette", "mnist", "cifar10small", "mnistrotation", "cifar10");

        try (PrintWriter writer = new PrintWriter("autoweka_result1.txt", "UTF-8")) {
            writer.println("ExpName, AlgoName, AllTime, Timeout, ErrorEstimate, BestAlgo, BestParams");
            for (String expName : datasets) {
                for (float timeout = 100.0F; timeout <= 10000.0F; timeout += 100.0F) {
                    try {
                        long begTime = System.currentTimeMillis();
                        ExperimentBatch.ExperimentComponent expComp = new ExperimentBatch.ExperimentComponent();
                        expComp.name = "Autoweka";
                        expComp.constructor = "autoweka.smac.SMACExperimentConstructor";
                        expComp.instanceGenerator = "autoweka.instancegenerators.CrossValidation";
                        expComp.instanceGeneratorArgs = "seed=0:numFolds=10";
                        expComp.tunerTimeout = timeout;
                        expComp.trainTimeout = timeout;
                        expComp.memory = "4000m";
                        expComp.extraProps = "executionMode=SMAC:initialIncumbent=RANDOM:initialN=1";
                        expComp.constructorArgs = new ArrayList<>();
                        expComp.constructorArgs.add("-nometa");
                        expComp.constructorArgs.add("-noensemble");

                        expComp.resultMetric = "errorRate";

                        List<String> allowedClassifiers = new ArrayList();
                        allowedClassifiers.add("weka.classifiers.lazy.IBk");
                        allowedClassifiers.add("weka.classifiers.trees.RandomForest");
                        allowedClassifiers.add("weka.classifiers.functions.Logistic");
                        allowedClassifiers.add("weka.classifiers.functions.SMO");
                        allowedClassifiers.add("weka.classifiers.functions.MultilayerPerceptron");
                        allowedClassifiers.add("weka.classifiers.trees.J48");

                        expComp.allowedClassifiers = allowedClassifiers;

                        ExperimentBatch.DatasetComponent datasetComp = new ExperimentBatch.DatasetComponent();
                        datasetComp.name = expName;
                        datasetComp.setTrainTestArff(expName + "_train.arff", expName + "_test.arff");

                        Experiment exp = ExperimentBatch.createExperiment(expComp, datasetComp);
                        SMACExperimentConstructor.buildSingle(expComp.constructor, exp, new LinkedList(expComp.constructorArgs));

                        String seed = "3";
                        String expFolder = "experiments" + File.separator + exp.name;

                        Experiment.main(new String[]{"-noexit", expFolder, seed});

                        TrajectoryParser.main(new String[]{"-single", expFolder, seed});

                        try {
                            TrajectoryMerger.main(new String[]{expFolder});
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        System.out.println("=========================\nResult:\n");
                        GetBestFromTrajectoryGroup gbfg = new GetBestFromTrajectoryGroup("experiments" + File.separator + exp.name + File.separator + exp.name + ".trajectories");

                        long endTime = System.currentTimeMillis();

                        String result = "Raw args = " + gbfg.rawArgs + "\nArgs = " + gbfg.classifierArgs + "\nError estimate = " + gbfg.errorEstimate;
                        System.out.println(result);

                        writer.println(expName + ",autoweka," + (endTime - begTime) / 1000 + "," + (int)timeout + "," + gbfg.errorEstimate + ","
                                + gbfg.classifierClass + "," + gbfg.classifierArgs);
                        writer.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
