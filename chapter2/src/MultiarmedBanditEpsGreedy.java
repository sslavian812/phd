import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class MultiarmedBanditEpsGreedy implements MultiarmedBandit {

    Function<Integer, Double> action;
    List<History> history;
    int N;
    double r[];
    int n[];
    double curMax = -Double.MAX_VALUE;
    int maxArm = 0;
    double eps; // 0.2, 0.4, 0.6
    Random rnd;

    public MultiarmedBanditEpsGreedy(Function<Integer, Double> action, int num, double eps) {
        this.action = action;
        this.history = new ArrayList<>();
        this.N = num;
        r = new double [N];
        n = new int [N];
        this.eps = eps;
        rnd = new Random(System.currentTimeMillis());
    }

    @Override
    public void init(double[] distribution) {
        System.out.println("\nSTART eps-greedy\n");
        for (int i = 0; i < N; i++) {
            r[i] = action.apply(i);
            if (r[i] > curMax) {
                curMax = r[i];
                maxArm = i;
            }
        }
        Arrays.fill(n, 1);
    }

    @Override
    public void iteration(int t) {
        double p = rnd.nextDouble();
        int curArm;
        if (p < eps) {
            curArm = rnd.nextInt(N);
        } else {
            curArm = maxArm;
        }
        System.out.println("\nArm " + curArm + " wins\n");
        n[curArm]++;
        double rewardt = action.apply(curArm);
        System.out.println("\nReward: " + rewardt + "\n");
        r[curArm] += rewardt;
        double val = r[curArm] / n[curArm];
        if (val > curMax) {
            curMax = val;
            maxArm = curArm;
        }
        history.add(new History(rewardt, curArm));
    }

    @Override
    public void iterate(int k) {
        for (int i = 1; i <= k; i++) {
            iteration(i);
        }
    }

    @Override
    public String getName() {
        return "eps-greedy" + (int)(eps * 10);
    }
}
