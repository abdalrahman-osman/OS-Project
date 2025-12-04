package org.example.MultiLevel;


import org.example.Process;
import org.example.SchedulingInterface;
import org.example.Statistics;

import java.util.LinkedList;
import java.util.List;


/***
 * Multilevel scheduling uses different queues for different process categories
 * For ex, an interactive process (with user) has a different queue for a system process
 * In this simulation:
 * We will use two categories (Interactive & System)
 * >> Round robin algorithm will be used for the interactive processes
 * >> FSFC algorithm will be used for the system processes
 * >> Interactive queue has a higher priority than system queue
 */

public class MultiLevelScheduling implements SchedulingInterface {
    private final List<Process> processesList;
    private LinkedList<Process> interactiveQueue;
    private LinkedList<Process> systemQueue;
    private int time = 0;
    private Statistics[] statistics;
    private int countOfProcesses;
    private static final int ROUND = 2;
    public static final String SYSTEM = "system";
    public static final String INTERACTIVE = "interactive";

    // ----------------------------------------------------------------------------------
    // Constructor
    public MultiLevelScheduling(List<Process> list) {
        // init variables
        this.processesList = list;
        this.countOfProcesses = list.size();
        statistics = new Statistics[countOfProcesses];
        interactiveQueue = new LinkedList<>();
        systemQueue = new LinkedList<>();
        
        // init statistics
        for (int i = 0; i < countOfProcesses; i++) {
            statistics[i] = new Statistics();
            statistics[i].setOriginalTime(list.get(i).getProcessTime());
        }
    }
    // ----------------------------------------------------------------------------------
    // Get Next Process
    
    @Override
    public Process getNextProcess(int currentProcessId) {
        enqueueProcessesUpToCurrentTime();
        if (countOfProcesses <= 0) return null;
        
        // Both queues are empty
        if (interactiveQueue.isEmpty() && systemQueue.isEmpty()) {waitForNextProcess();}
        
        if (!interactiveQueue.isEmpty()) {return interactiveQueue.poll();} 
        if (!systemQueue.isEmpty()) {return systemQueue.poll();}
        
        return null;
    }

    private void enqueueProcessesUpToCurrentTime() {
        for (Process process : processesList) {
            if (
                process.getStartTime() <= time 
                && !interactiveQueue.contains(process) 
                && !systemQueue.contains(process) 
                && !process.isFinished()
            ) {
                // add processes depending on their type
                if (process.getProcessType().equals(INTERACTIVE)) {
                    interactiveQueue.add(process);
                } else {
                    systemQueue.add(process);
                }
            }
        }
    }
    
    private void waitForNextProcess() {
        while (interactiveQueue.isEmpty() && systemQueue.isEmpty() && countOfProcesses > 0) {
            time++;
            enqueueProcessesUpToCurrentTime();
        }
    }

    // ----------------------------------------------------------------------------------

    // Deleting a Process
    @Override
    public boolean deleteProcess(Process process) {
        process.setFinished();
        countOfProcesses --;
        return true;
    }
    
    // ----------------------------------------------------------------------------------

    // Updating a Process Duration 
    @Override 
    public void updateDuration(Process process) {
        String processType = process.getProcessType();
        int processDuration = process.getProcessTime();

        // Roung robin running
        if (processType.equals(INTERACTIVE)) {
            // Take last round
            if (processDuration <= ROUND) {
                time += processDuration;
                deleteProcess(process);
                update_statistics(process, time);
            } 
            
            // Still need rounds
            else {
                process.setDuration(processDuration - ROUND);
                time += ROUND;
                interactiveQueue.add(process); // add it again for the next round
            }
            
        } 
        
        // FSFC
        else {
            time += processDuration;
            deleteProcess(process);
            update_statistics(process, time);
        }
    }

    private void update_statistics(Process process, int time) {
        statistics[process.getId()].setProcess(process);
        statistics[process.getId()].setEndingTime(time);
        statistics[process.getId()].setWaitingTime(time - statistics[process.getId()].getOriginalTime() - process.getStartTime());
    }

    // ----------------------------------------------------------------------------------------

    // Utility Functions
    public int getCurrentTime() {
        return time;
    }

    public Statistics[] getStatistics() {
        return statistics;
    }

}
