package utility;

import worker.Worker;

import java.util.ArrayList;

public class Statistics {

    private static ArrayList<Worker> workersStatisticsList = new ArrayList<>();
    private static long workersCount;
    private static long femaleWorkersCount;
    private static long maleWorkersCount;

    public static void generateWorkerStatistic() {

        workersStatisticsList = new ArrayList<>(Worker.getWorkersList());
        workersCount = workersStatisticsList.size();
        femaleWorkersCount = workersStatisticsList.stream()
                .filter(worker -> worker.getGender() == 'f')
                .count();
        maleWorkersCount = workersCount - femaleWorkersCount;

    }

    public static long getWorkerCount() {
        return workersCount;
    }

    public static long getFemaleWorkersCount() {
        return femaleWorkersCount;
    }

    public static long getMaleWorkersCount() {
        return maleWorkersCount;
    }
}
