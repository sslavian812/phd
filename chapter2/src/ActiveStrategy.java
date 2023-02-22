import ru.ifmo.ml.SMACRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActiveStrategy {

    public static void main(String[] args) {
        List<String> datasets = Arrays.asList(
                "car" //, "yeast", "krvskp", "gcredits", "semeion", "abalon", "wine", "dexter", "waveform",
                //"shuttle", "madelon", "secom"
                //, "amazon", "dorothea", "convex", "cup", "guisette", "mnist", "cifar10small", "mnistrotation", "cifar10"
                );

        for (String expName : datasets) {
            int seed = 1;
            AlgorithmCreater algCreater = new AlgorithmCreater(seed, expName + "_train.arff", expName + "_test.arff", expName + "_new");
            long begTime = System.currentTimeMillis();
            List<SMACRunner> arms = new ArrayList<>();
            List<Algorithms> algorithms = new ArrayList<>();
            algorithms.add(Algorithms.LogisticRegression);
            algorithms.add(Algorithms.RandomForest);
            algorithms.add(Algorithms.kNN);
            algorithms.add(Algorithms.SVM);

            for (Algorithms algo : algorithms) {
                arms.add(algCreater.create(algo, algo.name()));
            }

            AlgorithmExecutor executor = new AlgorithmExecutor(arms, new BanditTimer(30000, Constants.BANDIT_ALL_RUN_ITERATION));
            MultiarmedBandit bandit = new MultiarmedBanditSoftmaxEI(executor, arms.size(), 0.5);

            bandit.init(new double[7]);
            bandit.iterate(10);
            //double eps = 0.7;
            //bandit.epsGreedy(eps, Constants.BANDIT_ITERATION_NUM);

            long endTime = System.currentTimeMillis();

            for (SMACRunner runner : arms) {
                runner.stop();
            }

            String result = "Best algorithm: " + executor.bestAlgo + "\nBest hyperparameters: " + executor.bestParams + "\nBest error estimate: " + executor.bestError + "\n";
            System.out.println(result);

            try (PrintWriter writer = new PrintWriter("out" + File.separator + "result" + File.separator + expName + "_band_res" + (begTime % 100000000) + ".txt", "UTF-8")) {
                writer.println("Algorithms:");
                for (Algorithms algo : algorithms) {
                    writer.print(algo.name() + "\n");
                }
                writer.print("\n");
                //writer.println("Bandit algorithm: epsilon-greedy, eps = " + eps);
                writer.println("Bandit algorithm: Soft5EI");
                writer.println("Bandit iterations: " + Constants.BANDIT_ITERATION_NUM);
                writer.println("Bandit iteration budget: " + Constants.BANDIT_ITERATION_TIMEOUT);
                writer.println("All time (seconds): " + (endTime - begTime) / 1000 + "\n");
                writer.println("Multi-armed bandit result:");
                writer.println(result);
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }
}
