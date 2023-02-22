import autoweka.Experiment;
import autoweka.tools.GetBestFromTrajectoryGroup;
import ru.ifmo.ml.SMACRunner;

import java.io.File;;
import java.util.*;
import java.util.function.Function;

public class AlgorithmExecutor implements Function<Integer, Double> {

    public List<SMACRunner> arms;
    public List<Double> armError;
    public List<Double> armMuTheta;
    public List<Integer> runNum;
    public BanditTimer bandTimer;

    public double bestError = Double.MAX_VALUE;
    public double bestEI = Double.MAX_VALUE;
    public String bestParams;
    public String bestAlgo;

    public AlgorithmExecutor(List<SMACRunner> arms, BanditTimer timer) {
        this.arms = arms;
        this.runNum = new ArrayList<Integer>(Collections.nCopies(arms.size(), 0));
        this.armError = new ArrayList<Double>(Collections.nCopies(arms.size(), 100.0));
        this.armMuTheta = new ArrayList<Double>(Collections.nCopies(arms.size(), 100.0));
        bandTimer = timer;
    }

    @Override
    public Double apply(Integer arm) {
        final SMACRunner runner = arms.get(arm);

        // start or resume thread

        if (runNum.get(arm) == 0) {
            System.out.println("\nStart thread " + runner.exp.name + "\n");
            runner.start();
        } else {
            System.out.println("\nResume thread " + runner.exp.name + "\n");
            runner.resume();
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Thread " + runner.exp.name + " EI = " + runner.EI.get());
                System.out.println("\nSuspend thread " + runner.exp.name + "\n");
                runner.suspend();
            }
        }, bandTimer.getTimelimit());

        try {
            Thread.sleep(bandTimer.getTimelimit() + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        runNum.set(arm, runNum.get(arm) + 1);

        // reward: (best - cur) / 100.0
        //return getError(runner.exp, runner.seed, arm);

        return getExpRes(runner.exp, runner.seed, arm, runner.EI.get());
    }


    private Double getExpRes(Experiment experiment, String seed, Integer arm, double EI) {
        String outFolder = "out" + File.separator + "autoweka";

        AlgorithmCreater.parseTrajectory(experiment.name, seed, outFolder, experiment);

        System.out.println("=========================\n" + experiment.name + " result:\n");
        GetBestFromTrajectoryGroup gbfg = new GetBestFromTrajectoryGroup(outFolder + File.separator + experiment.name + ".trajectories");
        System.out.println("Raw args = " + gbfg.rawArgs);
        System.out.println("Args = " + gbfg.classifierArgs);
        System.out.println("Error estimate = " + gbfg.errorEstimate + "\n=========================\n");

        armMuTheta.set(arm, EI);
        if (gbfg.errorEstimate > 0) {
            armError.set(arm, Math.min(armError.get(arm), gbfg.errorEstimate));
        } else {
            armError.set(arm, 100.0);
        }
        if ((gbfg.errorEstimate < bestError) && (gbfg.errorEstimate > 0)) {
            bestError = gbfg.errorEstimate;
            bestAlgo = experiment.allowedClassifiers.get(0);
            bestParams = gbfg.classifierArgs;
        }

        System.out.println("Statistics:\nAlgo, Run num, Error, Mu_theta\n");
        for (int i = 0; i < arms.size(); i++) {
            System.out.println(arms.get(i).exp.allowedClassifiers.get(0) + ", " + runNum.get(i) + ", " + armError.get(i) + ", " + armMuTheta.get(i) + "\n");
        }

        return (100.0 - EI) / 100.0;
    }

    public double getError(Experiment experiment, String seed, Integer arm) {
        String outFolder = "out" + File.separator + "autoweka";

        AlgorithmCreater.parseTrajectory(experiment.name, seed, outFolder, experiment);

        System.out.println("=========================\n" + experiment.name + " result:\n");
        GetBestFromTrajectoryGroup gbfg = new GetBestFromTrajectoryGroup(outFolder + File.separator + experiment.name + ".trajectories");
        System.out.println("Raw args = " + gbfg.rawArgs);
        System.out.println("Args = " + gbfg.classifierArgs);
        System.out.println("Error estimate = " + gbfg.errorEstimate + "\n=========================\n");

        double reward = bestError;

        if (gbfg.errorEstimate > 0) {
            armError.set(arm, Math.min(armError.get(arm), gbfg.errorEstimate));
        }
        if ((gbfg.errorEstimate < bestError) && (gbfg.errorEstimate > 0)) {
            if (reward == Double.MAX_VALUE) {
                reward = gbfg.errorEstimate;
            }
            bestError = gbfg.errorEstimate;
            bestAlgo = experiment.allowedClassifiers.get(0);
            bestParams = gbfg.classifierArgs;
        } else if (gbfg.errorEstimate < 0) {
            reward = -2.0;
        }
        reward -= gbfg.errorEstimate;

        System.out.println("Statistics:\nAlgo, Run num, Error\n");
        for (int i = 0; i < arms.size(); i++) {
            System.out.println(arms.get(i).exp.allowedClassifiers.get(0) + ", " + runNum.get(i) + ", " + armError.get(i) + "\n");
        }

        return (reward / 100.0);    //best / reward
    }
}
