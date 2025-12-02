package org.example.roundrobin;

import org.example.Process;
import org.example.SchedulingAlgorithm;
import org.example.Statistics;

import java.util.ArrayList;
import java.util.List;

public class RoundRobinAlgorithm extends SchedulingAlgorithm {
    RoundRobinScheduling roundRobinScheduling;
    public RoundRobinAlgorithm(List<Process> list) {this.roundRobinScheduling = new RoundRobinScheduling(list);}

    @Override
    public List<String> run(List<Process> list) {
        List<String> result = new ArrayList<>();
        result.add("-- Starting Round Robin Algorithm --");
        Process process = roundRobinScheduling.getNextProcess(0);
        while(process!=null){
            String outputLine = String.format("\t\t" + "[current time = %d] process %d is running",
                    roundRobinScheduling.getCurrentTime(),
                    process.getId());
            result.add(outputLine);
            roundRobinScheduling.updateDuration(process);
            process = roundRobinScheduling.getNextProcess(0);
            if (process == null) {result.add("-- Ending Round Robin Algorithm --");break;}
        }
        return result;
    }

    public void printStatistics(){
        Statistics[] arr = roundRobinScheduling.getStatistics();
        System.out.println("--- Round Robin Statistics ---");
        for (Statistics ele:arr){
            System.out.println("\t\t" + ele.toString());
        }
        System.out.println("-- Ending Round Robin Statistics --");
    }
}
