package org.example.MultiLevel;

import org.example.Process;
import org.example.SchedulingAlgorithm;
import org.example.Statistics;

import java.util.ArrayList;
import java.util.List;

public class MultiLevelAlgorithm extends SchedulingAlgorithm {

    MultiLevelScheduling multiLevelScheduling;
    public MultiLevelAlgorithm(List<Process> list) {
        this.multiLevelScheduling =  new MultiLevelScheduling(list);
    }

    @Override
    public List<String> run(List<Process> list) {
        List<String> result = new ArrayList<>();
        result.add("-- Starting Multi Level Algorithm --");

        Process process = multiLevelScheduling.getNextProcess(0);

        while(process != null) {
            String outputLine = String.format(
                "\t\t" + "[current time = %d] process %d is running, process type: %s",
                multiLevelScheduling.getCurrentTime(),
                process.getId(),
                process.getProcessType()
            );
            result.add(outputLine);

            multiLevelScheduling.updateDuration(process);
            process = multiLevelScheduling.getNextProcess(0);
            if (process == null) {
                result.add("-- Ending Multi Level Algorithm --");
                break;
            }
        }
        return result;
    }

    public void printStatistics(){
        Statistics[] arr = multiLevelScheduling.getStatistics();
        System.out.println("--- Multi Level Statistics ---");

        for (Statistics ele:arr){
            System.out.println("\t\t" + ele.toString());
        }
        System.out.println("-- Ending Multi Level Statistics --");
    }
}
