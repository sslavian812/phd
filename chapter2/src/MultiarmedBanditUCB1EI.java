import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class MultiarmedBanditUCB1EI implements MultiarmedBandit {

    Function<Integer, Double> action;
    List<History> history;
    int N;
    double r[];
    int n[];

    public MultiarmedBanditUCB1EI(Function<Integer, Double> action, int num) {
        this.action = action;
        this.history = new ArrayList<>();
        this.N = num;
        r = new double [N];
        n = new int [N];
    }

    @Override
    public void init(double[] distribution) {
        System.out.println("\nSTART UCB1\n");
        for (int i = 0; i < N; i++) {
            r[i] = action.apply(i);
        }
        Arrays.fill(n, 1);
    }

    @Override
    public void iteration(int t) {
        System.out.println("Start iteration " + t + "\n");
        double curMax = r[0] + Math.sqrt(2 * Math.log(t) / n[0]);
        int curArm = 0;
        for (int i = 1; i < N; i++) {
            double val = r[i] + Math.sqrt(2 * Math.log(t) / n[i]);
            if (val > curMax) {
                curMax = val;
                curArm = i;
            }
        }
        System.out.println("\nArm " + curArm + " wins\n");
        n[curArm]++;
        double rewardt = action.apply(curArm);
        System.out.println("\nReward: " + rewardt + "\n");
        r[curArm] = rewardt;
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
        return "UCB1";
    }
}
