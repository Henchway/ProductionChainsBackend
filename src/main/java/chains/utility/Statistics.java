package chains.utility;

import chains.materials.Resource;
import chains.materials.Warehouse;
import chains.timeline.GameTimeline;
import chains.worker.Worker;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class Statistics {

    private CopyOnWriteArrayList<Worker> workersStatisticsList;
    private long workersCount;
    private long femaleWorkersCount;
    private long maleWorkersCount;
    private long migratedWorkersCount;
    private long currentYear;
    private TreeMap<String, Integer> workMap;
    private final GameTimeline gameTimeline;
    private final Warehouse warehouse;
    private HashMap<Class<? extends Resource>, Integer> resources;

    public Statistics(GameTimeline gameTimeline) {
        this.gameTimeline = gameTimeline;
        this.warehouse = this.gameTimeline.getWarehouse();
    }


    public void generateWorkerStatistics() {

        workersStatisticsList = new CopyOnWriteArrayList<>(gameTimeline.getWorkersList());
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
        generateWorkMap();

    }

    /**
     * Generates a map containing the work names and count of occurrences
     */
    public void generateWorkMap() {

        List<String> workList = workersStatisticsList.stream()
                .filter(Objects::nonNull)
                .filter(Worker::hasVocation)
                .map(worker -> worker.getWork().toString())
                .collect(Collectors.toList());

        workMap = new TreeMap<>();

        if (!workList.isEmpty()) {

            for (String s : workList) {

                if (workMap.containsKey(s)) {

                    workMap.put(s, workMap.get(s) + 1);

                } else {

                    workMap.put(s, 1);
                }

            }
        }
    }

    public void getWarehouseResources() {
        this.resources = this.warehouse.getResources();
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


    public HashMap<Class<? extends Resource>, Integer> getResources() {
        return resources;
    }

    public TreeMap<String, Integer> getWorkMap() {
        return workMap;
    }
}
