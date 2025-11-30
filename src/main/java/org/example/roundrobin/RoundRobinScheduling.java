package org.example.roundrobin;

import org.example.Process;
import org.example.SchedulingInterface;

import java.util.LinkedList;
import java.util.List;

public class RoundRobinScheduling implements SchedulingInterface {

    private static final int ROUND = 2;
    private final List<Process> list;
    private int COUNT_OF_PROCESSES;
    private LinkedList<Process> queue = new LinkedList<>();
    private int time = 0;
    private RoundRobinProcessClass[] roundRobinProcessClass;

    public RoundRobinScheduling(List<Process> list) {
        this.list = list;
        this.COUNT_OF_PROCESSES = list.size();
        roundRobinProcessClass = new RoundRobinProcessClass[COUNT_OF_PROCESSES];
        for (int i = 0; i < COUNT_OF_PROCESSES; i++) {roundRobinProcessClass[i] = new RoundRobinProcessClass();roundRobinProcessClass[i].setOriginalTime(list.get(i).getProcessTime());}
    }
    @Override
    public Process getNextProcess(int currentProcessId) {
        enqueueProcessesUpToCurrentTime();
        if (COUNT_OF_PROCESSES <= 0) return null;

        while (queue.isEmpty()) {
            time++;
            enqueueProcessesUpToCurrentTime();
        }

        if (!queue.isEmpty()) {
            Process nextProcess = queue.poll();
            return nextProcess;
        }
        return null;
    }
    private void enqueueProcessesUpToCurrentTime() {
        for (Process process : list) {
            if (process.getStartTime() <= time && !queue.contains(process) && !process.isFinished()) {
                queue.add(process);
            }
        }
    }
    @Override
    public boolean deleteProcess(Process process) {
        process.setFinished();
        COUNT_OF_PROCESSES--;
        return true;
    }
    @Override
    public void updateDuration(Process process) {
        if (process.getProcessTime() <= ROUND) {
            time += process.getProcessTime();
            deleteProcess(process);
            roundRobinProcessClass[process.getId()].setProcess(process);
            roundRobinProcessClass[process.getId()].setEndingTime(time);
            roundRobinProcessClass[process.getId()].setWaitingTime( time - process.getStartTime() - roundRobinProcessClass[process.getId()].getOriginalTime());
        } else {
            time += ROUND;
            int newTime = process.getProcessTime() - ROUND;
            process.setDuration(newTime);
            queue.remove(process);
        }
    }
    public RoundRobinProcessClass[] getStatistics() { return roundRobinProcessClass; }
}