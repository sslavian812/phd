import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class MetaFeaturesProc {

    public static void main(String[] args) {
        //MetaFeaturesExtr.extractToFile();

        String[] algos = {"SVM", "RF", "Percepron", "DT", "kNN", "LR", "other"};
        File folder = new File(".");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            String name = file.toString();
            if (name.contains(".arff")) {
                if (name.substring(name.length() - 10).equals("train.arff") && !name.equals(Constants.META_ARFF_FILE) && !name.equals(Constants.META_SELECTED_ARFF_FILE)) {
                    String newName = "meta/" + name.substring(2, 5) + ".arff";
                    MetaFeaturesExtr.extractFromOne(file.toString(), newName, true);
                    try (Reader reader = new FileReader(Constants.META_ARFF_FILE)) {
                        Instances instances = new Instances(reader);
                        MultilayerPerceptron per = new MultilayerPerceptron();
                        instances.setClassIndex(instances.numAttributes() - 1);
                        per.buildClassifier(instances);
                        try (Reader reader1 = new FileReader(newName)) {
                            Instances curInstances = new Instances(reader1);
                            curInstances.setClassIndex(curInstances.numAttributes() - 1);
                            //int clazz = (int)per.classifyInstance(curInstances.get(0));
                            //System.out.println(clazz);
                            double[] distribution = per.distributionForInstance(curInstances.get(0));
                            System.out.print(name);
                            for (int i = 0; i < distribution.length; i++) {
                                System.out.print(" " + algos[i] + ": " + distribution[i] + ",");
                            }
                            System.out.println("\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static double[] getDistribution(String filename) {
        String[] algos = {"SVM", "RF", "Percepron", "DT", "kNN", "LR", "other"};
        String newName = "meta/" + filename.substring(2, 5) + ".arff";
        MetaFeaturesExtr.extractFromOne(filename, newName, false);
        try (Reader reader = new FileReader(Constants.META_ARFF_FILE)) {
            Instances instances = new Instances(reader);
            MultilayerPerceptron per = new MultilayerPerceptron();
            instances.setClassIndex(instances.numAttributes() - 1);
            per.buildClassifier(instances);
            try (Reader reader1 = new FileReader(newName)) {
                Instances curInstances = new Instances(reader1);
                curInstances.setClassIndex(curInstances.numAttributes() - 1);
                double[] distribution = per.distributionForInstance(curInstances.get(0));
                System.out.print(filename);
                for (int i = 0; i < distribution.length; i++) {
                    System.out.print(" " + algos[i] + ": " + distribution[i] + ",");
                }
                System.out.println("\n");
                return distribution;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new double[7];
    }
}
