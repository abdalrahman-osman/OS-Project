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
        while (scheduler.getStatistics().size() < total) {
            Process next = scheduler.getNextProcess(lastRunningId);
            if (next == null) {
                // no more processes
                break;
            }
            // record which process runs at this time unit
            // scheduler.updateDuration will advance time by 1
            int beforeTime = scheduler.getCurrentTime();
            scheduler.updateDuration(next);
            timeline.add("Time " + beforeTime + " -> Process " + next.getId());
            lastRunningId = next.getId();
        }

        return timeline;
    }

    @Override
    public void printStatistics() {
        if (scheduler == null) {
            return;
        }
        List<Statistics> stats = scheduler.getStatistics();
        for (Statistics s : stats) {
            System.out.println(s.toString());
        }
    }

}
