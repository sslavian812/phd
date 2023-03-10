/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fantail.examples;

import fantail.algorithms.AbstractRanker;
import fantail.algorithms.*;
import fantail.core.MultiRunEvaluation;
import fantail.core.Tools;
import weka.core.Instances;

/**
 *
 * @author Quan Sun quan.sun.nz@gmail.com
 */
public class LabelRankingSingleAlgoExample01 {

    public static void main(String[] args) throws Exception {

        String arffPath = "/Users/Quan/Dropbox/Ranking_Datasets/LabelRankingSemiSyntheticData/glass_dense.csv.arff";
        Instances data = Tools.loadFantailARFFInstances(arffPath);

        int numRuns = 30;
        int randSeed = 1;
        double trainsetRatio = 0.50;

        System.out.println(arffPath);
        System.out.println("Num of labels: " + Tools.getNumberTargets(data));
        System.out.println("Num of instances: " + data.numInstances());
        System.out.println("Num of attributes (incl. target att): " + data.numAttributes());
        System.out.println();

        AbstractRanker ranker;
        MultiRunEvaluation eval;

        //
        String strFormat = "%-30s %-30s %-30s";
        System.out.println(String.format(strFormat, "<Algorithms>", "<Kendall>", "<SpearmanCC>"));
        //
        ranker = new LabelRankingTree();
        eval = new MultiRunEvaluation(data);
        eval.multiRunEvaluate(ranker, numRuns, trainsetRatio, randSeed);
        printResult(strFormat, ranker, eval);
        //
    }

    private static void printResult(String strFormat, AbstractRanker ranker, MultiRunEvaluation eval) {
        System.out.println(String.format(strFormat,
                ranker.rankerName(),
                roundDouble(eval.getScoreKendall(), 4) + "(" + roundDouble(eval.getScoreKendallStd(), 2) + ")",
                roundDouble(eval.getScoreSpearmanCC(), 4) + "(" + roundDouble(eval.getScoreSpearmanCCStd(), 2) + ")"));
    }

    private static double roundDouble(double n, int dPlaces) {
        return weka.core.Utils.roundDouble(n, dPlaces);
    }
}
