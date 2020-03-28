package utility;

import worker.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Statistics {

    private static ArrayList<Worker> workersStatisticsList = new ArrayList<>();
    private static long workersCount;
    private static long femaleWorkersCount;
    private static long maleWorkersCount;
    private static HashMap<String, Integer> vocationMap;

    public static void generateWorkerStatistic() {

        workersStatisticsList = new ArrayList<>(Worker.getWorkersList());
        workersCount = workersStatisticsList.size();
        femaleWorkersCount = workersStatisticsList.stream()
                .filter(worker -> worker.getGender() == 'f')
                .count();
        maleWorkersCount = workersCount - femaleWorkersCount;
        generateVocationMap();

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

    public static HashMap<String, Integer> getVocationMap() {
        return vocationMap;
    }

    public static void generateVocationMap() {

        List<String> vocationList = workersStatisticsList.stream()
                .filter(worker -> worker.hasVocation())
                .map(worker -> worker.getVocation().toString())
                .collect(Collectors.toList());

        vocationMap = new HashMap<>();

        if (!vocationList.isEmpty()) {

            for (String s : vocationList) {

                if (vocationMap.containsKey(s)) {

                    vocationMap.put(s, vocationMap.get(s) + 1);

                } else {

                    vocationMap.put(s, 1);
                }


            }
        }

    }

}
