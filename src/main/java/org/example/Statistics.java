package org.example;

public class Statistics {

    private Process process;
    private int endingTime;
    private int waitingTime;
    private int originalTime;

    public Statistics() {
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public int getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(int endingTime) {
        this.endingTime = endingTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getOriginalTime() {
        return originalTime;
    }

    public void setOriginalTime(int originalTime) {
        this.originalTime = originalTime;
    }

    @Override
    public String toString() {
        String result = "Process ID: " + process.getId()
                + ", Ending Time: " + endingTime
                + ", Waiting Time: " + waitingTime
                + ", Original Time: " + originalTime;
        
        if (!process.getProcessType().equals("general")) {
            result += ", Process Type: " + process.getProcessType();
        }
        
        return result;
    }
}
