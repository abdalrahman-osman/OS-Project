package org.example;

public interface SchedulingInterface {
    /**
     * @param currentProcessId
     * @return the next process to run on CPU according to the algorithm used
     */
    public Process getNextProcess (int currentProcessId);

    /**
     *
     * @param process
     * @return true if the process deleted successfully, otherwise false
     */
    public boolean deleteProcess (Process process);

    /**
     *
     * @param process
     * @apiNote This method update the process required time
     */
    public void updateDuration (Process process);

}
