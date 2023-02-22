package ru.ifmo.ml;

import autoweka.Experiment;
import ca.ubc.cs.beta.smac.executors.SMACExecutor;
import com.google.common.util.concurrent.AtomicDouble;

public class SMACRunner extends Thread {
    public Experiment exp;
    public String seed;
    public AtomicDouble EI;
    private String[] smacargs;
    public RunnerSetter setter;

    public SMACRunner(Experiment exp, String seed, String[] smacargs) {
        this.exp = exp;
        this.smacargs = smacargs;
        this.seed = seed;
        EI = new AtomicDouble();
        EI.set(0.0);
        setter = new RunnerSetter();
    }

    public void run() {
        SMACExecutor.main(exp.callString.toArray(smacargs), setter);
    }

    public class RunnerSetter {

        public void set(double val) {
            EI.set(val);
        }
    }
}
