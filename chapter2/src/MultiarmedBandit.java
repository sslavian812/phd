
public interface MultiarmedBandit {

    void init(double[] distribution);
    void iteration(int t);
    String getName();
    void iterate(int k);
}
