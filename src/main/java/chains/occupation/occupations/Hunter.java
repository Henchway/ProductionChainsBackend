package chains.occupation.occupations;

import chains.materials.Resource;
import chains.materials.raw.Hide;
import chains.materials.raw.Meat;
import chains.occupation.type.Labour;
import chains.utility.Generator;
import chains.worker.Worker;

import java.util.ArrayList;
import java.util.List;

public class Hunter extends Labour {
    private static double weight = 30.0;

    public Hunter(Worker worker) {
        super(worker);
    }

    @Override
    public void produce() {
        List<Resource> list = produceHide();
        warehouse.addResourcesOfSameTypeToWarehouse(produceMeat(list.size()));
        warehouse.addResourcesOfSameTypeToWarehouse(list);
    }

    public List<Resource> produceHide() {

        List<Resource> list = new ArrayList<>();
        int deer = Generator.nextInt(10) + 10 * efficiency;
        for (int i = 0; i < deer; i++) {
            list.add(new Hide());
        }
        return list;
    }

    public List<Resource> produceMeat(int deer) {

        List<Resource> list = new ArrayList<>();
        for (int i = 0; i < deer * 6; i++) {
            list.add(new Meat());
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
