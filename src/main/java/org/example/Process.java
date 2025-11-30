package org.example;

public class Process {
    private static int counter = 0;
    private int id;
    private int duration;
    private int startTime;
    private boolean finished;
    public Process(int time, int startTime) {
        id = counter++;
        this.duration = time;
        this.startTime = startTime;
        this.finished = false;
    }

    public int getProcessTime () {return this.duration;}
    public int getId () {return this.id;}
    public void setDuration (int duration) {this.duration = duration;}
    public boolean isFinished() {
        return finished;
    }
    public void setFinished () {this.finished = true;}
    public int getStartTime() {return this.startTime;}
    public String toString() {
        return "id = " + this.id + ", duration = " + this.duration + ", startTime = " + this.startTime;
    }
}
