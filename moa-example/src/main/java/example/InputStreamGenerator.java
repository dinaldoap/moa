package example;

import moa.core.TimingUtils;
import moa.options.ClassOption;
import moa.tasks.MainTask;
import moa.tasks.TaskThread;

public class InputStreamGenerator {
    public InputStreamGenerator() {
    }

    public void run() throws Exception {
        long evaluateStartTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
        String task =
                "WriteStreamToARFFFile -s (ConceptDriftStream -s (generators.SEAGenerator -f 3) -d (generators.SEAGenerator -f 2) -p 50000 -w 20000)"
                        + " -f data/gradual_concept_drift.arff -m 100000";
        System.out.println(task);
        MainTask currentTask = (MainTask) ClassOption.cliStringToObject(task, MainTask.class, null);
        TaskThread thread = new TaskThread((moa.tasks.Task) currentTask);
        thread.start();
        double time = TimingUtils
                .nanoTimeToSeconds(TimingUtils.getNanoCPUTimeOfCurrentThread() - evaluateStartTime);
        System.out.println(time + " seconds.");
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Generation started!");
        InputStreamGenerator generator = new InputStreamGenerator();
        generator.run();
    }
}
