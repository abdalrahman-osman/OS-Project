package org.example.ShortestRemainingTime;

import org.example.Process;
import org.example.SchedulingInterface;
import org.example.Statistics;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShortestRemainingTimeScheduling implements SchedulingInterface {

    private final List<Process> processList;
    private final Map<Integer, Integer> originalDurations = new HashMap<>();
    private final List<Statistics> statistics = new LinkedList<>();
    private int currentTime = 0;

    public ShortestRemainingTimeScheduling() {
        this.processList = new LinkedList<>();
    }

    public ShortestRemainingTimeScheduling(List<Process> processes) {
        // keep a local list reference so we can mark finished/remove
        this.processList = new LinkedList<>(processes);
        for (Process p : processes) {
            originalDurations.put(p.getId(), p.getProcessTime());
        }
    }

    @Override
    public Process getNextProcess(int currentProcessId) {
        // choose shortest remaining among arrived and not finished
        Process best = findShortestReady();
        if (best != null) {
            return best;
        }

        // if none ready, advance time to next arrival among unfinished processes
        int nextArrival = Integer.MAX_VALUE;
        for (Process p : processList) {
            if (!p.isFinished()) {
                nextArrival = Math.min(nextArrival, p.getStartTime());
            }
        }
        if (nextArrival == Integer.MAX_VALUE) {
            // no unfinished processes
            return null;
        }
        // advance time to the next arrival and pick again
        currentTime = Math.max(currentTime, nextArrival);
        return findShortestReady();
    }

    private Process findShortestReady() {
        Process best = null;
        int min = Integer.MAX_VALUE;
        for (Process p : processList) {
            if (p.isFinished()) {
                continue;
            }
            if (p.getStartTime() <= currentTime) {
                int rem = p.getProcessTime();
                if (rem < min || (rem == min && p.getId() < (best != null ? best.getId() : Integer.MAX_VALUE))) {
                    min = rem;
                    best = p;
                }
            }
        }
        return best;
    }

    @Override
    public boolean deleteProcess(Process process) {
        if (process == null) {
            return false;
        }
        process.setFinished();
        return processList.remove(process);
    }

    @Override
    public void updateDuration(Process process) {
        if (process == null) {
            return;
        }
        // run for one time unit
        int remaining = process.getProcessTime() - 1;
        process.setDuration(Math.max(0, remaining));
        currentTime++; // time passes

        if (process.getProcessTime() <= 0) {
            process.setFinished();
            Statistics s = new Statistics();
            s.setProcess(process);
            s.setEndingTime(currentTime);
            Integer original = originalDurations.get(process.getId());
            s.setOriginalTime(original != null ? original : 0);
            int waiting = s.getEndingTime() - process.getStartTime() - s.getOriginalTime();
            s.setWaitingTime(waiting);
            statistics.add(s);
            // keep or remove from list; mark finished already
            // optionally remove from active list to speed up searches:
            // processList.remove(process);
        }
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public List<Statistics> getStatistics() {
        return statistics;
    }

    public Map<Integer, Integer> getOriginalDurations() {
        return Collections.unmodifiableMap(originalDurations);
    }

    public List<Process> getProcessList() {
        return Collections.unmodifiableList(processList);
    }
}
