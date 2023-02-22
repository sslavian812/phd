import java.awt.geom.Arc2D;
import java.awt.image.AreaAveragingScaleFilter;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;

public class JSCreator {

    public static void main(String[] args) {
        //heatmap();
        //graph();
        //graphSeed();
        smth();
    }

    public static void graphSeed() {
        //String expName = "car";
        //String expName = "gcredits";
        //String expName = "wine";
        //String expName = "yeast";
        //String expName = "krvskp";
        //String expName = "semeion";
        //String expName = "abalon";
        //String expName = "dexter";
        //String expName = "waveform";
        //String expName = "shuttle";
        //String expName = "madelon";
        //String expName = "secom";
        //String expName = "dorothea";

        List<String> datasets = Arrays.asList("car", "yeast", "krvskp", "semeion", "shuttle", "dexter", "waveform", "secom", "dorothea", "gcredits");

        for (String expName : datasets) {
            List<String> algorithms = Arrays.asList("autoweka_result", "bandit_result_ucb1", "bandit_result_eps4", "bandit_result_eps6", "bandit_result_soft",
                    "bandit_result_EI_ucb1", "bandit_result_EI_soft");

            String resFolder = "out" + File.separator + "result" + File.separator + expName;

            File[] files = new File(resFolder).listFiles();
            ArrayList<ArrayList<Double>> graphData = new ArrayList<>();

            for (String algo : algorithms) {
                System.out.print(expName + ", " + algo + " ");
                ArrayList<ArrayList<Double>> algoRes = new ArrayList<>();
                for (File file : files) {
                    String name = file.getName();
                    if (name.startsWith(algo)) {
                        ArrayList<Double> tmp = new ArrayList<>();

                        try (BufferedReader br = new BufferedReader(new FileReader(resFolder + File.separator + name))) {
                            String line = br.readLine();
                            while (line != null) {     // 240..10800
                                if (line.contains(expName)) {
                                    String[] points = line.split(",");
                                    if (points[0].equals(expName)) {
                                        if (algo.contains("autoweka")) {
                                            tmp.add(Double.parseDouble(points[3]));
                                        } else {
                                            tmp.add(Double.parseDouble(points[6]));
                                        }
                                    }
                                }
                                line = br.readLine();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        algoRes.add(tmp);
                    }
                }
                graphData.add(minimum(algoRes));
                //graphData.add(maximum(algoRes));
                //graphData.add(mean(algoRes));
            }
            createGraph("min", resFolder, expName, graphData);
            //createGraph("max", resFolder, expName, graphData);
            //createGraph("mean", resFolder, expName, graphData);
        }
    }

    public static void createGraph(String type, String resFolder, String expName, ArrayList<ArrayList<Double>> graphData) {
        try (PrintWriter writer = new PrintWriter(resFolder + File.separator + "graph" + File.separator + expName + "_" + type + ".html", "UTF-8")) {
            writer.println("<head>\n" +
                    "<script src=\"https://cdn.plot.ly/plotly-1.2.0.min.js\"></script>\n" + "</head>\n"
                    + "<div id=\"graph\" style=\"width:1100px;height:700px;\"></div>\n"
                    + "<script>\n");

            writer.print("var layout = {\n" +
                    "xaxis: {\n" +
                    "title: 'Timeout, s'\n" +
                    "},\n" +
                    "yaxis: {\n" +
                    "title: 'Error, %'\n" +
                    "},\n" +
                    "title: '" + expName + " dataset'" +
                    "};");

            // -------------------

            writer.print("var trace1 = {\n" +
                    "x: [240");

            for (int i = 300; i <= 10800; i += 60) {
                writer.print(", " + i);
            }
            writer.println("],\n" +
                    "y: [");

            int k = 0;
            for (double d : graphData.get(0)) {
                if (k == 0) {
                    writer.print(d);
                } else {
                    writer.print(", " + d);
                }
                k++;
            }

            writer.println("],");

            writer.print("mode: 'lines',\n" +
                    "name: 'Autoweka',\n" +
                    "line: {\n" +
                    "    dash: 'solid',\n" +
                    "    width: 4\n" +
                    "  }\n" +
                    "};");

            // ----------------------

            writer.print("var trace2 = {\n" +
                    "x: [240");

            for (int i = 300; i <= 10800; i += 30) {
                writer.print(", " + i);
            }
            writer.println("],\n" +
                    "y: [");

            k = 0;
            for (double d : graphData.get(1)) {
                if (k == 0) {
                    writer.print(d);
                } else {
                    writer.print(", " + d);
                }
                k++;
            }

            writer.println("],");

            writer.print("mode: 'lines',\n" +
                    "name: 'Bandit UCB1',\n" +
                    "line: {\n" +
                    "    dash: 'dashdot',\n" +
                    "    width: 4\n" +
                    "  }\n" +
                    "};");


            // ---------------------------------------

            writer.print("var trace3 = {\n" +
                    "x: [240");

            for (int i = 300; i <= 10800; i += 30) {
                writer.print(", " + i);
            }
            writer.println("],\n" +
                    "y: [");

            k = 0;
            for (double d : graphData.get(2)) {
                if (k == 0) {
                    writer.print(d);
                } else {
                    writer.print(", " + d);
                }
                k++;
            }

            writer.println("],");

            writer.print("mode: 'lines',\n" +
                    "name: 'Bandit Epsilon-greedy, eps = 0.4',\n" +
                    "line: {\n" +
                    "    dash: 'dot',\n" +
                    "    width: 4\n" +
                    "  }\n" +
                    "};");


            // ---------------------------------------

            writer.print("var trace4 = {\n" +
                    "x: [240");

            for (int i = 300; i <= 10800; i += 30) {
                writer.print(", " + i);
            }
            writer.println("],\n" +
                    "y: [");

            k = 0;
            for (double d : graphData.get(3)) {
                if (k == 0) {
                    writer.print(d);
                } else {
                    writer.print(", " + d);
                }
                k++;
            }

            writer.println("],");

            writer.print("mode: 'lines',\n" +
                    "name: 'Bandit Epsilon-greedy, eps = 0.6',\n" +
                    "line: {\n" +
                    "    dash: 'dot',\n" +
                    "    width: 4\n" +
                    "  }\n" +
                    "};");

            // ---------------------------------------

            writer.print("var trace5 = {\n" +
            "x: [240");

            for (int i = 300; i <= 10800; i += 30) {
                writer.print(", " + i);
            }
            writer.println("],\n" +
                    "y: [");

            k = 0;
            for (double d : graphData.get(4)) {
                if (k == 0) {
                    writer.print(d);
                } else {
                    writer.print(", " + d);
                }
                k++;
            }

            writer.println("],");

            writer.print("mode: 'lines',\n" +
                    "name: 'Bandit Softmax, tau = 5',\n" +
                    "line: {\n" +
                    "    dash: 'dashdot',\n" +
                    "    width: 4\n" +
                    "  }\n" +
                    "};");

            // ---------------------------------------

            writer.print("var trace6 = {\n" +
                    "x: [240");

            for (int i = 300; i <= 10800; i += 30) {
                writer.print(", " + i);
            }
            writer.println("],\n" +
                    "y: [");

            k = 0;
            for (double d : graphData.get(5)) {
                if (k == 0) {
                    writer.print(d);
                } else {
                    writer.print(", " + d);
                }
                k++;
            }

            writer.println("],");

            writer.print("mode: 'lines',\n" +
                    "name: 'Bandit UCB1 with EI reward',\n" +
                    "line: {\n" +
                    "    dash: 'dashdot',\n" +
                    "    width: 4\n" +
                    "  }\n" +
                    "};");

            // ---------------------------------------
            writer.print("var trace7 = {\n" +
                    "x: [240");

            for (int i = 300; i <= 10800; i += 30) {
                writer.print(", " + i);
            }
            writer.println("],\n" +
                    "y: [");

            k = 0;
            for (double d : graphData.get(6)) {
                if (k == 0) {
                    writer.print(d);
                } else {
                    writer.print(", " + d);
                }
                k++;
            }

            writer.println("],");

            writer.print("mode: 'lines',\n" +
                    "name: 'Bandit Softmax with EI reward, tau = 5',\n" +
                    "line: {\n" +
                    "    dash: 'dashdot',\n" +
                    "    width: 4\n" +
                    "  }\n" +
                    "};");

            // ---------------------------------------

            writer.println("var data = [trace1, trace2, trace3, trace4, trace5, trace6, trace7];");
            writer.print("Plotly.newPlot(document.getElementById('graph'), data, layout);");
            writer.println("</script>");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Double> minimum(ArrayList<ArrayList<Double>> data) {
        double min = Double.MAX_VALUE;
        int ind = -1;
        for (int i = 0; i < data.size(); i++) {
            ArrayList<Double> list = data.get(i);
            double val = list.get(list.size() - 1);
            if (val < min) {
                min = val;
                ind = i;
            }
        }
        System.out.println("best res ind = " + ind + ", best res val = " + min + "\n");
        return data.get(ind);
    }

    public static ArrayList<Double> maximum(ArrayList<ArrayList<Double>> data) {
        double min = -Double.MIN_VALUE;
        int ind = -1;
        for (int i = 0; i < data.size(); i++) {
            ArrayList<Double> list = data.get(i);
            double val = list.get(list.size() - 1);
            if (val > min) {
                min = val;
                ind = i;
            }
        }
        return data.get(ind);
    }

    public static ArrayList<Double> mean(ArrayList<ArrayList<Double>> data) {
        int size = data.size();
        int ds = 400;
        for (ArrayList<Double> list : data) {
            if (list.size() < ds) {
                ds = list.size();
            }
        }
        ArrayList<Double> res = new ArrayList<>();
        for (int i = 0; i < ds; i++) {
            res.add(0.0);
        }
        for (ArrayList<Double> list : data) {
            for (int i = 0; i < res.size(); i++) {
                res.set(i, res.get(i) + list.get(i));
            }
        }
        for (int i = 0; i < res.size(); i++) {
            res.set(i, res.get(i) / size);
        }
        return res;
    }

    public static void graph() {
        String expName = "car";
        //String expName = "gcredits";
        //String expName = "wine";
        //String expName = "yeast";
        //String expName = "krvskp";
        //String expName = "semeion";
        //String expName = "abalon";
        //String expName = "dexter";
        //String expName = "waveform";
        //String expName = "shuttle";
        //String expName = "madelon";
        //String expName = "secom";
        //String expName = "dorothea";
        try (PrintWriter writer = new PrintWriter("bandit_" + expName + "_ucb1_30s.html", "UTF-8")) {
                writer.println("<head>\n" +
                        "<script src=\"https://cdn.plot.ly/plotly-1.2.0.min.js\"></script>\n" + "</head>\n"
                        + "<div id=\"autoweka\" style=\"width:1100px;height:700px;\"></div>\n"
                        + "<script>\n");

                writer.print("var layout = {\n" +
                    "xaxis: {\n" +
                    "title: 'timeout, s'\n" +
                    "},\n" +
                    "yaxis: {\n" +
                    "title: 'error'\n" +
                    "},\n" +
                    "title: '" + expName + " dataset'" +
                    "};");

            writer.print("var trace1 = {\n" +
                    "x: [210");

            for (int i = 240; i <= 10800; i += 30) {
                writer.print(", " + i);
            }
            writer.println("],\n" +
                    "y: [");

            try(BufferedReader br = new BufferedReader(new FileReader("bandit_result_ucb1_30s.txt"))) {
                String line = br.readLine();
                //line = br.readLine();
                String[] points = line.split(",");
                if (points[0].equals(expName)) {
                    writer.print(Double.parseDouble(points[6]));
                }
                line = br.readLine();
                while (line != null) {
                    if (line.contains(expName)) {
                        points = line.split(",");
                        if (points[0].equals(expName)) {
                            writer.print(", " + Double.parseDouble(points[6]));
                        }
                    }
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            writer.println("],");

            writer.println("fill: 'tozeroy',\n" +
                    "type: 'scatter'\n" +
                    "};");

            writer.println("var data = [trace1];");
            writer.print("Plotly.newPlot(document.getElementById('autoweka'), data, layout);");
            writer.println("</script>");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void heatmap() {
        //String expName = "car";
        //String expName = "gcredits";
        String expName = "wine";
        try (PrintWriter writer = new PrintWriter("bandits_" + expName + "_ucb1_1.html", "UTF-8")) {
            writer.println("<head>\n" +
                    "<script src=\"https://cdn.plot.ly/plotly-1.2.0.min.js\"></script>\n" + "</head>\n"
                    + "<div id=\"active\" style=\"width:1100px;height:700px;\"></div>\n"
                    + "<script>\n");

            writer.print("var layout = {\n" +
                    "xaxis: {\n" +
                    "title: 'iterations'\n" +
                    "},\n" +
                    "yaxis: {\n" +
                    "title: 'seconds'\n" +
                    "}\n" +
                    "};");

            writer.print("var data = [\n" +
                    "  {\n" +
                    "    z: [");

            int curIt = 46;
            try(BufferedReader br = new BufferedReader(new FileReader("bandit_result_ucb1_2.txt"))) {
                String line = br.readLine();
                while (line != null) {
                    line = br.readLine();
                    if (line.contains(expName)) {
                        for (int itTime = 10; itTime <= curIt; itTime += 3) {
                            writer.print("[");
                            for (int itNum = 10; itNum <= 60; itNum++) {
                                if (line == null) {
                                    break;
                                }
                                String[] points = line.split(",");
                                if (points[0].equals(expName)) {
                                    writer.print(Double.parseDouble(points[6]));
                                }
                                if (itNum == 60) {
                                    writer.print("]");
                                } else {
                                    writer.print(", ");
                                }
                                line = br.readLine();
                            }
                            if (itTime != curIt) {
                                writer.print(",");
                            }
                        }
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            writer.println("],");

            writer.print("x: [");
            for (int itNum = 10; itNum < 60; itNum++) {
                writer.print(itNum + ",");
            }
            writer.print("60],");

            writer.print("y: [");
            for (int itTime = 10; itTime < curIt; itTime += 3) {
                writer.print(itTime + ",");
            }
            writer.print(curIt + "],");

            writer.println("colorscale: 'YIGnBu',\ntype: 'heatmap'\n" +
                    "  }\n" +
                    "];\n");

            writer.print("Plotly.newPlot(document.getElementById('active'), data, layout);");
            writer.println("</script>");
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void smth() {
        String expName = "car";

        List<String> algorithms = Arrays.asList("bandit_result_EI_soft");

        String resFolder = "out" + File.separator + "result" + File.separator + expName;

        File[] files = new File(resFolder).listFiles();
        ArrayList<ArrayList<Double>> algoRes = new ArrayList<>();
        ArrayList<ArrayList<String>> algos = new ArrayList<>();
        ArrayList<String> seeds = new ArrayList<>();

        for (String algo : algorithms) {
            System.out.print(expName + ", " + algo + " ");
            for (File file : files) {
                String name = file.getName();
                if (name.startsWith(algo)) {
                    ArrayList<Double> tmp = new ArrayList<>();
                    ArrayList<String> tmp2 = new ArrayList<>();
                    String[] nameParts = name.split("_");
                    seeds.add(nameParts[nameParts.length - 1].split("\\.")[0]);

                    try (BufferedReader br = new BufferedReader(new FileReader(resFolder + File.separator + name))) {
                        String line = br.readLine();
                        while (line != null) {     // 240..10800
                            if (line.contains(expName)) {
                                String[] points = line.split(",");
                                if (points[0].equals(expName)) {
                                    tmp.add(Double.parseDouble(points[6]));
                                }
                                String[] points2 = points[7].split("\\.");
                                tmp2.add(points2[points2.length - 1]);
                            }
                            line = br.readLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    algoRes.add(tmp);
                    algos.add(tmp2);
                }
            }
        }
        createGraphNew("algo", resFolder, expName, algoRes, algos, seeds);
    }

    public static void createGraphNew(String type, String resFolder, String expName, ArrayList<ArrayList<Double>> graphData, ArrayList<ArrayList<String>> algos, ArrayList<String> seeds) {
        for (int j = 0; j < graphData.size(); j++) {
            ArrayList<Double> data = graphData.get(j);
            String seed = seeds.get(j);
            try (PrintWriter writer = new PrintWriter(resFolder + File.separator + "graph" + File.separator + expName + "_" + type + "_" + seed + ".html", "UTF-8")) {
                writer.println("<head>\n" +
                        "<script src=\"https://cdn.plot.ly/plotly-1.2.0.min.js\"></script>\n" + "</head>\n"
                        + "<div id=\"graph\" style=\"width:1100px;height:700px;\"></div>\n"
                        + "<script>\n");

                writer.print("var layout = {\n" +
                        "xaxis: {\n" +
                        "title: 'Timeout, s'\n" +
                        "},\n" +
                        "yaxis: {\n" +
                        "title: 'Error, %'\n" +
                        "},\n" +
                        "title: '" + expName + " dataset, Softmax algorithm'" +
                        "};");

                // -------------------

                writer.print("var trace1 = {\n" +
                        "x: [");

                int k = 0;
                for (int i = 300; i <= 10800; i += 60) {
                    if (algos.get(j).get(i / 60 - 5).equals("SMO")) {
                        if (k == 0)  {
                            writer.print(i);
                        } else {
                            writer.print(", " + i);
                        }
                        k++;
                    }
                }
                writer.println("],\n" +
                        "y: [");

                k = 0;
                for (int i = 300; i <= 10800; i += 60) {
                    int ii = i / 60 - 5;
                    double d = graphData.get(j).get(ii);
                    if (algos.get(j).get(i / 60 - 5).equals("SMO")) {
                        if (k == 0) {
                            writer.print(d);
                        } else {
                            writer.print(", " + d);
                        }
                        k++;
                    }
                }

                writer.println("],");

                writer.print("mode: 'markers',\n" +
                        "name: 'SVM',\n" +
                        "type: 'scatter',\n" +
                        "marker: {size: 12}" +
                        "};");

                // ---------------------------------------

                writer.print("var trace2 = {\n" +
                        "x: [");


                k = 0;
                for (int i = 300; i <= 10800; i += 60) {
                    if (algos.get(j).get(i / 60 - 5).equals("J48")) {
                        if (k == 0) {
                            writer.print(i);
                        } else {
                            writer.print(", " + i);
                        }
                        k++;
                    }
                }
                writer.println("],\n" +
                        "y: [");

                k = 0;
                for (int i = 300; i <= 10800; i += 60) {
                    int ii = i / 60 - 5;
                    double d = graphData.get(j).get(ii);
                    if (algos.get(j).get(i / 60 - 5).equals("J48")) {
                        if (k == 0) {
                            writer.print(d);
                        } else {
                            writer.print(", " + d);
                        }
                        k++;
                    }
                }

                writer.println("],");

                writer.print("mode: 'markers',\n" +
                        "name: 'C4.5 Decision Tree',\n" +
                        "type: 'scatter',\n" +
                        "marker: {size: 12}" +
                        "};");

                // ---------------------------------------

                writer.print("var trace3 = {\n" +
                        "x: [");

                k = 0;
                for (int i = 300; i <= 10800; i += 60) {
                    if (algos.get(j).get(i / 60 - 5).equals("MultilayerPerceptron")) {
                        if (k == 0) {
                            writer.print(i);
                        } else {
                            writer.print(", " + i);
                        }
                        k++;
                    }
                }
                writer.println("],\n" +
                        "y: [");

                k = 0;
                for (int i = 300; i <= 10800; i += 60) {
                    int ii = i / 60 - 5;
                    double d = graphData.get(j).get(ii);
                    if (algos.get(j).get(i / 60 - 5).equals("MultilayerPerceptron")) {
                        if (k == 0) {
                            writer.print(d);
                        } else {
                            writer.print(", " + d);
                        }
                        k++;
                    }
                }

                writer.println("],");

                writer.print("mode: 'markers',\n" +
                        "name: 'Multilayer Perceptron',\n" +
                        "type: 'scatter',\n" +
                        "marker: {size: 12}" +
                        "};");

                // ---------------------------------------

                writer.print("var trace4 = {\n" +
                        "x: [");

                k = 0;
                for (int i = 300; i <= 10800; i += 60) {
                    if (algos.get(j).get(i / 60 - 5).equals("RandomForest")) {
                        if (k == 0) {
                            writer.print(i);
                        } else {
                            writer.print(", " + i);
                        }
                        k++;
                    }
                }
                writer.println("],\n" +
                        "y: [");

                k = 0;
                for (int i = 300; i <= 10800; i += 60) {
                    int ii = i / 60 - 5;
                    double d = graphData.get(j).get(ii);
                    if (algos.get(j).get(i / 60 - 5).equals("RandomForest")) {
                        if (k == 0) {
                            writer.print(d);
                        } else {
                            writer.print(", " + d);
                        }
                        k++;
                    }
                }

                writer.println("],");

                writer.print("mode: 'markers',\n" +
                        "name: 'Random Forest',\n" +
                        "type: 'scatter',\n" +
                        "marker: {size: 12}" +
                        "};");

                // ---------------------------------------

                writer.print("var trace5 = {\n" +
                        "x: [");

                k = 0;
                for (int i = 300; i <= 10800; i += 60) {
                    if (algos.get(j).get(i / 60 - 5).equals("IBk")) {
                        if (k == 0) {
                            writer.print(i);
                        } else {
                            writer.print(", " + i);
                        }
                        k++;
                    }
                }
                writer.println("],\n" +
                        "y: [");

                k = 0;
                for (int i = 300; i <= 10800; i += 60) {
                    int ii = i / 60 - 5;
                    double d = graphData.get(j).get(ii);
                    if (algos.get(j).get(i / 60 - 5).equals("IBk")) {
                        if (k == 0) {
                            writer.print(d);
                        } else {
                            writer.print(", " + d);
                        }
                        k++;
                    }
                }

                writer.println("],");

                writer.print("mode: 'markers',\n" +
                        "name: 'kNN',\n" +
                        "type: 'scatter',\n" +
                        "marker: {size: 12}" +
                        "};");

                // ---------------------------------------

                writer.print("var trace6 = {\n" +
                        "x: [");

                k = 0;
                for (int i = 300; i <= 10800; i += 60) {
                    if (algos.get(j).get(i / 60 - 5).equals("Logistic")) {
                        if (k == 0) {
                            writer.print(i);
                        } else {
                            writer.print(", " + i);
                        }
                        k++;
                    }
                }
                writer.println("],\n" +
                        "y: [");

                k = 0;
                for (int i = 300; i <= 10800; i += 60) {
                    int ii = i / 60 - 5;
                    double d = graphData.get(j).get(ii);
                    if (algos.get(j).get(i / 60 - 5).equals("Logistic")) {
                        if (k == 0) {
                            writer.print(d);
                        } else {
                            writer.print(", " + d);
                        }
                        k++;
                    }
                }

                writer.println("],");

                writer.print("mode: 'markers',\n" +
                        "name: 'Logistic',\n" +
                        "type: 'scatter',\n" +
                        "marker: {size: 12}" +
                        "};");


                writer.println("var data = [trace1, trace2, trace3, trace4, trace5, trace6];");
                writer.print("Plotly.newPlot(document.getElementById('graph'), data, layout);");
                writer.println("</script>");
                writer.close();
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

}
