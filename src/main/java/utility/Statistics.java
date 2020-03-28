package utility;

import worker.Worker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Statistics extends Thread {

    private ArrayList<Worker> workersStatisticsList = new ArrayList<>();
    private long workersCount;
    private long femaleWorkersCount;
    private long maleWorkersCount;
    private long migratedWorkersCount;
    private HashMap<String, Integer> vocationMap;

    private void generateWorkerStatistics() {

        workersStatisticsList = new ArrayList<>(Worker.getWorkersList());
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

    public long getFemaleWorkersCount() {
        return femaleWorkersCount;
    }

    public long getMaleWorkersCount() {
        return maleWorkersCount;
    }

    public long getMigratedWorkersCount() {
        return migratedWorkersCount;
    }

    public HashMap<String, Integer> getVocationMap() {
        return vocationMap;
    }

    public void generateVocationMap() {

        List<String> vocationList = workersStatisticsList.stream()
                .filter(Objects::nonNull)
                .filter(Worker::hasVocation)
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

    @Override
    public void run() {

        do {

            generateWorkerStatistics();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (workersCount > 0);


    }
}
