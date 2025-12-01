package org.example;

import org.example.roundrobin.RoundRobinAlgorithm;
import org.example.sjf.SJFAlgorithm;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Generator generator = new Generator();

        ///  put your code here.
        ///  Naming Convention -> (Algorithm name)List, (Algorithm name)Result
//        List<Process> roundRobinList = generator.getProcessList();
//        SchedulingAlgorithm schedulingAlgorithm = new RoundRobinAlgorithm(roundRobinList);
//        List<String> roundRobinResult = schedulingAlgorithm.run(roundRobinList);

        List<Process> sjfList = generator.getProcessList();
        SchedulingAlgorithm schedulingAlgorithm = new SJFAlgorithm(sjfList);
        List<String> sjfResult = schedulingAlgorithm.run(sjfList);

        ///  Print Result here
        schedulingAlgorithm.print(sjfResult);
        schedulingAlgorithm.printStatistics();
    }
}