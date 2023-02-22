import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.ifmo.recommendersystem.metafeatures.MetaFeatureExtractor;

import weka.core.Instances;

public class MetaFeaturesExtr {

    public static void extractToFile() {
        printArff();
        //printCsv();
    }

    public static void printArff() {
        List<MetaFeatureExtractor> extractors = listMetaFeatures();
        try (PrintWriter writer = new PrintWriter(Constants.META_ARFF_FILE, "UTF-8")) {
            try (PrintWriter writerSel = new PrintWriter(Constants.META_SELECTED_ARFF_FILE, "UTF-8")) {
                createHeader(writer, extractors);
                createHeader(writerSel, extractors);

                int k = 0;
                int svm = 0, forest = 0, percep = 0, knn = 0, regr = 0, decs = 0, oth = 0;
                File[] folder = new File("datasets/meta").listFiles();
                for (File file : folder) {
                    k++;
                    try (Reader reader = new FileReader(file)) {
                        createFeatures(reader, extractors, writer, false);
                        //createFeatures(reader, extractors, writerSel, true);
                        String filename = file.getName();
                        if        (filename.contains("0011") || filename.contains("1006") || filename.contains("1023") || filename.contains("1030") || filename.contains("1048") || filename.contains("1051") || filename.contains("1058") || filename.contains("1071") || filename.contains("1075") || filename.contains("1100") || filename.contains("1117") || filename.contains("1435") || filename.contains("1465") || filename.contains("1467") || filename.contains("1480") || filename.contains("1482") || filename.contains("1495") || filename.contains("1498") || filename.contains("1513") || filename.contains("1516") || filename.contains("1529") || filename.contains("1530") || filename.contains("1540") || filename.contains("1546") || filename.contains("1559") || filename.contains("1571") || filename.contains("1574") || filename.contains("164") || filename.contains("172") || filename.contains("174") || filename.contains("185") || filename.contains("187") || filename.contains("192") || filename.contains("200") || filename.contains("206") || filename.contains("231") || filename.contains("232") || filename.contains("278") || filename.contains("307") || filename.contains("333") || filename.contains("337") || filename.contains("408") || filename.contains("413") || filename.contains("431") || filename.contains("454") || filename.contains("461") || filename.contains("466") || filename.contains("470") || filename.contains("474") || filename.contains("475") || filename.contains("483") || filename.contains("485") || filename.contains("492") || filename.contains("500") || filename.contains("502") || filename.contains("527") || filename.contains("546") || filename.contains("680") || filename.contains("681") || filename.contains("683") || filename.contains("692") || filename.contains("693") || filename.contains("697") || filename.contains("711") || filename.contains("741") || filename.contains("753") || filename.contains("758") || filename.contains("771") || filename.contains("776") || filename.contains("796") || filename.contains("825") || filename.contains("827") || filename.contains("839") || filename.contains("970") || filename.contains("973") || filename.contains("982") || filename.contains("987") || filename.contains("997") || filename.contains("998")) {
                            writer.print("1"); // SVM
                            writerSel.print("1");
                            svm++;
                        } else if (filename.contains("0001") || filename.contains("0010") || filename.contains("0015") || filename.contains("1005") || filename.contains("1007") || filename.contains("1016") || filename.contains("1025") || filename.contains("1061") || filename.contains("1089") || filename.contains("1091") || filename.contains("1093") || filename.contains("1115") || filename.contains("1228") || filename.contains("1436") || filename.contains("1447") || filename.contains("1450") || filename.contains("1453") || filename.contains("1463") || filename.contains("1473") || filename.contains("1490") || filename.contains("1494") || filename.contains("1517") || filename.contains("1518") || filename.contains("1519") || filename.contains("1527") || filename.contains("1543") || filename.contains("1547") || filename.contains("1552") || filename.contains("1554") || filename.contains("1572") || filename.contains("181") || filename.contains("183") || filename.contains("188") || filename.contains("196") || filename.contains("212") || filename.contains("213") || filename.contains("224") || filename.contains("230") || filename.contains("275") || filename.contains("277") || filename.contains("285") || filename.contains("329") || filename.contains("334") || filename.contains("339") || filename.contains("428") || filename.contains("452") || filename.contains("453") || filename.contains("465") || filename.contains("467") || filename.contains("468") || filename.contains("476") || filename.contains("477") || filename.contains("478") || filename.contains("479") || filename.contains("486") || filename.contains("488") || filename.contains("501") || filename.contains("504") || filename.contains("506") || filename.contains("513") || filename.contains("516") || filename.contains("535") || filename.contains("536") || filename.contains("542") || filename.contains("543") || filename.contains("563") || filename.contains("567") || filename.contains("568") || filename.contains("570") || filename.contains("576") || filename.contains("577") || filename.contains("578") || filename.contains("682") || filename.contains("700") || filename.contains("705") || filename.contains("713") || filename.contains("717") || filename.contains("719") || filename.contains("728") || filename.contains("732") || filename.contains("740") || filename.contains("743") || filename.contains("746") || filename.contains("750") || filename.contains("760") || filename.contains("762") || filename.contains("768") || filename.contains("775") || filename.contains("778") || filename.contains("783") || filename.contains("784") || filename.contains("786") || filename.contains("789") || filename.contains("791") || filename.contains("793") || filename.contains("794") || filename.contains("799") || filename.contains("800") || filename.contains("802") || filename.contains("811") || filename.contains("812") || filename.contains("817") || filename.contains("820") || filename.contains("824") || filename.contains("826") || filename.contains("830") || filename.contains("832") || filename.contains("838") || filename.contains("840") || filename.contains("844") || filename.contains("845") || filename.contains("859") || filename.contains("966") || filename.contains("974") || filename.contains("985") || filename.contains("988") || filename.contains("989") || filename.contains("990") || filename.contains("996")) {
                            writer.print("2"); // Random Forest
                            writerSel.print("2");
                            forest++;
                        } else if (filename.contains("1001") || filename.contains("1011") || filename.contains("1029") || filename.contains("1045") || filename.contains("1054") || filename.contains("1055") || filename.contains("1057") || filename.contains("1059") || filename.contains("1060") || filename.contains("1072") || filename.contains("1094") || filename.contains("1096") || filename.contains("13") || filename.contains("1442") || filename.contains("1449") || filename.contains("1472") || filename.contains("1500") || filename.contains("1510") || filename.contains("1511") || filename.contains("1512") || filename.contains("1524") || filename.contains("1536") || filename.contains("1542") || filename.contains("1545") || filename.contains("1557") || filename.contains("163") || filename.contains("186") || filename.contains("193") || filename.contains("209") || filename.contains("222") || filename.contains("228") || filename.contains("327") || filename.contains("328") || filename.contains("336") || filename.contains("345") || filename.contains("421") || filename.contains("424") || filename.contains("429") || filename.contains("446") || filename.contains("456") || filename.contains("459") || filename.contains("480") || filename.contains("481") || filename.contains("482") || filename.contains("495") || filename.contains("511") || filename.contains("518") || filename.contains("519") || filename.contains("520") || filename.contains("525") || filename.contains("526") || filename.contains("534") || filename.contains("550") || filename.contains("556") || filename.contains("557") || filename.contains("661") || filename.contains("690") || filename.contains("694") || filename.contains("720") || filename.contains("726") || filename.contains("730") || filename.contains("731") || filename.contains("737") || filename.contains("747") || filename.contains("749") || filename.contains("751") || filename.contains("754") || filename.contains("755") || filename.contains("774") || filename.contains("782") || filename.contains("788") || filename.contains("792") || filename.contains("813") || filename.contains("818") || filename.contains("861") || filename.contains("983") || filename.contains("991") || filename.contains("994")) {
                            writer.print("3"); // Perceptron
                            writerSel.print("3");
                            percep++;
                        } else if (filename.contains("1003") || filename.contains("1013") || filename.contains("1014") || filename.contains("1028") || filename.contains("1062") || filename.contains("1063") || filename.contains("1070") || filename.contains("1073") || filename.contains("1074") || filename.contains("1090") || filename.contains("1092") || filename.contains("1095") || filename.contains("1420") || filename.contains("1438") || filename.contains("1441") || filename.contains("1446") || filename.contains("1448") || filename.contains("1451") || filename.contains("1460") || filename.contains("1470") || filename.contains("1499") || filename.contains("1508") || filename.contains("1528") || filename.contains("1532") || filename.contains("1539") || filename.contains("1544") || filename.contains("1549") || filename.contains("1551") || filename.contains("1553") || filename.contains("1564") || filename.contains("190") || filename.contains("202") || filename.contains("210") || filename.contains("214") || filename.contains("276") || filename.contains("335") || filename.contains("342") || filename.contains("409") || filename.contains("419") || filename.contains("437") || filename.contains("443") || filename.contains("444") || filename.contains("450") || filename.contains("451") || filename.contains("455") || filename.contains("463") || filename.contains("471") || filename.contains("472") || filename.contains("497") || filename.contains("515") || filename.contains("531") || filename.contains("533") || filename.contains("540") || filename.contains("541") || filename.contains("549") || filename.contains("553") || filename.contains("555") || filename.contains("559") || filename.contains("561") || filename.contains("565") || filename.contains("569") || filename.contains("575") || filename.contains("665") || filename.contains("679") || filename.contains("685") || filename.contains("689") || filename.contains("691") || filename.contains("716") || filename.contains("721") || filename.contains("724") || filename.contains("729") || filename.contains("733") || filename.contains("736") || filename.contains("738") || filename.contains("757") || filename.contains("759") || filename.contains("767") || filename.contains("769") || filename.contains("770") || filename.contains("773") || filename.contains("777") || filename.contains("779") || filename.contains("785") || filename.contains("787") || filename.contains("798") || filename.contains("801") || filename.contains("814") || filename.contains("848") || filename.contains("860") || filename.contains("984") || filename.contains("992")) {
                            writer.print("4"); // Decision Tree
                            writerSel.print("4");
                            decs++;
                        } else if (filename.contains("1010") || filename.contains("1015") || filename.contains("1026") || filename.contains("1047") || filename.contains("1065") || filename.contains("1066") || filename.contains("1068") || filename.contains("1076") || filename.contains("1097") || filename.contains("1167") || filename.contains("1245") || filename.contains("1412") || filename.contains("1419") || filename.contains("1452") || filename.contains("1462") || filename.contains("1488") || filename.contains("1506") || filename.contains("1520") || filename.contains("1533") || filename.contains("1534") || filename.contains("1565") || filename.contains("171") || filename.contains("195") || filename.contains("203") || filename.contains("205") || filename.contains("207") || filename.contains("229") || filename.contains("338") || filename.contains("340") || filename.contains("406") || filename.contains("426") || filename.contains("427") || filename.contains("448") || filename.contains("449") || filename.contains("457") || filename.contains("460") || filename.contains("464") || filename.contains("469") || filename.contains("490") || filename.contains("523") || filename.contains("530") || filename.contains("659") || filename.contains("687") || filename.contains("696") || filename.contains("706") || filename.contains("712") || filename.contains("744") || filename.contains("764") || filename.contains("765") || filename.contains("780") || filename.contains("804") || filename.contains("808") || filename.contains("815") || filename.contains("829") || filename.contains("835") || filename.contains("836") || filename.contains("841") || filename.contains("842") || filename.contains("972") || filename.contains("975") || filename.contains("986")) {
                            writer.print("5"); // kNN
                            writerSel.print("5");
                            knn++;
                        } else if (filename.contains("1008") || filename.contains("1009") || filename.contains("1012") || filename.contains("1027") || filename.contains("1035") || filename.contains("1049") || filename.contains("1064") || filename.contains("1067") || filename.contains("1098") || filename.contains("1099") || filename.contains("1443") || filename.contains("1444") || filename.contains("1454") || filename.contains("1456") || filename.contains("1464") || filename.contains("1523") || filename.contains("1525") || filename.contains("1526") || filename.contains("1531") || filename.contains("1535") || filename.contains("1538") || filename.contains("1541") || filename.contains("1563") || filename.contains("1600") || filename.contains("173") || filename.contains("194") || filename.contains("199") || filename.contains("204") || filename.contains("217") || filename.contains("343") || filename.contains("412") || filename.contains("458") || filename.contains("462") || filename.contains("487") || filename.contains("491") || filename.contains("494") || filename.contains("498") || filename.contains("510") || filename.contains("524") || filename.contains("532") || filename.contains("544") || filename.contains("551") || filename.contains("566") || filename.contains("663") || filename.contains("675") || filename.contains("686") || filename.contains("703") || filename.contains("710") || filename.contains("714") || filename.contains("739") || filename.contains("745") || filename.contains("748") || filename.contains("756") || filename.contains("763") || filename.contains("772") || filename.contains("790") || filename.contains("795") || filename.contains("810") || filename.contains("828") || filename.contains("831") || filename.contains("858")) {
                            writer.print("6"); // Logistic Regression
                            writerSel.print("6");
                            regr++;
                        } else {
                            writer.print("7");
                            writerSel.print("7");
                            oth++;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    writer.print("\n");
                    System.out.println("k = " + k + " / " + folder.length);
                }
                System.out.println("svm = " + svm + "\nrandom forest = " + forest + "\nperceptron = " + percep + "\ndecision tree = " + decs + "\nknn = " + knn + "\nlogistic regression = " + regr
                + "\nother = " + oth);
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void printCsv() {
        List<MetaFeatureExtractor> extractors = listMetaFeatures();
        try (PrintWriter writer = new PrintWriter(Constants.META_CSV_FILE, "UTF-8")) {
            for (MetaFeatureExtractor extractor : extractors) {
                writer.print(extractor.getName().replace(" ", "") + ",");
            }
            writer.print("class\n");

            int k = 0;
            int svm = 0, forest = 0, percep = 0, knn = 0, regr = 0, decs = 0, oth = 0;
            File[] folder = new File("datasets/meta").listFiles();
            for (File file : folder) {
                k++;
                try (Reader reader = new FileReader(file)) {
                    createFeatures(reader, extractors, writer, false);
                    String filename = file.getName();
                    if        (filename.contains("0011") || filename.contains("1006") || filename.contains("1023") || filename.contains("1030") || filename.contains("1048") || filename.contains("1051") || filename.contains("1058") || filename.contains("1071") || filename.contains("1075") || filename.contains("1100") || filename.contains("1117") || filename.contains("1435") || filename.contains("1465") || filename.contains("1467") || filename.contains("1480") || filename.contains("1482") || filename.contains("1495") || filename.contains("1498") || filename.contains("1513") || filename.contains("1516") || filename.contains("1529") || filename.contains("1530") || filename.contains("1540") || filename.contains("1546") || filename.contains("1559") || filename.contains("1571") || filename.contains("1574") || filename.contains("164") || filename.contains("172") || filename.contains("174") || filename.contains("185") || filename.contains("187") || filename.contains("192") || filename.contains("200") || filename.contains("206") || filename.contains("231") || filename.contains("232") || filename.contains("278") || filename.contains("307") || filename.contains("333") || filename.contains("337") || filename.contains("408") || filename.contains("413") || filename.contains("431") || filename.contains("454") || filename.contains("461") || filename.contains("466") || filename.contains("470") || filename.contains("474") || filename.contains("475") || filename.contains("483") || filename.contains("485") || filename.contains("492") || filename.contains("500") || filename.contains("502") || filename.contains("527") || filename.contains("546") || filename.contains("680") || filename.contains("681") || filename.contains("683") || filename.contains("692") || filename.contains("693") || filename.contains("697") || filename.contains("711") || filename.contains("741") || filename.contains("753") || filename.contains("758") || filename.contains("771") || filename.contains("776") || filename.contains("796") || filename.contains("825") || filename.contains("827") || filename.contains("839") || filename.contains("970") || filename.contains("973") || filename.contains("982") || filename.contains("987") || filename.contains("997") || filename.contains("998")) {
                        writer.print("1"); // SVM
                        svm++;
                    } else if (filename.contains("0001") || filename.contains("0010") || filename.contains("0015") || filename.contains("1005") || filename.contains("1007") || filename.contains("1016") || filename.contains("1025") || filename.contains("1061") || filename.contains("1089") || filename.contains("1091") || filename.contains("1093") || filename.contains("1115") || filename.contains("1228") || filename.contains("1436") || filename.contains("1447") || filename.contains("1450") || filename.contains("1453") || filename.contains("1463") || filename.contains("1473") || filename.contains("1490") || filename.contains("1494") || filename.contains("1517") || filename.contains("1518") || filename.contains("1519") || filename.contains("1527") || filename.contains("1543") || filename.contains("1547") || filename.contains("1552") || filename.contains("1554") || filename.contains("1572") || filename.contains("181") || filename.contains("183") || filename.contains("188") || filename.contains("196") || filename.contains("212") || filename.contains("213") || filename.contains("224") || filename.contains("230") || filename.contains("275") || filename.contains("277") || filename.contains("285") || filename.contains("329") || filename.contains("334") || filename.contains("339") || filename.contains("428") || filename.contains("452") || filename.contains("453") || filename.contains("465") || filename.contains("467") || filename.contains("468") || filename.contains("476") || filename.contains("477") || filename.contains("478") || filename.contains("479") || filename.contains("486") || filename.contains("488") || filename.contains("501") || filename.contains("504") || filename.contains("506") || filename.contains("513") || filename.contains("516") || filename.contains("535") || filename.contains("536") || filename.contains("542") || filename.contains("543") || filename.contains("563") || filename.contains("567") || filename.contains("568") || filename.contains("570") || filename.contains("576") || filename.contains("577") || filename.contains("578") || filename.contains("682") || filename.contains("700") || filename.contains("705") || filename.contains("713") || filename.contains("717") || filename.contains("719") || filename.contains("728") || filename.contains("732") || filename.contains("740") || filename.contains("743") || filename.contains("746") || filename.contains("750") || filename.contains("760") || filename.contains("762") || filename.contains("768") || filename.contains("775") || filename.contains("778") || filename.contains("783") || filename.contains("784") || filename.contains("786") || filename.contains("789") || filename.contains("791") || filename.contains("793") || filename.contains("794") || filename.contains("799") || filename.contains("800") || filename.contains("802") || filename.contains("811") || filename.contains("812") || filename.contains("817") || filename.contains("820") || filename.contains("824") || filename.contains("826") || filename.contains("830") || filename.contains("832") || filename.contains("838") || filename.contains("840") || filename.contains("844") || filename.contains("845") || filename.contains("859") || filename.contains("966") || filename.contains("974") || filename.contains("985") || filename.contains("988") || filename.contains("989") || filename.contains("990") || filename.contains("996")) {
                        writer.print("2"); // Random Forest
                        forest++;
                    } else if (filename.contains("1001") || filename.contains("1011") || filename.contains("1029") || filename.contains("1045") || filename.contains("1054") || filename.contains("1055") || filename.contains("1057") || filename.contains("1059") || filename.contains("1060") || filename.contains("1072") || filename.contains("1094") || filename.contains("1096") || filename.contains("13") || filename.contains("1442") || filename.contains("1449") || filename.contains("1472") || filename.contains("1500") || filename.contains("1510") || filename.contains("1511") || filename.contains("1512") || filename.contains("1524") || filename.contains("1536") || filename.contains("1542") || filename.contains("1545") || filename.contains("1557") || filename.contains("163") || filename.contains("186") || filename.contains("193") || filename.contains("209") || filename.contains("222") || filename.contains("228") || filename.contains("327") || filename.contains("328") || filename.contains("336") || filename.contains("345") || filename.contains("421") || filename.contains("424") || filename.contains("429") || filename.contains("446") || filename.contains("456") || filename.contains("459") || filename.contains("480") || filename.contains("481") || filename.contains("482") || filename.contains("495") || filename.contains("511") || filename.contains("518") || filename.contains("519") || filename.contains("520") || filename.contains("525") || filename.contains("526") || filename.contains("534") || filename.contains("550") || filename.contains("556") || filename.contains("557") || filename.contains("661") || filename.contains("690") || filename.contains("694") || filename.contains("720") || filename.contains("726") || filename.contains("730") || filename.contains("731") || filename.contains("737") || filename.contains("747") || filename.contains("749") || filename.contains("751") || filename.contains("754") || filename.contains("755") || filename.contains("774") || filename.contains("782") || filename.contains("788") || filename.contains("792") || filename.contains("813") || filename.contains("818") || filename.contains("861") || filename.contains("983") || filename.contains("991") || filename.contains("994")) {
                        writer.print("3"); // Perceptron
                        percep++;
                    } else if (filename.contains("1003") || filename.contains("1013") || filename.contains("1014") || filename.contains("1028") || filename.contains("1062") || filename.contains("1063") || filename.contains("1070") || filename.contains("1073") || filename.contains("1074") || filename.contains("1090") || filename.contains("1092") || filename.contains("1095") || filename.contains("1420") || filename.contains("1438") || filename.contains("1441") || filename.contains("1446") || filename.contains("1448") || filename.contains("1451") || filename.contains("1460") || filename.contains("1470") || filename.contains("1499") || filename.contains("1508") || filename.contains("1528") || filename.contains("1532") || filename.contains("1539") || filename.contains("1544") || filename.contains("1549") || filename.contains("1551") || filename.contains("1553") || filename.contains("1564") || filename.contains("190") || filename.contains("202") || filename.contains("210") || filename.contains("214") || filename.contains("276") || filename.contains("335") || filename.contains("342") || filename.contains("409") || filename.contains("419") || filename.contains("437") || filename.contains("443") || filename.contains("444") || filename.contains("450") || filename.contains("451") || filename.contains("455") || filename.contains("463") || filename.contains("471") || filename.contains("472") || filename.contains("497") || filename.contains("515") || filename.contains("531") || filename.contains("533") || filename.contains("540") || filename.contains("541") || filename.contains("549") || filename.contains("553") || filename.contains("555") || filename.contains("559") || filename.contains("561") || filename.contains("565") || filename.contains("569") || filename.contains("575") || filename.contains("665") || filename.contains("679") || filename.contains("685") || filename.contains("689") || filename.contains("691") || filename.contains("716") || filename.contains("721") || filename.contains("724") || filename.contains("729") || filename.contains("733") || filename.contains("736") || filename.contains("738") || filename.contains("757") || filename.contains("759") || filename.contains("767") || filename.contains("769") || filename.contains("770") || filename.contains("773") || filename.contains("777") || filename.contains("779") || filename.contains("785") || filename.contains("787") || filename.contains("798") || filename.contains("801") || filename.contains("814") || filename.contains("848") || filename.contains("860") || filename.contains("984") || filename.contains("992")) {
                        writer.print("4"); // Decision Tree
                        decs++;
                    } else if (filename.contains("1010") || filename.contains("1015") || filename.contains("1026") || filename.contains("1047") || filename.contains("1065") || filename.contains("1066") || filename.contains("1068") || filename.contains("1076") || filename.contains("1097") || filename.contains("1167") || filename.contains("1245") || filename.contains("1412") || filename.contains("1419") || filename.contains("1452") || filename.contains("1462") || filename.contains("1488") || filename.contains("1506") || filename.contains("1520") || filename.contains("1533") || filename.contains("1534") || filename.contains("1565") || filename.contains("171") || filename.contains("195") || filename.contains("203") || filename.contains("205") || filename.contains("207") || filename.contains("229") || filename.contains("338") || filename.contains("340") || filename.contains("406") || filename.contains("426") || filename.contains("427") || filename.contains("448") || filename.contains("449") || filename.contains("457") || filename.contains("460") || filename.contains("464") || filename.contains("469") || filename.contains("490") || filename.contains("523") || filename.contains("530") || filename.contains("659") || filename.contains("687") || filename.contains("696") || filename.contains("706") || filename.contains("712") || filename.contains("744") || filename.contains("764") || filename.contains("765") || filename.contains("780") || filename.contains("804") || filename.contains("808") || filename.contains("815") || filename.contains("829") || filename.contains("835") || filename.contains("836") || filename.contains("841") || filename.contains("842") || filename.contains("972") || filename.contains("975") || filename.contains("986")) {
                        writer.print("5"); // kNN
                        knn++;
                    } else if (filename.contains("1008") || filename.contains("1009") || filename.contains("1012") || filename.contains("1027") || filename.contains("1035") || filename.contains("1049") || filename.contains("1064") || filename.contains("1067") || filename.contains("1098") || filename.contains("1099") || filename.contains("1443") || filename.contains("1444") || filename.contains("1454") || filename.contains("1456") || filename.contains("1464") || filename.contains("1523") || filename.contains("1525") || filename.contains("1526") || filename.contains("1531") || filename.contains("1535") || filename.contains("1538") || filename.contains("1541") || filename.contains("1563") || filename.contains("1600") || filename.contains("173") || filename.contains("194") || filename.contains("199") || filename.contains("204") || filename.contains("217") || filename.contains("343") || filename.contains("412") || filename.contains("458") || filename.contains("462") || filename.contains("487") || filename.contains("491") || filename.contains("494") || filename.contains("498") || filename.contains("510") || filename.contains("524") || filename.contains("532") || filename.contains("544") || filename.contains("551") || filename.contains("566") || filename.contains("663") || filename.contains("675") || filename.contains("686") || filename.contains("703") || filename.contains("710") || filename.contains("714") || filename.contains("739") || filename.contains("745") || filename.contains("748") || filename.contains("756") || filename.contains("763") || filename.contains("772") || filename.contains("790") || filename.contains("795") || filename.contains("810") || filename.contains("828") || filename.contains("831") || filename.contains("858")) {
                        writer.print("6"); // Logistic Regression
                        regr++;
                    } else {
                        writer.print("7");
                        oth++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writer.print("\n");
                System.out.println("k = " + k + " / " + folder.length);
            }
            System.out.println("svm = " + svm + "\nrandom forest = " + forest + "\nperceptron = " + percep + "\ndecision tree = " + decs + "\nknn = " + knn + "\nlogistic regression = " + regr
                    + "\nother = " + oth);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void extractFromOne(String dataset, String name, boolean selection) {
        List<MetaFeatureExtractor> extractors = listMetaFeatures();
        try (PrintWriter writer = new PrintWriter(name, "UTF-8")) {
            createHeader(writer, extractors);

            File file = new File(dataset);
            try (Reader reader = new FileReader(file)) {
                createFeatures(reader, extractors, writer, selection);
                writer.print(",1");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void createHeader(PrintWriter writer, List<MetaFeatureExtractor> extractors) {
        writer.print("@relation meta.features.ascah\n");

        for (MetaFeatureExtractor extractor : extractors) {
            writer.print("@attribute " + extractor.getName().replace(" ", "") + " numeric\n");
        }
        writer.print("@attribute class {1, 2, 3, 4, 5, 6, 7}");
        writer.print("\n\n@data\n\n");
    }

    public static void createFeatures(Reader reader, List<MetaFeatureExtractor> extractors, PrintWriter writer, boolean selection) throws IOException {
        Instances instances = new Instances(reader);
        int i = 0;
        instances.setClassIndex(instances.numAttributes() - 1);
        for (MetaFeatureExtractor extractor : extractors) {
            try {
                //if (!selection || (i == 0) || (i == 3) || (i == 13) || (i == 14)) {
                    double value = extractor.extractValue(instances);
                    if (Double.isNaN(value)) {
                        writer.print("-1.0");
                    } else {
                        writer.print(String.format(Locale.ROOT, "%.6f", value));
                    }
                    writer.print(",");
                    i++;
                //}
            } catch (Exception e) {
                writer.printf(e.getMessage());
            }
        }
    }

    public static List<MetaFeatureExtractor> listMetaFeatures() {
        List<MetaFeatureExtractor> e = new ArrayList<>();

        e.add(new com.ifmo.recommendersystem.metafeatures.general.NumberOfInstances());
        e.add(new com.ifmo.recommendersystem.metafeatures.general.NumberOfFeatures());
        e.add(new com.ifmo.recommendersystem.metafeatures.general.NumberOfClasses());
        e.add(new com.ifmo.recommendersystem.metafeatures.general.DataSetDimensionality());
        e.add(new com.ifmo.recommendersystem.metafeatures.statistical.MeanLinearCorrelationCoefficient());
        e.add(new com.ifmo.recommendersystem.metafeatures.statistical.MeanSkewness());
        e.add(new com.ifmo.recommendersystem.metafeatures.statistical.MeanKurtosis());
        e.add(new com.ifmo.recommendersystem.metafeatures.informationtheoretic.NormalizedClassEntropy());
        e.add(new com.ifmo.recommendersystem.metafeatures.informationtheoretic.MeanNormalizedFeatureEntropy());
        e.add(new com.ifmo.recommendersystem.metafeatures.informationtheoretic.MeanMutualInformation());
        e.add(new com.ifmo.recommendersystem.metafeatures.informationtheoretic.MaxMutualInformation());
        e.add(new com.ifmo.recommendersystem.metafeatures.informationtheoretic.EquivalentNumberOfFeatures());
        e.add(new com.ifmo.recommendersystem.metafeatures.informationtheoretic.NoiseSignalRatio());

        e.add(new com.ifmo.recommendersystem.metafeatures.statistical.MeanStandardDeviation());
        e.add(new com.ifmo.recommendersystem.metafeatures.statistical.MeanCoefficientOfVariation());

        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeDevAttr());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeDevBranch());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeDevLevel());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeHeight());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeLeavesNumber());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeMaxAttr());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeMaxBranch());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeMaxLevel());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeMeanAttr());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeMeanBranch());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeMeanLevel());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeMinAttr());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeMinBranch());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeNodeNumber());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeWidth());

        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeDevClass());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeMaxClass());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeMinClass());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.pruned.PrunedTreeMeanClass());

        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeDevAttr());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeDevBranch());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeDevLevel());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeHeight());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeLeavesNumber());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeMaxAttr());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeMaxBranch());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeMaxLevel());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeMeanAttr());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeMeanBranch());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeMeanLevel());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeMinAttr());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeMinBranch());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeNodeNumber());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeWidth());

        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeDevClass());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeMaxClass());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeMinClass());
        e.add(new com.ifmo.recommendersystem.metafeatures.decisiontree.unpruned.UnprunedTreeMeanClass());

        return e;
    }
}
