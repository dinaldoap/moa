package example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import moa.core.TimingUtils;
import moa.options.ClassOption;
import moa.tasks.MainTask;
import moa.tasks.TaskThread;

public class Experiment {

    public void run() throws Exception {
        for (ClassifierEnum classifier : ClassifierEnum.values()) {
            for (String stream : InputStreamGenerator.STREAMS) {
                cleanUp(classifier.getName(), stream);
                long evaluateStartTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
                String task = String.format(
                        "EvaluatePrequential -l (%2$s)"
                                + " -s (ArffFileStream -f data/stream/%1$s.arff -c -1)"
                                + " -e (FadingFactorClassificationPerformanceEvaluator -r)"
                                + " -i 50000 -f 1 -d data/evaluation/%3$s/%1$s.csv",
                        stream, classifier.getCommand(), classifier.getName());
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
    }

    private void cleanUp(String classifier, String stream) throws IOException {
        Path path = Paths.get(String.format("data/evaluation/%s/%s.csv", classifier, stream));
        if (Files.exists(path)) {
            Files.delete(path);
        }
    }


    public static void main(String[] args) throws Exception {
        System.out.println("Experiment started!");
        Experiment exp = new Experiment();
        exp.run();
    }
}
