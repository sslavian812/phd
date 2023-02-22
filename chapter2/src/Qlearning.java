import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Qlearning implements MultiarmedBandit {

    Function<Integer, Double> action;
    Random rnd;
    double df, lf; // in (0, 1]
    double Q[];
    int N;
    int prevAct = 0;
    double prevReward;

    public Qlearning(Function<Integer, Double> action, int num, double lf, double df) {
        this.action = action;
        rnd = new Random(System.currentTimeMillis());
        this.df = df;
        this.lf = lf;
        this.N = num;
        Q = new double[N];
    }

    @Override
    public void init(double[] distribution) {
        System.out.println("\nSTART Q-learning with lf = " + lf + ", df = " + df + "\n");
        for (int i = 0; i < N; i++) {
            Q[i] = rnd.nextDouble();
        }
    }

    @Override
    public void iteration(int t) {
        System.out.println("Start iteration " + t + "\n");
        Q[prevAct] = Q[prevAct] + lf * (prevReward + df * maximum(Q) - Q[prevAct]);
        int a = argmax(Q);
        prevReward = action.apply(a);
        prevAct = a;
    }

    private double maximum(double Q[]) {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < N; i++) {
            if (Q[i] > max) {
                max = Q[i];
            }
        }
        return max;
    }

    private int argmax(double Q[]) {
        int amax = 0;
        for (int i = 0; i < N; i++) {
            if (Q[i] > Q[amax]) {
                amax = i;
            }
        }
        return  amax;
    }

    @Override
    public void iterate(int k) {
        for (int i = 1; i <= k; i++) {
            iteration(i);
        }
    }

    public String getName() {
        return "qlearning" + (int) (lf * 100) + "_" + (int)(df * 100);
    }
}
