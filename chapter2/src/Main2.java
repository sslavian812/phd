import autoweka.*;
import autoweka.smac.SMACExperimentConstructor;
import autoweka.smac.SMACTrajectoryParser;
import autoweka.tools.GetBestFromTrajectoryGroup;
import ca.ubc.cs.beta.smac.executors.SMACExecutor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main2 {

    private static Pattern mTrajPattern = Pattern.compile("([\\-\\.\\d]+), ([\\-\\.\\dEef]+), [\\-\\.\\d]+, [\\-\\.\\d]+, [\\-\\.\\d]+, (.*)");
    private static Pattern mRunsAndResultFileNamePattern = Pattern.compile("runs_and_results-it(\\d+).csv");

    public static void main(String[] args) {
        ExperimentBatch.ExperimentComponent expComp = new ExperimentBatch.ExperimentComponent();
        expComp.name = "AW_r1";
        expComp.constructor = "autoweka.smac.SMACExperimentConstructor";
        expComp.instanceGenerator = "autoweka.instancegenerators.CrossValidation";
        expComp.instanceGeneratorArgs = "seed=0:numFolds=10";
        expComp.tunerTimeout = 300.0F;
        expComp.trainTimeout = 300.0F;
        expComp.memory = "2000m";
        expComp.extraProps = "executionMode=SMAC:initialIncumbent=RANDOM:initialN=1";
        expComp.constructorArgs = new ArrayList<String>();
        expComp.constructorArgs.add("-nometa");
        expComp.constructorArgs.add("-noensemble");

        expComp.resultMetric = "errorRate";

        List<String> allowedClassifiers = new ArrayList();
        allowedClassifiers.add("weka.classifiers.functions.SMO");
        //allowedClassifiers.add("weka.classifiers.bayes.NaiveBayes");
        //allowedClassifiers.add("weka.classifiers.functions.Logistic");

        expComp.allowedClassifiers = allowedClassifiers;

        ExperimentBatch.DatasetComponent datasetComp = new ExperimentBatch.DatasetComponent();
        datasetComp.name = "diabetes";
        //datasetComp.name = "mushrooms";
        datasetComp.setTrainTestArff("dataset_37_diabetes.arff", "dataset_37_diabetes.arff");
        //datasetComp.setTrainTestArff("dataset_24_mushroom.arff", "dataset_24_mushroom.arff");

        Experiment exp = ExperimentBatch.createExperiment(expComp, datasetComp);
        SMACExperimentConstructor.buildSingle(expComp.constructor, exp, new LinkedList(expComp.constructorArgs));

        //exp.callString.add("--restore-scenario");
        //exp.callString.add("D:\\Data\\Work\\ITMO\\ml\\diplom\\ASCAH\\experiments\\AW_r1-diabetes\\out\\autoweka\\state-run3");
        //exp.callString.add("--restore-iteration");
        //exp.callString.add("AUTO");

        String seed = "3";
        String expName = expComp.name + "-" + datasetComp.name;
        String expFolder = "experiments" + File.separator + expName;

        for(int executable = 0; executable < exp.callString.size(); ++executable) {
            exp.callString.set(executable, (exp.callString.get(executable)).replace("{SEED}", seed));
        }

        exp.callString.remove(0);
        exp.callString.set(3, expFolder + "\\" + exp.callString.get(3));
        String[] smacargs = new String [exp.callString.size() - 1];

        //SMACExecutor.oldMain(exp.callString.toArray(smacargs));

//        final SMACRunner runner = new SMACRunner(exp, smacargs);
//        runner.start();
//
//        long timeout = 7000;

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                System.out.println("\nTimer!!!\n");
//                runner.suspend();
//            }
//        }, timeout);

//        try {
//            Thread.sleep(timeout + 1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //System.out.println("Resume thread\n");
        //runner.resume();

        String outFolder = "out\\autoweka";

        // copy autoweka.params to outFolder
        try {
            FileUtils.copyFile(new File("experiments" + File.separator + exp.name + File.separator + "autoweka.params"), new File(outFolder + File.separator + "autoweka" + seed + ".params"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        parseTrajectory(expName, seed, outFolder, exp);

        System.out.println("=========================\nResult:\n");
        GetBestFromTrajectoryGroup gbfg = new GetBestFromTrajectoryGroup(outFolder + File.separator + expName + ".trajectories");
        System.out.println("Raw args = " + gbfg.rawArgs);
        System.out.println("Args = " + gbfg.classifierArgs);
        System.out.println("Error estimate = " + gbfg.errorEstimate);
    }

    private static void parseTrajectory(String expName, String targetSeed, String expFolder, Experiment exp) {
        File outDir = new File(expFolder);
        TrajectoryGroup group = new TrajectoryGroup(exp);
        group.addTrajectory(getTrajectory(exp, outDir, targetSeed));
        group.toXML(expFolder + File.separator + expName + ".trajectories");
    }

    public static Trajectory getTrajectory(Experiment experiment, File folder, String seed) {
        ClassParams params = new ClassParams(URLDecoder.decode(folder.getAbsolutePath()) + File.separator + "autoweka" + seed + ".params"); // needs autoweka.params
        Trajectory traj = new Trajectory(seed);

        try {
            String e = "";
            File[] files = (new File(URLDecoder.decode(folder.getAbsolutePath()))).listFiles();
            File[] scanner = files;
            int line = files.length;

            for(int matcher = 0; matcher < line; ++matcher) {
                File currentBest = scanner[matcher];
                String s = currentBest.getName();
                if(s.startsWith("traj") && s.endsWith("-" + seed + ".txt")) {
                    e = URLDecoder.decode(currentBest.getAbsolutePath());
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
            files = (new File("out\\autoweka\\state-run" + seed + File.separator)).listFiles();
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
