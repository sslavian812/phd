package ru.ifmo.ctddev.features;

import ru.ifmo.ctddev.scheduling.ScheduleData;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static util.Util.*;

/**
 * This class extracts features from original, packed in {@link ScheduleData} object.
 * Completely stateless.
 * <p>
 * Created by viacheslav on 14.02.2016.
 * <p>
 * Features:
 * width = maxX-minX
 * height = maxY-minY
 * meanX
 * meanY
 * ...
 */
public class FeatureMaker {

    private Moments momentsCalculator;

    public FeatureMaker() {
        momentsCalculator = new Moments();
    }

    public List<Feature> getFeatures(ScheduleData scheduleData) {
        List<Feature> features = new ArrayList<>(50);

        features.addAll(getMomentsForAllXs(scheduleData));
        features.addAll(getMomentsForAllYs(scheduleData));

        features.addAll(getMomentsForSrcXs(scheduleData));
        features.addAll(getMomentsForSrcYs(scheduleData));

        features.addAll(getMomentsForDstXs(scheduleData));
        features.addAll(getMomentsForDstYs(scheduleData));

        features.addAll(getMomentsForEdgesLengths(scheduleData));


        features.add(getDstLength(scheduleData));
        features.add(getSrcLength(scheduleData));
        features.add(getLength(scheduleData));

        features.add(getRelativeSrcLength(scheduleData));
        features.add(getRelativeDstLength(scheduleData));

        features.add(new Feature(
                "n", scheduleData.getOrdersNum(), "amount of pairs in dataset"
        ));
        return features;
    }

    public List<Feature> getMomentsForAllXs(ScheduleData scheduleData) {
        List<Double> xs = Arrays.asList(scheduleData.getPoints()).stream()
                .map(p -> p.getX()).collect(Collectors.toList());
        return momentsCalculator.extractStatisticalFeatures(xs, "all_xs_");
    }

    public List<Feature> getMomentsForAllYs(ScheduleData scheduleData) {
        List<Double> xs = Arrays.asList(scheduleData.getPoints()).stream()
                .map(p -> p.getY()).collect(Collectors.toList());
        return momentsCalculator.extractStatisticalFeatures(xs, "all_ys_");
    }

    public List<Feature> getMomentsForSrcXs(ScheduleData scheduleData) {
        List<Double> xs = scheduleData.getSrcOrDstPoints(true).stream()
                .map(p -> p.getX()).collect(Collectors.toList());
        return momentsCalculator.extractStatisticalFeatures(xs, "src_xs_");
    }

    public List<Feature> getMomentsForSrcYs(ScheduleData scheduleData) {
        List<Double> xs = scheduleData.getSrcOrDstPoints(true).stream()
                .map(p -> p.getY()).collect(Collectors.toList());
        return momentsCalculator.extractStatisticalFeatures(xs, "src_ys_");
    }

    public List<Feature> getMomentsForDstXs(ScheduleData scheduleData) {
        List<Double> xs = scheduleData.getSrcOrDstPoints(true).stream()
                .map(p -> p.getX()).collect(Collectors.toList());
        return momentsCalculator.extractStatisticalFeatures(xs, "dst_xs_");
    }

    public List<Feature> getMomentsForDstYs(ScheduleData scheduleData) {
        List<Double> xs = scheduleData.getSrcOrDstPoints(false).stream()
                .map(p -> p.getY()).collect(Collectors.toList());
        return momentsCalculator.extractStatisticalFeatures(xs, "dst_ys_");
    }

    public List<Feature> getMomentsForEdgesLengths(ScheduleData scheduleData) {
        List<Double> xs = scheduleData.getAllSrcDstPairs().stream()
                .map(p -> decartDist(p.getKey(), p.getValue())).collect(Collectors.toList());
        return momentsCalculator.extractStatisticalFeatures(xs, "edges_");
    }

    public Feature getLength(ScheduleData scheduleData) {
        return new Feature("length", getCostByPoints(Arrays.asList(scheduleData.getPoints())),
                "length of path through all points");
    }

    public Feature getSrcLength(ScheduleData scheduleData) {
        return new Feature("src_length", getCostByPoints(scheduleData.getSrcOrDstPoints(true)),
                "length of path through src points only");
    }

    public Feature getDstLength(ScheduleData scheduleData) {
        return new Feature("dst_length", getCostByPoints(scheduleData.getSrcOrDstPoints(false)),
                "length of path through dst points only");
    }

    public Feature getRelativeSrcLength(ScheduleData scheduleData) {
        return new Feature("relative_src_length",
                getCostByPoints(scheduleData.getSrcOrDstPoints(true)) / getCostByPoints(Arrays.asList(scheduleData.getPoints())),
                "length of path through src points relative to he whole path");
    }

    public Feature getRelativeDstLength(ScheduleData scheduleData) {
        return new Feature("relative_dst_length",
                getCostByPoints(scheduleData.getSrcOrDstPoints(false)) / getCostByPoints(Arrays.asList(scheduleData.getPoints())),
                "length of path through dst points relative to he whole path");
    }


    private double getCostByPoints(List<Point2D.Double> route) {
        double acc = 0.0;
        for (int i = 1; i < route.size(); ++i) {
            acc += decartDist(route.get(i - 1), route.get(i));
        }
        return acc;
    }
}
