package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The main purpose of this Class is to generate a list of processes
 * AND
 * getProcessList creates a new arraylist that copies all values in the original arraylist
 */
public class Generator {
    private List<Process> list;
    private List<Process> generate (int num) {
        list = new ArrayList<>();
        Random random = new Random(124);
        for (int i = 0; i < num; i++) {
            Process process = new Process(random.nextInt(10) + 2, random.nextInt(5) + 1);
            list.add(process);
        }
        list.forEach(process -> System.out.println(process.toString()));
        return list;
    }
    public List<Process> getProcessList() {
        if (list == null) {
            list = generate(5);
        }
        return clone(list);
    }
    private List<Process> clone(List<Process> processList) {
        List<Process> cloneList = new ArrayList<>(processList);
        return  cloneList;
    }
}
