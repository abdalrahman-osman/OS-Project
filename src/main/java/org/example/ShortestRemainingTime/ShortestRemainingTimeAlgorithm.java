package org.example.ShortestRemainingTime;

import org.example.Process;
import org.example.SchedulingAlgorithm;
import org.example.Statistics;

import java.util.ArrayList;
import java.util.List;

public class ShortestRemainingTimeAlgorithm extends SchedulingAlgorithm {

    private ShortestRemainingTimeScheduling scheduler;

    public ShortestRemainingTimeAlgorithm(List<Process> processList) {
        // store scheduler for later statistics printing
        this.scheduler = new ShortestRemainingTimeScheduling(processList);
    }

    @Override
    public List<String> run(List<Process> processList) {
        // if run is called with a different list, recreate scheduler
        if (scheduler == null || scheduler.getProcessList().size() != processList.size()) {
            this.scheduler = new ShortestRemainingTimeScheduling(processList);
        }

        List<String> timeline = new ArrayList<>();
        int total = processList.size();

        int lastRunningId = -1;
        timeline.add("-- Starting Shortest Remaining Time Algorithm -- ");
        while (scheduler.getStatistics().size() < total) {
            Process next = scheduler.getNextProcess(lastRunningId);
            if (next == null) {
                // no more processes
                break;
            }

            // capture scheduler time when the process is selected
            int fetchTime = scheduler.getCurrentTime();

            // only record when a new process starts running (context switch or first start)
            if (next.getId() != lastRunningId) {
                timeline.add("\t\t[currentTime=" + fetchTime + "] Process " + next.getId() + " is running");
            }

            // advance the simulation by one time unit for the selected process
            scheduler.updateDuration(next);
            lastRunningId = next.getId();
        }
        timeline.add("-- Ending Shortest Remaining Time Algorithm -- ");
        return timeline;
    }

    @Override
    public void printStatistics() {
        if (scheduler == null) {
            return;
        }
        List<Statistics> stats = scheduler.getStatistics();
        System.out.println("=== Shortest Remaining Time Statistics ===");
        for (Statistics s : stats) {
            System.out.println("\t\t" + s.toString());
        }
        System.out.println("=== End of Shortest Remaining Time Statistics ===");
    }

}
