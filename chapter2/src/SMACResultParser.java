import autoweka.Experiment;
import autoweka.ExperimentBatch;
import autoweka.tools.GetBestFromTrajectoryGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SMACResultParser {

    public static void main(String[] args) {
        List<String> seeds = Arrays.asList("1", "11", "111");
        String expName = "yeast";
        ExperimentBatch.ExperimentComponent expComp = new ExperimentBatch.ExperimentComponent();
        expComp.name = "My";
        expComp.constructor = "autoweka.smac.SMACExperimentConstructor";
        expComp.instanceGenerator = "autoweka.instancegenerators.CrossValidation";
        expComp.instanceGeneratorArgs = "seed=0:numFolds=10";
        expComp.tunerTimeout = 400.0F;
        expComp.trainTimeout = 400.0F;
        expComp.memory = "2000m";
        expComp.extraProps = "executionMode=SMAC:initialIncumbent=RANDOM:initialN=1";
        expComp.constructorArgs = new ArrayList<String>();
        expComp.constructorArgs.add("-nometa");
        expComp.constructorArgs.add("-noensemble");

        expComp.resultMetric = "errorRate";

        List<String> allowedClassifiers = new ArrayList();
        allowedClassifiers.add("weka.classifiers.functions.SMO");

        expComp.allowedClassifiers = allowedClassifiers;

        ExperimentBatch.DatasetComponent datasetComp = new ExperimentBatch.DatasetComponent();
        datasetComp.name = expName;
        datasetComp.setTrainTestArff("yeast_train.arff", "yeast_test.arff");

        Experiment exp = ExperimentBatch.createExperiment(expComp, datasetComp);

        for (String seed : seeds) {

            String outFolder = "out" + File.separator + "new" + File.separator + expName;
            AlgorithmCreater.parseTrajectory(exp.name, seed, outFolder, exp);
            GetBestFromTrajectoryGroup gbfg = new GetBestFromTrajectoryGroup(outFolder + File.separator + exp.name + ".trajectories");

            System.out.println("\n=========================\nSeed = " + seed);
            System.out.println("Raw args = " + gbfg.rawArgs);
            System.out.println("Args = " + gbfg.classifierArgs);
            System.out.println("Error estimate = " + gbfg.errorEstimate + "\n=========================\n");
        }
    }
}
