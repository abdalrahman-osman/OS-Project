package org.example.roundrobin;

import org.example.Process;
import org.example.SchedulingInterface;
import org.example.Statistics;

import java.util.LinkedList;
import java.util.List;

public class RoundRobinScheduling implements SchedulingInterface {

    private static final int ROUND = 2;
    private final List<Process> list;
    private int countOfProcesses;
    private LinkedList<Process> queue = new LinkedList<>();
    private int time = 0;
    private Statistics[] statistics;

    public RoundRobinScheduling(List<Process> list) {
        this.list = list;
        this.countOfProcesses = list.size();
        statistics = new Statistics[countOfProcesses];
        for (int i = 0; i < countOfProcesses; i++) {
            statistics[i] = new Statistics();
            statistics[i].setOriginalTime(list.get(i).getProcessTime());
        }
    }

    @Override
    public Process getNextProcess(int currentProcessId) {
        enqueueProcessesUpToCurrentTime();
        if (countOfProcesses <= 0) return null;

        if (queue.isEmpty()) {
            waitForNextProcess();
        }

        return queue.isEmpty() ? null : queue.poll();
    }

    private void enqueueProcessesUpToCurrentTime() {
        for (Process process : list) {
            if (process.getStartTime() <= time && !queue.contains(process) && !process.isFinished()) {
                queue.add(process);
            }
        }
    }

    private void waitForNextProcess() {
        while (queue.isEmpty() && countOfProcesses > 0) {
            time++;
            enqueueProcessesUpToCurrentTime();
        }
    }

    @Override
    public boolean deleteProcess(Process process) {
        process.setFinished();
        countOfProcesses--;
        return true;
    }

    @Override
    public void updateDuration(Process process) {
        if (process.getProcessTime() <= ROUND) {
            time += process.getProcessTime();
            deleteProcess(process);
            updateStatistics(process);
        } else {
            time += ROUND;
            process.setDuration(process.getProcessTime() - ROUND);
            queue.remove(process);
        }
    }

    public void updateStatistics(Process process) {
        statistics[process.getId()].setProcess(process);
        statistics[process.getId()].setEndingTime(time);
        statistics[process.getId()].setWaitingTime
                ( time - process.getStartTime() - statistics[process.getId()].getOriginalTime());
    }

    public Statistics[] getStatistics() { return statistics; }

    public int getCurrentTime() { return time; }
}