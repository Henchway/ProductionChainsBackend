package chains.utility;

import chains.timeline.GameTimeline;
import chains.worker.Worker;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class Statistics {

    private CopyOnWriteArrayList<Worker> workersStatisticsList;
    private long workersCount;
    private long femaleWorkersCount;
    private long maleWorkersCount;
    private long migratedWorkersCount;
    private long currentYear;
    private TreeMap<String, Integer> vocationMap;

    public Statistics() {
    }


    public void generateWorkerStatistics() {

        workersStatisticsList = new CopyOnWriteArrayList<>(GameTimeline.workersList);
        currentYear = GameTimeline.getYearsPassed();
        workersCount = workersStatisticsList.size();
        femaleWorkersCount = workersStatisticsList.stream()
                .filter(Objects::nonNull)
                .filter(worker -> worker.getGender() == 'f')
                .count();

        migratedWorkersCount = workersStatisticsList.stream()
                .filter(Objects::nonNull)
                .filter(Worker::isMigrated)
                .count();

        maleWorkersCount = workersCount - femaleWorkersCount;
        generateVocationMap();

    }

    public long getWorkerCount() {
        return workersCount;
    }

    public long getFemaleWorkerCount() {
        return femaleWorkersCount;
    }

    public long getMaleWorkerCount() {
        return maleWorkersCount;
    }

    public long getMigratedWorkerCount() {
        return migratedWorkersCount;
    }

    public long getCurrentYear() {
        return currentYear;
    }

    public void generateVocationMap() {

        List<String> vocationList = workersStatisticsList.stream()
                .filter(Objects::nonNull)
                .filter(Worker::hasVocation)
                .map(worker -> worker.getVocation().toString())
                .collect(Collectors.toList());

        vocationMap = new TreeMap<>();

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
