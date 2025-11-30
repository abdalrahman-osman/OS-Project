package org.example;

import org.example.roundrobin.RoundRobinAlgorithm;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Generator generator = new Generator();

        ///  put your code here.
        ///  Naming Convention -> (Algorithm name)List, (Algorithm name)Result
        List<Process> roundRobinList = generator.getProcessList();
        SchedulingAlgorithm schedulingAlgorithm = new RoundRobinAlgorithm(roundRobinList);
        List<String> RoundRobinResult = schedulingAlgorithm.run(roundRobinList);

        ///  Print Result here
        schedulingAlgorithm.print(RoundRobinResult);
        schedulingAlgorithm.printStatistics();
    }
}