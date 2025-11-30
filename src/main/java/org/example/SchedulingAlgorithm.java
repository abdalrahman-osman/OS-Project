package org.example;

import java.util.List;

public abstract class SchedulingAlgorithm {
    public void print (List<String> results){
        for (String result:results){
            System.out.println(result);
        }
    }

    public abstract List<String> run (List<Process> processes);
    public abstract void printStatistics();
}
