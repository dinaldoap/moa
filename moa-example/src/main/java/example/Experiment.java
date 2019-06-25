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
        for (String stream : InputStreamGenerator.STREAMS) {
            cleanUp(stream);
            long evaluateStartTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
            String task = String.format("EvaluatePrequential -l bayes.NaiveBayes"
                    + " -s (ArffFileStream -f data/stream/%1$s.arff -c -1)"
                    + " -e (FadingFactorClassificationPerformanceEvaluator -r)"
                    + " -i 100000 -f 1 -d data/evaluation/%1$s.csv", stream);
            System.out.println(task);
            MainTask currentTask =
                    (MainTask) ClassOption.cliStringToObject(task, MainTask.class, null);
            TaskThread thread = new TaskThread((moa.tasks.Task) currentTask);
            thread.start();
            double time = TimingUtils.nanoTimeToSeconds(
                    TimingUtils.getNanoCPUTimeOfCurrentThread() - evaluateStartTime);
            System.out.println(time + " seconds.");
        }
    }

    private void cleanUp(String stream) {
        new File(String.format("data/evaluation/%s.csv", stream)).delete();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Experiment started!");
        Experiment exp = new Experiment();
        exp.run();
    }
}
