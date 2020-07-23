package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.raw.Tannin;
import chains.materials.raw.Wood;
import chains.occupation.type.Labour;
import chains.utility.Generator;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.List;

public class Lumberjack extends Labour {
    private static double weight = 10.0;

    public Lumberjack(Worker worker) {

        this.worker = worker;
        this.warehouse = worker.getGameTimeline().getWarehouse();
    }

    @Override
    public void produce() {
        List<Resource> lumber = produceLumber();
        warehouse.addResourcesOfSameTypeToWarehouse(lumber);
        warehouse.addResourcesOfSameTypeToWarehouse(produceTannin(lumber.size()));
    }

    public List<Resource> produceLumber() {

        List<Resource> list = new ArrayList<>();
        int wood = (Generator.nextInt(15) + 30) * efficiency;
        for (int i = 0; i < wood; i++) {
            list.add(new Wood());
        }
        return list;
    }

    public List<Resource> produceTannin(double amount) {

        List<Resource> list = new ArrayList<>();
        int tannin = (int) Math.ceil(amount/10) * efficiency;

        for (int i = 0; i < tannin; i++) {
            list.add(new Tannin());
        }
        return list;
    }


    @Override
    public void acquireTool() {

    }

    public static double getWeight() {
        return weight;
    }
}
