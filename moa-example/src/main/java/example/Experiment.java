package example;

import java.io.IOException;

import com.yahoo.labs.samoa.instances.Instance;

import moa.classifiers.Classifier;
import moa.classifiers.trees.HoeffdingTree;
import moa.core.TimingUtils;
import moa.streams.generators.RandomRBFGenerator;

public class Experiment {

    public Experiment(){
    }

    public void run(int numInstances, boolean isTesting){
            Classifier learner = new HoeffdingTree();
            RandomRBFGenerator stream = new RandomRBFGenerator();
            stream.prepareForUse();

            learner.setModelContext(stream.getHeader());
            learner.prepareForUse();

            int numberSamplesCorrect = 0;
            int numberSamples = 0;
            long evaluateStartTime = TimingUtils.getNanoCPUTimeOfCurrentThread();
            while (stream.hasMoreInstances() && numberSamples < numInstances) {
                    Instance trainInst = stream.nextInstance().getData();
                    if (isTesting) {
                            if (learner.correctlyClassifies(trainInst)){
                                    numberSamplesCorrect++;
                            }
                    }
                    numberSamples++;
                    learner.trainOnInstance(trainInst);
            }
            double accuracy = 100.0 * (double) numberSamplesCorrect/ (double) numberSamples;
            double time = TimingUtils.nanoTimeToSeconds(TimingUtils.getNanoCPUTimeOfCurrentThread()- evaluateStartTime);
            System.out.println(numberSamples + " instances processed with " + accuracy + "% accuracy in "+time+" seconds.");
    }

    public static void main(String[] args) throws IOException {
            System.out.println("Experiment started!");
            Experiment exp = new Experiment();
            exp.run(1000000, true);
    }
}
