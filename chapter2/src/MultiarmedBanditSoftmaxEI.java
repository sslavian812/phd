import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class MultiarmedBanditSoftmaxEI implements MultiarmedBandit {

    Function<Integer, Double> action;
    List<History> history;
    int N;
    double r[];
    int n[];
    double tau; // 0.5?
    Random rnd;

    public MultiarmedBanditSoftmaxEI(Function<Integer, Double> action, int num, double tau) {
        this.action = action;
        this.history = new ArrayList<>();
        this.N = num;
        r = new double [N];
        n = new int [N];
        this.tau = tau;
        rnd = new Random(System.currentTimeMillis());
    }

    @Override
    public void init(double[] distribution) {
        System.out.println("\nSTART Softmax with tau = " + tau + "\n");
        for (int i = 0; i < N; i++) {
            r[i] = distribution[i]; //action.apply(i);
        }
        Arrays.fill(n, 1);
    }

    @Override
    public void iteration(int t) {
        System.out.println("Start iteration " + t + "\n");
        double del = 0;
        for (int i = 0; i < N; i++) {
            del += Math.exp(r[i] / tau);
        }
        double sum = 0.0;
        double val = rnd.nextDouble();
        int curArm = 0;
        for (int i = 1; i < N; i++) {
            sum += Math.exp(r[i] / tau) / del;
            if (val < sum) {
                curArm = i;
                break;
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
        return "softmax" + (int)(tau * 10);
    }

}
