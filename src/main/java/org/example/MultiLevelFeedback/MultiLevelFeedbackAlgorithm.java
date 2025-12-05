package org.example.MultiLevelFeedback;

import org.example.Process;
import org.example.SchedulingAlgorithm;
import org.example.Statistics;

import java.util.ArrayList;
import java.util.List;

public class MultiLevelFeedbackAlgorithm extends SchedulingAlgorithm {
    private static final int RSEET_TIME = 10;
    private static final int PRIORITY_0 = 0;
    private static final int PRIORITY_1 = 1;
    private static final int PRIORITY_2 = 2;

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
            // If another process came while this is running
            // multiLevelFeedbackScheduling.enqueueProcessesUpToCurrentTime();

            int currentTime = multiLevelFeedbackScheduling.getCurrentTime();

            // Update Output
            int priorityMLF = process.getPriorityMLF();
            String queue = "";
            if (priorityMLF == PRIORITY_0) {queue = "Queue0";}
            else if (priorityMLF == PRIORITY_1) {queue = "Queue1";}
            else if (priorityMLF == PRIORITY_2) {queue = "Queue2";}

            String outputLine = String.format(
                "\t\t" + "[current time = %d] process %d is running, process type: %s, from queue: %s",
                // multiLevelFeedbackScheduling.getCurrentTime(),
                currentTime,
                process.getId(),
                process.getProcessType(),
                queue
            );

            result.add(outputLine);

            // Reset priorities each RESRT_TIME
            if (currentTime % RSEET_TIME == 0 && currentTime != 0) {
                multiLevelFeedbackScheduling.promoteAllProcesses();
                result.add("-- Resetting Priorities --");
            }

            // Updating Processes
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
