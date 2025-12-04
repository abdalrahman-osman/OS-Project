package org.example.MultiLevelFeedback;


import org.example.Process;
import org.example.SchedulingInterface;
import org.example.Statistics;

import java.util.LinkedList;
import java.util.List;


/***
 * MultilevelFeedback scheduling is similar to MultiLevel scheduling, but with minor changes.
 * Basically, it requires different queues as MultiLevel, each queue represents a different priority.
 * Processes are added to the first queue. 
 * If a process marked as lower priority, it is added to next low-level queue "Demotion". 
 * If a process marked as higher priority, it remains in its queue (or promoted to the next high-level queue) "Promotion".
 * 
 * Assume, we have 3 queues:-
 * - First is Round Robin  -> Priority_0 (Round = 2)
 * - Second is Round Robin -> Priority_1 (Round = 3)
 * - Third is FCFS         -> Priority_2
 * 
 * When a Demotion happens?
 * - If a process has a very long time (System Process) -> It is demoted to the next low-level
 * 
 * When a Promotion happens?
 * - If a process requires a user input (Interactive), so it may run for little time then yields, hince it will still top priority
 * - After certain predefined time (say RSEET_TIME = 10), all processes are promoted to the Queue1 again. 
 */

public class MultiLevelFeedbackScheduling implements SchedulingInterface {
    private final List<Process> processesList;
    private LinkedList<Process> Queue0; // Higher priority queue (Round Robin)
    private LinkedList<Process> Queue1; // Middle priority queue (Round Robin)
    private LinkedList<Process> Queue2; // Lower  priority queue (FCFS)
    private int time = 0;
    private Statistics[] statistics;
    private int countOfProcesses;
    private static final int ROUND_0 = 2;
    private static final int ROUND_1 = 3;
    private static final int Priority_0 = 0;
    private static final int Priority_1 = 1;
    private static final int Priority_2 = 2;
    public static final String INTERACTIVE = "interactive";

    // ----------------------------------------------------------------------------------
    // Constructor
    public MultiLevelFeedbackScheduling(List<Process> list) {
        // init variables
        this.processesList = list;
        this.countOfProcesses = list.size();
        statistics = new Statistics[countOfProcesses];
        Queue0 = new LinkedList<>();
        Queue1 = new LinkedList<>();
        Queue2 = new LinkedList<>();
        
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
        
        // All queues are empty
        if (Queue0.isEmpty() && Queue1.isEmpty() && Queue2.isEmpty()) {waitForNextProcess();}
        
        if (!Queue0.isEmpty()) {return Queue0.poll();} 
        if (!Queue1.isEmpty()) {return Queue1.poll();} 
        if (!Queue2.isEmpty()) {return Queue2.poll();} 
        
        return null;
    }

    private void enqueueProcessesUpToCurrentTime() {
        for (Process process : processesList) {
            if (
                process.getStartTime() <= time 
                && !Queue0.contains(process) 
                && !Queue1.contains(process) 
                && !Queue2.contains(process) 
                && !process.isFinished()
            ) {
                Queue0.add(process);
            }
        }
    }
    
    private void waitForNextProcess() {
        while (Queue0.isEmpty() && Queue1.isEmpty() && Queue2.isEmpty() && countOfProcesses > 0) {
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
        int processPriority = process.getPriority();

        if (processPriority == Priority_0) {
            // Took last round
            if (processDuration <= ROUND_0) {
                time += processDuration;
                deleteProcess(process);
                update_statistics(process, time);
            } 
            
            // Still need another time (Demote it)
            else {
                process.setDuration(processDuration - ROUND_0);
                time += ROUND_0;

                // if interactive (Requires I/O, will stay in the firstQueue) "Simulation"
                if (processType.equals(INTERACTIVE)) {
                    Queue0.add(process);
                } else {  // Else -> Demote it
                    Queue1.add(process);
                    process.setPriority(Priority_1);
                }
            }
        }

        else if (processPriority == Priority_1) {
            if (processDuration <= ROUND_1) {
                time += processDuration;
                deleteProcess(process);
                update_statistics(process, time);
            }

            // Still need time -> Demote it
            else {
                process.setDuration(processDuration - ROUND_1);
                time += ROUND_1;
                Queue2.add(process);
                process.setPriority(Priority_2);
            }
        }

        // Was in FCFS
        else {
            time += processDuration;
            deleteProcess(process);
            update_statistics(process, time);
        }
    }


    // If RESET_TIME has been passed -> Promote all processes in Queue1 & Queue2 to Queue0
    public void promoteAllProcesses() {
        for (Process process : processesList) {
            if (!process.isFinished()) {
                // Reset Priorities
                process.setPriority(Priority_0); 

                // Promote processes
                if (Queue1.contains(process)) {
                    Queue1.remove(process);
                    Queue0.add(process);
                } else if (Queue2.contains(process)) {
                    Queue2.remove(process);
                    Queue0.add(process);
                }
            }
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
