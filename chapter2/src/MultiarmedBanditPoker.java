import org.apache.commons.math.MathException;
import org.apache.commons.math.special.Erf;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

public class MultiarmedBanditPoker implements MultiarmedBandit {
    Function<Integer, Double> action;
    int K, T;
    Random rnd;
    int n[];
    double r[], r2[], mu[], si[];
    double eps = 0.00001;

    public MultiarmedBanditPoker(Function<Integer, Double> action, int num, int T) {
        this.action = action;
        this.K = num;
        this.T = T;
        rnd = new Random();
        n = new int[this.K];
        r = new double[this.K];
        r2 = new double[this.K];
        mu = new double[this.K];
        si = new double[this.K];
    }

    @Override
    public void init(double[] distribution) {
        Arrays.fill(n, 0);
        Arrays.fill(r, 0);
        Arrays.fill(r2, 0);
        Arrays.fill(mu, 0);
        for (int i = 0; i < K; i++) {
            int a = rnd.nextInt(K);
            //int a = i;
            double reward = action.apply(a);
            n[a]++;
            r[a] += reward;
            r2[a] += reward * reward;
            mu[a] = r[a] / n[a];
            si[a] = Math.sqrt(r2[a] / n[a] - mu[a] * mu[a]);
        }
        for (int i = 0; i < K; i++) {
            if (n[i] == 0) {
                int a = i;
                double reward = action.apply(a);
                n[a]++;
                r[a] += reward;
                r2[a] += reward * reward;
                mu[a] = r[a] / n[a];
                si[a] = Math.sqrt(r2[a] / n[a] - mu[a] * mu[a]);
            }
        }
    }

    private double normal(double x, double mu, double si) {
        return Math.exp(Math.pow(x - mu, 2) / (2.0 * si * si)) / Math.sqrt(2.0 * Math.PI * si);
    }

    private int argmax_mu() {
        double max = Double.MIN_VALUE;
        int maxInd = 0;
        for (int i = 0; i < K; i++) {
            if (mu[i] > max) {
                max = mu[i];
                maxInd = i;
            }
        }
        return maxInd;
    }

    private double erfi(double val, double sis, double mum) {
        val = (val - mum) / (Math.sqrt(2) * sis);
        return 2.0 * (-val - Math.pow(val, 3) / 3.0 - Math.pow(val, 5) / 10.0) / Math.sqrt(Math.PI);
    }

    private double integral(double dlim, double sis, double mum) {
        try {
            double d = Erf.erfc((dlim - mum) / Math.sqrt(2 * sis * sis));
            return 0.5 - d;
        } catch (MathException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // mu_i be the mean values associated to these reward distributions.
    @Override
    public void iteration(int t) {
        int q = 0;
        for (int i = 0; i < K; i++) {
            if (r[i] > 0) {
                q++;
            }
        }
        int i0 = argmax_mu();
        int i1 = 0;
        for (int j = 0; j < K; j++) {
            int sum = 0;
            for (int i = 0; i < K; i++) {
                if (mu[i] > mu[j]) {
                    sum++;
                }
            }
            if (sum - Math.sqrt(q) < eps) {
                i1 = j;
            }
        }
        double dem = (mu[i0] - mu[i1]) / Math.sqrt(q);
        double muz = argmax_mu();
        double pmax = Double.MIN_VALUE;
        int imax = 0;
        System.out.println("\n============");
        for (int i = 0; i < K; i++) {
            double mum, sis;
            if (n[i] > 0) {
                mum = mu[i];
            } else {
                mum = 0.0;
                int num = 0;
                for (int k = 0; k < K; k++) {
                    if (n[k] > 0) {
                        mum += mu[k];
                        num++;
                    }
                }
                mum /= num;
            }
            if (n[i] > 1) {
                sis = si[i];
            } else {
                sis = 0.0;
                int num = 0;
                for (int k = 0; k < K; k++) {
                    if (n[k] > 1) {
                        sis += si[k];
                        num++;
                    }
                }
                sis /= num;
            }
            double iii = integral(muz + dem, si[i] / Math.sqrt(n[i]), mu[i]);
            double p = mum + dem * (T - t) * iii;
            System.out.println("\np = " + p + "\n");
            if (p > pmax) {
                pmax = p;
                imax = i;
            }
        }
        System.out.println("\nimax = " + imax);
        double reward = action.apply(imax);
        n[imax]++;
        r[imax] += reward;
        r2[imax] += reward * reward;
        mu[imax] = r[imax] / n[imax];
        si[imax] = Math.sqrt(r2[imax] / n[imax] - mu[imax] * mu[imax]);
    }

    @Override
    public void iterate(int k) {
        for (int i = 1; i <= k; i++) {
            iteration(i);
        }
    }

    @Override
    public String getName() {
        return "poker";
    }
}
