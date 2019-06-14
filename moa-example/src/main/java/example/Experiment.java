package example;

import moa.core.TimingUtils;
import moa.options.ClassOption;
import moa.tasks.MainTask;
import moa.tasks.TaskThread;

public class Experiment {

    public Experiment() {
    }

    public void run(int numInstances, boolean isTesting) throws Exception {
        long evaluateStartTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
        String task = "EvaluatePrequential -l bayes.NaiveBayes"
                + " -s (ConceptDriftStream -s (generators.SEAGenerator -f 3) -d (generators.SEAGenerator -f 2) -p 50000 -w 20000)"
                + " -e (FadingFactorClassificationPerformanceEvaluator)"
                + " -i 100000 -f 1000 -d data/results.csv";
        System.out.println(task);
        MainTask currentTask = (MainTask) ClassOption.cliStringToObject(task, MainTask.class, null);
        TaskThread thread = new TaskThread((moa.tasks.Task) currentTask);
        thread.start();
        double time = TimingUtils
                .nanoTimeToSeconds(TimingUtils.getNanoCPUTimeOfCurrentThread() - evaluateStartTime);
        System.out.println(time + " seconds.");
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Experiment started!");
        Experiment exp = new Experiment();
        exp.run(1000000, true);
    }
}
