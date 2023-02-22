import autoweka.*;
import autoweka.smac.SMACExperimentConstructor;
import org.apache.commons.io.FileUtils;
import ru.ifmo.ml.SMACRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AlgorithmCreater {

    private String dataset;
    private String testset;
    public String datasetName;
    int seedN;


    private static Pattern mTrajPattern = Pattern.compile("([\\-\\.\\d]+), ([\\-\\.\\dEef]+), [\\-\\.\\d]+, [\\-\\.\\d]+, [\\-\\.\\d]+, (.*)");
    private static Pattern mRunsAndResultFileNamePattern = Pattern.compile("runs_and_results-it(\\d+).csv");

    public AlgorithmCreater(int seed, String dataset, String testset, String datasetName) {
        seedN = seed;
        this.dataset = dataset;
        this.testset = testset;
        this.datasetName = datasetName;
        /*
        try {
            FileUtils.cleanDirectory(new File("out" + File.separator + "autoweka"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    public SMACRunner create(Algorithms algo, String name) {
        ExperimentBatch.ExperimentComponent expComp = new ExperimentBatch.ExperimentComponent();
        expComp.name = name;
        expComp.constructor = "autoweka.smac.SMACExperimentConstructor";
        expComp.instanceGenerator = "autoweka.instancegenerators.CrossValidation";
        expComp.instanceGeneratorArgs = "seed=0:numFolds=10";
        expComp.tunerTimeout = Constants.AUTOWEKA_TUNER_TIMEOUT;
        expComp.trainTimeout = Constants.AUTOWEKA_TRAIN_TIMEOUT;
        expComp.memory = "8000m";
        expComp.extraProps = "executionMode=SMAC:initialIncumbent=RANDOM:initialN=1";
        expComp.constructorArgs = new ArrayList<String>();
        expComp.constructorArgs.add("-nometa");
        expComp.constructorArgs.add("-noensemble");

        expComp.resultMetric = "errorRate";

        List<String> allowedClassifiers = new ArrayList();

        switch(algo) {
            case kNN: allowedClassifiers.add("weka.classifiers.lazy.IBk"); break;
            case RandomForest: allowedClassifiers.add("weka.classifiers.trees.RandomForest"); break;
            case LogisticRegression: allowedClassifiers.add("weka.classifiers.functions.Logistic"); break;
            case SVM: allowedClassifiers.add("weka.classifiers.functions.SMO"); break;
            case SingleLayerPerceptron: allowedClassifiers.add("weka.classifiers.functions.MultilayerPerceptron"); break;
            case C45DecTree: allowedClassifiers.add("weka.classifiers.trees.J48"); break;
        }
        expComp.allowedClassifiers = allowedClassifiers;

        ExperimentBatch.DatasetComponent datasetComp = new ExperimentBatch.DatasetComponent();
        datasetComp.name = datasetName;
        datasetComp.setTrainTestArff(dataset, testset);

        Experiment experiment = ExperimentBatch.createExperiment(expComp, datasetComp);
        SMACExperimentConstructor.buildSingle(expComp.constructor, experiment, new LinkedList(expComp.constructorArgs));

        String expFolder = "experiments" + File.separator + expComp.name + "-" + datasetComp.name;
        //String seed = String.valueOf((System.currentTimeMillis() / 100) % 10000);
        String seed = String.valueOf(seedN);

        String outFolder = "out" + File.separator + "autoweka";

        // copy autoweka.params to outFolder
        try {
            FileUtils.copyFile(new File("experiments" + File.separator + experiment.name + File.separator + "autoweka.params"),
                    new File(outFolder + File.separator + "autoweka" + seed + ".params"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int executable = 0; executable < experiment.callString.size(); ++executable) {
            experiment.callString.set(executable, (experiment.callString.get(executable)).replace("{SEED}", seed));
        }

        experiment.callString.remove(0);
        experiment.callString.set(3, expFolder + File.separator + experiment.callString.get(3));

        experiment.callString.remove(12);
        experiment.callString.remove(12);
        experiment.callString.remove(12);
        experiment.callString.remove(12);

        String[] smacargs = new String [experiment.callString.size() - 1];
        seedN++;
        return new SMACRunner(experiment, seed, smacargs);
    }

    public static void parseTrajectory(String expName, String targetSeed, String expFolder, Experiment exp) {
        File outDir = new File(expFolder);
        TrajectoryGroup group = new TrajectoryGroup(exp);
        group.addTrajectory(getTrajectory(exp, outDir, targetSeed));
        group.toXML(expFolder + File.separator + expName + ".trajectories");
    }

    public static Trajectory getTrajectory(Experiment experiment, File folder, String seed) {
        String outFolder = "out" + File.separator + "autoweka";
        ClassParams params = new ClassParams(outFolder + File.separator + "autoweka" + seed + ".params"); // needs autoweka.params.... ADD SEED TO BANDITS!!!!!!!
        Trajectory traj = new Trajectory(seed);

        try {
            String e = "not_ready.txt";
            File[] files = new File(outFolder).listFiles();
            File[] scanner = files;
            int line = files.length;

            for(int matcher = 0; matcher < line; ++matcher) {
                File currentBest = scanner[matcher];
                String s = currentBest.getName();
                if(s.startsWith("traj") && s.endsWith("-" + seed + ".txt")) {
                    e = outFolder + File.separator + s;
                    break;
                }
            }

            Scanner var28 = new Scanner(new FileInputStream(e));
            double var31 = 1.0E100D;
            String argString = null;
            double time = 0.0D;
            double score = 3.4028234663852886E38D;

            Matcher var30;
            while(var28.hasNextLine()) {
                String var29 = var28.nextLine();
                var30 = mTrajPattern.matcher(var29);
                if(var30.matches()) {
                    time = (double)Float.parseFloat(var30.group(1));
                    score = (double)Float.parseFloat(var30.group(2));
                    if(score < var31) {
                        var31 = score;
                        argString = filterArgString(params, var30.group(3));
                        traj.addPoint(new Trajectory.Point(time, score, argString));
                    }
                }
            }

            String runsAndResultsFileName = null;
            int runsAndResultsIteration = -1;
            files = (new File("out" + File.separator + "autoweka" + File.separator + "state-run" + seed + File.separator)).listFiles();
            File[] numEvals = files;
            int numMemOut = files.length;

            int numTimeOut;
            for(numTimeOut = 0; numTimeOut < numMemOut; ++numTimeOut) {
                File row = numEvals[numTimeOut];
                String e1 = row.getName();
                var30 = mRunsAndResultFileNamePattern.matcher(e1);
                if(var30.matches()) {
                    int itr = Integer.parseInt(var30.group(1));
                    if(itr > runsAndResultsIteration) {
                        runsAndResultsFileName = URLDecoder.decode(row.getAbsolutePath());
                        runsAndResultsIteration = itr;
                    }
                }
            }

            if(runsAndResultsFileName != null) {
                int var32 = 0;
                numMemOut = 0;
                numTimeOut = 0;
                var28 = new Scanner(new FileInputStream(runsAndResultsFileName));
                var28.nextLine();

                while(var28.hasNextLine()) {
                    String[] var33 = var28.nextLine().split(",");

                    try {
                        ++var32;
                        if((double)Float.parseFloat(var33[7]) >= 1.1D * (double)experiment.trainTimeout) {
                            ++numTimeOut;
                        }

                        if(var33[14].contains("MEMOUT")) {
                            ++numMemOut;
                        }
                    } catch (Exception var26) {

                    }
                }

                traj.setEvaluationCounts(var32, numMemOut, numTimeOut);
            }
            return traj;
        } catch (Exception var27) {
            throw new RuntimeException("Failed to parse trajectory", var27);
        }
    }

    private static String filterArgString(ClassParams params, String args) {
        HashMap paramMap = params.getParameterMap();
        HashMap argMap = new HashMap();
        String[] splitArgs = args.split(", ");
        String[] var6 = splitArgs;
        int var7 = splitArgs.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String argPair = var6[var8];
            String[] splitArg = argPair.split("=", 2);
            String arg = splitArg[0].trim();
            String value = splitArg[1].trim();
            if(paramMap.get(arg) == null) {
                throw new RuntimeException("Unknown argument found in trajectory \'" + arg + "\'");
            }

            if(value.startsWith("\'") && value.endsWith("\'")) {
                value = value.substring(1, value.length() - 1);
            }

            argMap.put(arg, value);
        }

        return Util.argMapToString(params.filterParams(argMap));
    }
}
