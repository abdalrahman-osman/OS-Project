package org.example.MultiLevelFeedback;

import org.example.Process;
import org.example.SchedulingAlgorithm;
import org.example.Statistics;

import java.util.ArrayList;
import java.util.List;

public class MultiLevelFeedbackAlgorithm extends SchedulingAlgorithm {
    private static final int RSEET_TIME = 10;

    MultiLevelFeedbackScheduling multiLevelFeedbackScheduling;
    public MultiLevelFeedbackAlgorithm(List<Process> list) {
        this.multiLevelFeedbackScheduling =  new MultiLevelFeedbackScheduling(list);
    }

    @Override
    public List<String> run(List<Process> list) {
        List<String> result = new ArrayList<>();
        result.add("-- Starting Multi Level Feedback Algorithm --");

        Process process = multiLevelFeedbackScheduling.getNextProcess(0);

        while(process != null) {
            int currentTime = multiLevelFeedbackScheduling.getCurrentTime();

            // Reset priorities each RESRT_TIME
            if (currentTime % RSEET_TIME == 0 && currentTime != 0) {
                multiLevelFeedbackScheduling.promoteAllProcesses();
            }

            String outputLine = String.format(
                "\t\t" + "[current time = %d] process %d is running, process type: %s",
                multiLevelFeedbackScheduling.getCurrentTime(),
                process.getId(),
                process.getProcessType()
            );
            result.add(outputLine);

            multiLevelFeedbackScheduling.updateDuration(process);
            process = multiLevelFeedbackScheduling.getNextProcess(0);
            if (process == null) {
                result.add("-- Ending Multi Level Feedback Algorithm --");
                break;
            }
        }
        return result;
    }

    public void printStatistics(){
        Statistics[] arr = multiLevelFeedbackScheduling.getStatistics();
        System.out.println("--- Multi Level Feedback Statistics ---");

        for (Statistics ele:arr){
            System.out.println("\t\t" + ele.toString());
        }
        System.out.println("-- Ending Multi Level Feedback Statistics --");
    }
}
