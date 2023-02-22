import autoweka.*;
import autoweka.smac.SMACExperimentConstructor;
import autoweka.tools.GetBestFromTrajectoryGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class AutowekaRunner {

    public static void main(String[] args) {

        List<String> datasets = Arrays.asList("car", "yeast", "krvskp", "gcredits", "semeion", "abalon", "wine", "dexter", "waveform",
                "shuttle", "madelon", "secom", "amazon", "dorothea", "convex", "cup", "guisette", "mnist", "cifar10small", "mnistrotation", "cifar10");

        for (String expName : datasets) {
            try {
                long begTime = System.currentTimeMillis();
                ExperimentBatch.ExperimentComponent expComp = new ExperimentBatch.ExperimentComponent();
                expComp.name = "Autoweka";
                expComp.constructor = "autoweka.smac.SMACExperimentConstructor";
                expComp.instanceGenerator = "autoweka.instancegenerators.CrossValidation";
                expComp.instanceGeneratorArgs = "seed=0:numFolds=10";
                expComp.tunerTimeout = 390.0F;
                expComp.trainTimeout = 390.0F;
                expComp.memory = "4000m";
                expComp.extraProps = "executionMode=SMAC:initialIncumbent=RANDOM:initialN=1";
                expComp.constructorArgs = new ArrayList<String>();
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

                //invoking the autoweka.TrajectoryParser class, with the arguments of the experiment folder and the seed you want to parse.

                //Run the main method of autoweka.TrajectoryMerger, with a single argument of the experiment's directory.
                //This produces a single file <ExperimentName>.trajectories inside the experiment's folder.

                // Finally, to get the best hyper-parameters and method that Auto-WEKA has found on the dataset,
                //run the main method of autoweka.tools.GetBestFromTrajectoryGroup, with the single command line argument pointing at the .trajectories file

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

                try (PrintWriter writer = new PrintWriter("out" + File.separator + "result" + File.separator + expName + "_autoweka" + (begTime % 100000000) + ".txt", "UTF-8")) {
                    writer.println("Algorithms:");
                    for (String algo : allowedClassifiers) {
                        writer.print(algo + "\n");
                    }
                    writer.print("\n");
                    writer.println("All time (seconds): " + (endTime - begTime) / 1000 + "\n");
                    writer.println("Autoweka result:");
                    writer.println(result);
                    writer.close();
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
}
