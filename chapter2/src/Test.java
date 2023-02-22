import java.util.Random;
import java.util.function.Function;

public class Test implements Function<Integer, Double> {
    Random rnd;

    public Test() {
        this.rnd = new Random();
    }

    public Double apply(Integer arm) {
        return 100.0 * rnd.nextDouble();
    }

    public static void main(String[] args) {
        Test test = new Test();
        int iterAll = 50;
        MultiarmedBandit bandit = new MultiarmedBanditPoker(test, 6, iterAll);
        bandit.init(new double[7]);
        for (int iterNum = 2; iterNum <= iterAll; iterNum++) {
            bandit.iteration(iterNum);
        }
    }
}
