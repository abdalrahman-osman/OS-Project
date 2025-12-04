package org.example;


public class Process {
    
    private static int counter = 0;
    private int id;
    private int duration;
    private int startTime;
    private boolean finished;

    private String processType;  // For multilevel scheduling
    public static final String SYSTEM = "system";
    public static final String INTERACTIVE = "interactive";
 
    public Process(int time, int startTime) {
        this.id = counter++;
        this.duration = time;
        this.startTime = startTime;
        this.finished = false;
        this.processType = "general";
    }

    public Process(int time, int startTime, String type) {
        this(time, startTime);
        this.processType = type;
    }

    public int getProcessTime() {
        return this.duration;
    }

    public int getId() {
        return this.id;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void setFinished() {
        this.finished = true;
    }

    public int getStartTime() {
        return this.startTime;
    }

    public void setProcessType(String type) {
        this.processType = type;
    }

    public String getProcessType() {
        return this.processType;
    }

    public String toString() {
        String result = "id = " + this.id + ", duration = " + this.duration + ", startTime = " + this.startTime;
        if (!this.processType.equals("general")) {
            result += ", processType: " + this.processType;
        }

        return result;
    }
    
}
