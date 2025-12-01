package org.example.sjf;

import org.example.Process;
import org.example.SchedulingInterface;
import org.example.Statistics;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class SJFScheduling implements SchedulingInterface {
    private int currentTime = 0;
    private int countOfProcesses = 0;
    private final Statistics[] statistics;
    private final PriorityQueue<Process> queue = new PriorityQueue<>(new Comparator<Process>() {
        @Override
        public int compare(Process o1, Process o2) {
            return Integer.compare(o1.getProcessTime(), o2.getProcessTime());
        }
    });
    private final List<Process> processList;

    public SJFScheduling(List<Process> processList) {
        this.processList = processList;
        this.countOfProcesses = processList.size();

        this.statistics = new Statistics[this.countOfProcesses];
        for (Process process : processList) {
            this.statistics[process.getId()] = new Statistics();
            this.statistics[process.getId()].setProcess(process);
            this.statistics[process.getId()].setOriginalTime(process.getProcessTime());
        }
    }

    public int getCurrentTime() {
        return currentTime;
    }

    private void enqueueProcessesUpToCurrentTime() {
        for (Process process : processList) {
            if (process.getStartTime() <= currentTime && !queue.contains(process) && !process.isFinished()) {
                queue.add(process);
            }
        }
    }

    @Override
    public Process getNextProcess(int currentProcessId) {
        if (this.countOfProcesses == 0) return null;

        enqueueProcessesUpToCurrentTime();

        while (queue.isEmpty()) {
            this.currentTime++;
            enqueueProcessesUpToCurrentTime();
        }

        return queue.poll();
    }

    @Override
    public boolean deleteProcess(Process process) {
        process.setFinished();
        this.countOfProcesses--;
        return true;
    }

    @Override
    public void updateDuration(Process process) {
        this.currentTime += process.getProcessTime();
        Statistics statistics = this.statistics[process.getId()];
        statistics.setEndingTime(this.currentTime);
        statistics.setWaitingTime(this.currentTime - process.getStartTime() - statistics.getOriginalTime());
        deleteProcess(process);
    }

    public List<Process> getProcesses() {
        return this.processList;
    }

    public Statistics[] getStatistics() {
        return this.statistics;
    }
}
