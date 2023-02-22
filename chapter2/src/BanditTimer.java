
public class BanditTimer {

    long small;
    long big;

    public BanditTimer(long small, long big) {
        this.small = small;
        this.big = big;
    }

    public long getTimelimit() {
        return small;
    }

    public long[] getAllTimelimit(double[] p) {
        double max = Double.MIN_VALUE;
        long[] time = new long [p.length];
        for (int i = 0; i < p.length; i++) {
            max = Math.max(max, p[i]);
        }
        long all = big;
        for (int i = 0; i < p.length - 1; i++) {
            time[i] = (int) (p[i] / max);
            all -= time[i];
        }
        time[p.length - 1] = all;
        return time;
    }
}
