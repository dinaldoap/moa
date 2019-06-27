package example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import moa.options.ClassOption;
import moa.tasks.MainTask;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

public class InputStreamGenerator {
    private static final String NO_DRIFT_BALANCED = "no_drift_balanced";
    private static final String GRADUAL_DRIFT = "gradual_drift";
    private static final String IMBALANCED = "imbalanced";
    private static final List<String> SYNTHETICS =
            Arrays.asList(NO_DRIFT_BALANCED, GRADUAL_DRIFT, IMBALANCED);
    private static final List<String> PROJECTS =
            Arrays.asList("pip", "scikit-learn", "jenkins", "ant", "mongo", "postgres");
    public static final List<String> STREAMS = union(SYNTHETICS, PROJECTS);

    public InputStreamGenerator() {
    }

    private static List<String> union(List<String> list1, List<String> list2) {
        List<String> union = new ArrayList<String>(list1.size() + list2.size());
        union.addAll(list1);
        union.addAll(list2);
        return union;
    }

    public void run() throws Exception {
        noDriftBalanced();
        gradualDrift();
        imbalanced();
        for (String synthetic : SYNTHETICS) {
            arff2Csv(synthetic);
        }
        for (String project : PROJECTS) {
            preprocess(project);
            realDrifts(project);
        }
    }


    private void preprocess(String project) throws IOException {
        System.out.println(String.format("%s's commit data preprocessing started!", project));
        RawDataReader reader = new RawDataReader();
        reader.setSource(project);

        PreprocessorWriter preprocessor = new PreprocessorWriter();
        preprocessor.setInput(reader);
        preprocessor.setTarget(project);
        preprocessor.write();
    }

    private void realDrifts(String project) throws IOException {
        System.out.println(String.format("%s's real drifts generation started!", project));
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(String.format("data/preprocessed/%s.csv", project)));
        weka.core.Instances data = loader.getDataSet();

        ArffSaver saver = new ArffSaver();
        saver.setInstances(data);
        saver.setFile(new File((String.format("data/stream/%s.arff", project))));
        saver.writeBatch();
    }

    private void noDriftBalanced() throws Exception {
        System.out.println(String.format("%s generation started!", NO_DRIFT_BALANCED));
        String task =
                String.format("WriteStreamToARFFFile -s (generators.SEAGenerator -f 3 -p 0 -b)"
                        + " -f data/stream/%s.arff -m 50000", NO_DRIFT_BALANCED);
        MainTask currentTask = (MainTask) ClassOption.cliStringToObject(task, MainTask.class, null);
        currentTask.doTask();
    }

    private void gradualDrift() throws Exception {
        System.out.println(String.format("%s generation started!", GRADUAL_DRIFT));
        String task = String.format(
                "WriteStreamToARFFFile -s (ConceptDriftStream -s (generators.SEAGenerator -f 3 -p 0 -b) -d (generators.SEAGenerator -f 2 -p 0 -b) -p 25000 -w 10000)"
                        + " -f data/stream/%s.arff -m 50000",
                GRADUAL_DRIFT);
        MainTask currentTask = (MainTask) ClassOption.cliStringToObject(task, MainTask.class, null);
        currentTask.doTask();
    }

    private void imbalanced() throws Exception {
        System.out.println(String.format("%s generation started!", IMBALANCED));
        String task = String.format("WriteStreamToARFFFile -s (generators.SEAGenerator -f 3 -p 0)"
                + " -f data/stream/%s.arff -m 50000", IMBALANCED);
        MainTask currentTask = (MainTask) ClassOption.cliStringToObject(task, MainTask.class, null);
        currentTask.doTask();
    }

    private void arff2Csv(String baseName) throws IOException {
        System.out.println(String.format("%s's arff2csv started!", baseName));
        ArffLoader loader = new ArffLoader();
        loader.setSource(new File(String.format("data/stream/%s.arff", baseName)));
        Instances data = loader.getDataSet();

        CSVSaver saver = new CSVSaver();
        saver.setInstances(data);
        saver.setFile(new File(String.format("data/preprocessed/%s.csv", baseName)));
        saver.writeBatch();
    }

    public static void main(String[] args) throws Exception {
        InputStreamGenerator generator = new InputStreamGenerator();
        generator.run();
    }
}
