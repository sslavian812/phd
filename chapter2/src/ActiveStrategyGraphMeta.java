import ru.ifmo.ml.SMACRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActiveStrategyGraphMeta {

    public static void main(String[] args) {

        //args[0] -- algo: ucb1, soft5
        // args[1] -- iteration num, was 360
        // args[2...] -- seeds

        List<Integer> seeds = new ArrayList<>();
        for (int i = 2; i < args.length; i++) {
            seeds.add(Integer.parseInt(args[i]));
        }

        int iterAll = Integer.parseInt(args[1]);

        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();
        List<String> datasets = new ArrayList<>();
        int counter = 0;
        for (File file : listOfFiles) {
            String name = file.toString();
            if (name.substring(name.length() - 5).equals(".arff") && !name.contains(Constants.META_ARFF_FILE) && !name.contains(Constants.META_SELECTED_ARFF_FILE)) {
                if (counter == 0) {
                    int len = name.length() - 10;
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
            for (Integer seed : seeds) {
                try (PrintWriter writer = new PrintWriter("bandit_result_meta_" + args[0] + "_" + expName + "_seed_" + seed + ".txt", "UTF-8")) {
                    //writer.println("ExpName, AlgoName, AllTime, BandTime, IterTime, IterNum, ErrorEstimate, BestAlgo, BestParams");

                    AlgorithmCreater algCreater = new AlgorithmCreater(seed, expName + "_train.arff", expName + "_test.arff", expName);
                    double[] metaDistribution = MetaFeaturesProc.getDistribution(expName + "_train.arff");

                    //for (long timeBudget = 10000; timeBudget <= 60000; timeBudget += 3000) {
                    long timeBudget = 30000;

                    long begTime = System.currentTimeMillis();
                    List<SMACRunner> arms = new ArrayList<>();
                    List<Algorithms> algorithms = new ArrayList<>();
                    algorithms.add(Algorithms.LogisticRegression);
                    algorithms.add(Algorithms.RandomForest);
                    algorithms.add(Algorithms.kNN);
                    algorithms.add(Algorithms.SVM);
                    algorithms.add(Algorithms.SingleLayerPerceptron);
                    algorithms.add(Algorithms.C45DecTree);

                    for (Algorithms algo : algorithms) {
                        arms.add(algCreater.create(algo, algo.name()));
                    }

                    AlgorithmExecutor executor = new AlgorithmExecutor(arms, new BanditTimer(timeBudget, Constants.BANDIT_ALL_RUN_ITERATION));

                    MultiarmedBandit bandit;
                    switch (args[0]) {
                        case "ucb1":   bandit = new MultiarmedBanditUCB1EI(executor, arms.size()); break;
                        case "soft5":   bandit = new MultiarmedBanditSoftmaxEI(executor, arms.size(), 5); break;
                        case "qlearn":  bandit = new Qlearning(executor, arms.size(), 0.5, 0.7); break;
                        case "poker":  bandit = new MultiarmedBanditPoker(executor, arms.size(), iterAll); break;
                        default: bandit = new MultiarmedBanditUCB1EI(executor, arms.size()); break;
                    }

                    //MultiarmedBandit bandit = new MultiarmedBanditUCB1(executor, arms.size());
                    //MultiarmedBandit bandit = new MultiarmedBanditEpsGreedy(executor, arms.size(), 0.4);
                    //MultiarmedBandit bandit = new MultiarmedBanditSoftmax(executor, arms.size(), 5);
                    bandit.init(metaDistribution);
                    bandit.iterate(1);

                    for (int iterNum = 2; iterNum <= iterAll; iterNum++) {

                        bandit.iteration(iterNum);

                        long endTime = System.currentTimeMillis();

                        String result = "Experiment name: " + expName
                                + "\nBest algorithm: " + executor.bestAlgo
                                + "\nBest hyperparameters: " + executor.bestParams
                                + "\nBest error estimate: " + executor.bestError + "\n";
                        System.out.println(result);

                        writer.println(expName + "," + bandit.getName() + "," + (endTime - begTime) / 1000 + ","
                                + (iterNum * timeBudget) / 1000 + "," + (timeBudget / 1000) + "," + iterNum + ","
                                + executor.bestError + "," + executor.bestAlgo + "," + executor.bestParams);
                        writer.flush();
                    }

                    for (SMACRunner runner : arms) {
                        runner.stop();
                    }
                    //}
                } catch (FileNotFoundException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        System.exit(0);
    }
}
