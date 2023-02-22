import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Result {
    static String res = "results.txt";
    static String test = "results2.txt";

    public static void main(String[] args) {
        getBestClassifier();
        printCode();
        //getBestForTest();
    }

    public static void getBestClassifier() {
        try (PrintWriter writer = new PrintWriter(res, "UTF-8")) {
            String filename = "";
            double bestRes = 100.0;
            String bestLine = "";
            String bestSeed = "";
            int k = 0;
            for (File file : new File("datasets/result").listFiles()) {
                if (!filename.equals("")) {
                    String[] mas = filename.split("_");
                    String[] mas1 = file.getName().split("_");
                    if (!mas[3].equals(mas1[3])) {
                        k++;
                        if (bestRes == 100.0) {
                            System.out.println(mas[3]);
                        } else {
                            writer.println(mas[3] + " " + bestSeed + " " + bestLine + " best error = " + bestRes);
                        }
                        bestRes = 100.0;
                    }
                }
                filename = file.getName();
                try (BufferedReader bf = new BufferedReader(new FileReader(file))) {
                    String s = "";
                    while ((s = bf.readLine()) != null) {
                        String[] mass = s.split(",");
                        double error = Double.parseDouble(mass[6]);
                        if (error <= bestRes) {
                            bestRes = error;
                            bestLine = s;
                            String[] mas = filename.split("_");
                            bestSeed = mas[5];
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //writer.println(k);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void printCode() {
        List<String> svm = new ArrayList<>();
        List<String> rf = new ArrayList<>();
        List<String> per = new ArrayList<>();
        List<String> j48 = new ArrayList<>();
        List<String> log = new ArrayList<>();
        List<String> knn = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(res))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] mas = line.split(" ");
                if (mas[2].contains("SMO")) {
                    svm.add(mas[0]);
                } else if (mas[2].contains("RandomForest")) {
                    rf.add(mas[0]);
                } else if (mas[2].contains("Logistic")) {
                    log.add(mas[0]);
                } else if (mas[2].contains("Perceptron")) {
                    per.add(mas[0]);
                } else if (mas[2].contains("J48")) {
                    j48.add(mas[0]);
                } else if (mas[2].contains("IBk")) {
                    knn.add(mas[0]);
                } else {
                    System.out.println("Unknown classifier");
                }
            }
            try (PrintWriter wr = new PrintWriter("result1.txt", "UTF-8")) {
                int i = 0;
                wr.print("                    if        (");
                wr.print("filename.contains(\"" + svm.get(i) + "\")");
                for (i = 1; i < svm.size(); i++) {
                    wr.print(" || filename.contains(\"" + svm.get(i) + "\")");
                }
                wr.print(") {\n" +
                        "                        writer.print(\"1\"); // SVM\n" +
                        "                        svm++;\n");
                wr.print("                    } else if (");
                i = 0;
                wr.print("filename.contains(\"" + rf.get(i) + "\")");
                for (i = 1; i < rf.size(); i++) {
                    wr.print(" || filename.contains(\"" + rf.get(i) + "\")");
                }
                wr.print(") {\n" +
                        "                        writer.print(\"2\"); // Random Forest\n" +
                        "                        forest++;\n");
                wr.print("                    } else if (");
                i = 0;
                wr.print("filename.contains(\"" + per.get(i) + "\")");
                for (i = 1; i < per.size(); i++) {
                    wr.print(" || filename.contains(\"" + per.get(i) + "\")");
                }
                wr.print(") {\n" +
                        "                        writer.print(\"3\"); // Perceptron\n" +
                        "                        percep++;\n");
                wr.print("                    } else if (");
                i = 0;
                wr.print("filename.contains(\"" + j48.get(i) + "\")");
                for (i = 1; i < j48.size(); i++) {
                    wr.print(" || filename.contains(\"" + j48.get(i) + "\")");
                }
                wr.print(") {\n" +
                        "                        writer.print(\"4\"); // Decision Tree\n" +
                        "                        decs++;\n");
                wr.print("                    } else if (");
                i = 0;
                wr.print("filename.contains(\"" + knn.get(i) + "\")");
                for (i = 1; i < knn.size(); i++) {
                    wr.print(" || filename.contains(\"" + knn.get(i) + "\")");
                }
                wr.print(") {\n" +
                        "                        writer.print(\"5\"); // kNN\n" +
                        "                        knn++;\n");
                wr.print("                    } else if (");
                i = 0;
                wr.print("filename.contains(\"" + log.get(i) + "\")");
                for (i = 1; i < log.size(); i++) {
                    wr.print(" || filename.contains(\"" + log.get(i) + "\")");
                }
                wr.print(") {\n" +
                        "                        writer.print(\"6\"); // Logistic Regression\n" +
                        "                        regr++;\n");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void getBestForTest() {
        try (PrintWriter writer = new PrintWriter(test, "UTF-8")) {
            File folder = new File("out/result");
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                if (!file.getName().contains(".txt")) {
                    double bestRes = 100.0;
                    String bestLine = "";
                    String bestSeed = "";
                    File[] dataFiles = file.listFiles();
                    for (File f : dataFiles) {
                        if (f.getName().contains("EI_soft5")) {
                            String filename = f.getName();
                            try (BufferedReader bf = new BufferedReader(new FileReader(f))) {
                                String s = "";
                                while ((s = bf.readLine()) != null) {
                                    String[] mass = s.split(",");
                                    try {
                                        double error = Double.parseDouble(mass[6]);
                                        if (error <= bestRes) {
                                            bestRes = error;
                                            bestLine = s;
                                            String[] mas = filename.split("_");
                                            bestSeed = mas[6];
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    writer.println(file.getName() + " " + bestSeed + " " + bestLine + " best error = " + bestRes);
                }
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
