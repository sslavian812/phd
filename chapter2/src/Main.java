import autoweka.*;
import autoweka.smac.SMACExperimentConstructor;
import autoweka.tools.GetBestFromTrajectoryGroup;
import ca.ubc.cs.beta.smac.executors.SMACExecutor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        ExperimentBatch.ExperimentComponent expComp = new ExperimentBatch.ExperimentComponent();
        expComp.name = "Autoweka";
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
        allowedClassifiers.add("weka.classifiers.lazy.IBk");
        allowedClassifiers.add("weka.classifiers.trees.RandomForest");
        allowedClassifiers.add("weka.classifiers.rules.DecisionTable");
        allowedClassifiers.add("weka.classifiers.functions.Logistic");
        allowedClassifiers.add("weka.classifiers.functions.SMO");

        expComp.allowedClassifiers = allowedClassifiers;

        ExperimentBatch.DatasetComponent datasetComp = new ExperimentBatch.DatasetComponent();
        datasetComp.name = "german_credits";
        //datasetComp.name = "mushrooms";
        //datasetComp.setTrainTestArff("credit-g.arff", "credit-g_test.arff");
        //datasetComp.setTrainTestArff("dataset_24_mushroom.arff", "dataset_24_mushroom.arff");
        datasetComp.setTrainTestArff("dexter_train.arff", "dexter_test.arff");

        Experiment exp = ExperimentBatch.createExperiment(expComp, datasetComp);
        SMACExperimentConstructor.buildSingle(expComp.constructor, exp, new LinkedList(expComp.constructorArgs));

        //exp.callString.add("--restore-scenario");
        //exp.callString.add("D:\\Data\\Work\\ITMO\\ml\\diplom\\ASCAH\\experiments\\AW_r1-diabetes\\out\\autoweka\\state-run3");
        //exp.callString.add("--restore-iteration");
        //exp.callString.add("AUTO");

        String seed = "3";
        String expFolder = "experiments" + File.separator + exp.name;


        //exp.callString.add("--restore-scenario");
        //exp.callString.add("D:\\Data\\Work\\ITMO\\ml\\diplom\\ASCAH\\experiments\\AW_r1-diabetes");

        //String expFolder = "experiments\\AW_r1-mushrooms";
        Experiment.main(new String[] {"-noexit", expFolder, seed});

        //invoking the autoweka.TrajectoryParser class, with the arguments of the experiment folder and the seed you want to parse.

        //Run the main method of autoweka.TrajectoryMerger, with a single argument of the experiment's directory.
        //This produces a single file <ExperimentName>.trajectories inside the experiment's folder.

        // Finally, to get the best hyper-parameters and method that Auto-WEKA has found on the dataset,
        //run the main method of autoweka.tools.GetBestFromTrajectoryGroup, with the single command line argument pointing at the .trajectories file


        File fold = new File(expFolder);
        Trajectory tr = TrajectoryParser.getTrajectory(exp, fold, seed);

        /*
        int numEv = tr.getNumEvaluations();
        System.out.println("evaluations num = " + numEv);
        List<Trajectory.Point> points = tr.getPoints();
        for (Trajectory.Point p : points) {
            System.out.println("=================\n"
                    + "Time = " + p.getTime() + "\n"
                    + "Args = " + p.getArgs() + "\n"
                    + "Error estimate = " + p.getErrorEstimate());
        }
        System.out.println("=================\n");
 		*/

        TrajectoryParser.main(new String[]{"-single", expFolder, seed});
        //TrajectoryPointPredictionRunner.main(new String[]{expFolder + File.separator + expName + ".trajectories." + seed, "-savemodel"});

        try {
            TrajectoryMerger.main(new String[] {expFolder});
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("=========================\nResult:\n");
        GetBestFromTrajectoryGroup gbfg = new GetBestFromTrajectoryGroup("experiments\\AW_r1-diabetes\\AW_r1-diabetes.trajectories");
        //GetBestFromTrajectoryGroup gbfg = new GetBestFromTrajectoryGroup("experiments\\AW_r1-mushrooms\\AW_r1-mushrooms.trajectories");
        System.out.println("Raw args = " + gbfg.rawArgs);
        System.out.println("Args = " + gbfg.classifierArgs);
        System.out.println("Error estimate = " + gbfg.errorEstimate);

        /*
        Raw args = -_0__wekaclassifiersfunctionssmo_00_0_C 0.8282789985643634 -_0__wekaclassifiersfunctionssmo_01_1_N 1 -_0__wekaclassifiersfunctionssmo_02_2_M REMOVED -_0__wekaclassifiersfunctionssmo_03_3_REG_IGNORE_QUOTE_START_K weka.classifiers.functions.supportVector.Puk -_0__wekaclassifiersfunctionssmo_08_4_puk_S 0.541681608158524 -_0__wekaclassifiersfunctionssmo_09_4_puk_O 0.25202183052409793 -_0__wekaclassifiersfunctionssmo_11_5_QUOTE_END REMOVED -targetclass weka.classifiers.functions.SMO
        Args = -C 0.8282789985643634 -N 1 -M -K "weka.classifiers.functions.supportVector.Puk -S 0.541681608158524 -O 0.25202183052409793"
        Error estimate = 22.077923
         */
    }
}
