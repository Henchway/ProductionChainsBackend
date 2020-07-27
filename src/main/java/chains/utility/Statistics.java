package chains.utility;

import chains.materials.Lifestock;
import chains.materials.Resource;
import chains.materials.Warehouse;
import chains.timeline.GameTimeline;
import chains.worker.Worker;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.Collectors;


public class Statistics {

    private HashSet<Worker> workersStatisticsList;
    private long workersCount;
    private long femaleWorkersCount;
    private long maleWorkersCount;
    private long migratedWorkersCount;
    private long currentYear;
    private TreeMap<String, Integer> workMap;
    private final GameTimeline gameTimeline;
    private final Warehouse warehouse;
    TreeMap<String, Integer> resources;
    TreeMap<String, Integer> lifestock;
    private long locallyStoredResources;

    public Statistics(GameTimeline gameTimeline) {
        this.gameTimeline = gameTimeline;
        this.warehouse = this.gameTimeline.getWarehouse();
    }

    public void refreshStatistics() {
        generateWorkerStatistics();
        getWarehouseResources();
        getWarehouseLifestock();
    }


    public void generateWorkerStatistics() {

        workersStatisticsList = new HashSet<>(gameTimeline.getWorkersList());
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

//        List<ConcurrentLinkedQueue<Resource>> list = workersStatisticsList.stream()
//                .filter(Objects::nonNull)
//                .filter(Worker::hasWork)
//                .map(worker -> worker.getWork().getLocalResourceStorage().values())
//                .flatMap(Collection::stream)
//                .count();

        locallyStoredResources = workersStatisticsList.stream()
                .filter(Objects::nonNull)
                .filter(Worker::hasWork)
                .map(worker -> worker.getWork().getLocalResourceStorage().values())
                .mapToLong(Collection::size)
                .sum();


//        System.out.println(Collections.max(list));

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

        ConcurrentHashMap<Class<? extends Resource>, ConcurrentLinkedQueue<Resource>> resources = new ConcurrentHashMap<>(this.warehouse.getResourceStorage());

        this.resources = new TreeMap<>();
        resources
                .entrySet()
                .stream()
                .filter(Objects::nonNull)
                .forEach(classListEntry -> this.resources.put(classListEntry.getKey().getSimpleName(),
                        classListEntry.getValue().size()));

    }

    public void getWarehouseLifestock() {

        ConcurrentHashMap<Class<? extends Resource>, PriorityBlockingQueue<Lifestock>> lifestock = new ConcurrentHashMap<>(this.warehouse.getLifestockStorage());

        this.lifestock = new TreeMap<>();
        lifestock
                .entrySet()
                .stream()
                .filter(Objects::nonNull)
                .forEach(classListEntry -> this.lifestock.put(classListEntry.getKey().getSimpleName(),
                        classListEntry.getValue().size()));

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

    public long getLocallyStoredResources() {
        return locallyStoredResources;
    }

    public TreeMap<String, Integer> getWorkMap() {
        return workMap;
    }

    public Map<String, Integer> getResources() {
        return resources;
    }

    public TreeMap<String, Integer> getLifestock() {
        return lifestock;
    }
}
