package example;

import java.io.File;
import moa.core.TimingUtils;
import moa.options.ClassOption;
import moa.tasks.MainTask;
import moa.tasks.TaskThread;

public class Experiment {

    public Experiment() {
    }

    public void run() throws Exception {
        cleanUp();
        long evaluateStartTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
        String task = "EvaluatePrequential -l bayes.NaiveBayes"
                + " -s (ArffFileStream -f data/gradual_concept_drift.arff -c -1)"
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

    private void cleanUp() {
        new File("data/results.csv").delete();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Experiment started!");
        Experiment exp = new Experiment();
        exp.run();
    }
}
