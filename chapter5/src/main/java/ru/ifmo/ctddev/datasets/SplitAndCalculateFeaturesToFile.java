package ru.ifmo.ctddev.datasets;

import ru.ifmo.ctddev.features.Feature;
import ru.ifmo.ctddev.features.FeatureMaker;
import ru.ifmo.ctddev.scheduling.ScheduleData;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by viacheslav on 01.05.2016.
 */
public class SplitAndCalculateFeaturesToFile {


    private static FeatureMaker featureMaker = new FeatureMaker();


    public static void main(String[] args) {

        dealWithFile("uniform8000.csv");
        dealWithFile("gaussian8000.csv");
        dealWithFile("taxi8000.csv");
    }

    private static void dealWithFile(String file) {
        try {
            int start = 0;
            int size = 50;

            while (start + size <= 8000) {
                String dataFilePath = "./src/main/resources/ml/data/" + file.split("\\.")[0] + "_" + start + "_" + (start + size);
                String featuresFilePath = "./src/main/resources/ml/features/" + file.split("\\.")[0] + "_" + start + "_" + (start + size);
                ScheduleData scheduleData = DatasetProvider.getDataset(
                        size, start, DatasetProvider.Direction.RIGHT,
                        file, dataFilePath + ".csv");

                BufferedWriter writer = new BufferedWriter(new FileWriter(
                        featuresFilePath + ".csv", false));

                writer.write(Feature.getHeadOfCSV());
                writer.newLine();

                featureMaker.getFeatures(scheduleData).stream().map(f -> f.toCSVString())
                        .forEach(f -> {
                            try {
                                writer.write(f);
                                writer.newLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                writer.flush();
                writer.close();

                start += size;
                System.out.println(dataFilePath + " - OK");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
