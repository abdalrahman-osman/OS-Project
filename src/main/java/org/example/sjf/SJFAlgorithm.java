package org.example.sjf;

import org.example.Process;
import org.example.SchedulingAlgorithm;
import org.example.Statistics;

import java.util.ArrayList;
import java.util.List;

public class SJFAlgorithm extends SchedulingAlgorithm {
    private SJFScheduling scheduling;

    public SJFAlgorithm(List<Process> processes) {
        this.scheduling = new SJFScheduling(processes);
    }

    @Override
    public List<String> run(List<Process> processes) {
        List<String> result = new ArrayList<>();
        result.add("-- Starting SJF Algorithm --");
        Process nextProcess = scheduling.getNextProcess(0);
        while (nextProcess != null) {
            String s = String.format("\t\t[currentTime=%d] process %s is running", scheduling.getCurrentTime(), nextProcess.getId());
            result.add(s);
            scheduling.updateDuration(nextProcess);
            nextProcess = scheduling.getNextProcess(nextProcess.getId());
        }
        result.add("-- Ending SJF Algorithm --");
        return result;
    }

    @Override
    public void printStatistics() {
        Statistics[] arr = scheduling.getStatistics();
        System.out.println("--- SJF Statistics ---");
        for (Statistics ele:arr){
            System.out.println("\t\t" + ele.toString());
        }
        System.out.println("-- Ending SJF Statistics --");
    }
}
