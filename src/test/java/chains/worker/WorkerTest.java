package chains.worker;

import chains.materials.Lifestock;
import chains.materials.Warehouse;
import chains.materials.lifestock.Chicken;
import chains.timeline.GameTimeline;
import chains.utility.Generator;
import org.junit.Test;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.util.*;
import java.util.stream.Collectors;


class WorkerTest {

//    @Test
//    public void testPriorityQueue() {
//
//
//        PriorityQueue<Lifestock> priorityQueueChicken = new PriorityQueue<>(Comparator.comparingInt(Lifestock::getAge));
//        PriorityQueue<Integer> priorityQueueNumbers = new PriorityQueue<>();
//
//        for (int i = 0; i < 10; i++) {
//            Chicken chicken = new Chicken();
//            chicken.setAge(Generator.nextInt(8));
//            priorityQueueChicken.add(chicken);
//
//            priorityQueueNumbers.add(Generator.nextInt(8));
//
//        }
//
//        int size = priorityQueueChicken.size();
//        for (int i = 0; i < size; i++) {
//            System.out.println(priorityQueueChicken.poll().getAge());
//        }
//
//    }
//
//    @Test
//    public void testTreeSet() {
//
//        TreeSet<Lifestock> treeSet = new TreeSet<>(Comparator.comparingInt(Lifestock::getAge).reversed());
//
//        for (int i = 0; i < 10; i++) {
//
//            Chicken chicken = new Chicken();
//            chicken.setAge(Generator.nextInt(8));
//            treeSet.add(chicken);
//
//        }
//
//        List<Lifestock> list = treeSet.stream()
//                .takeWhile(lifestock -> lifestock.getAge() > 5)
//                .collect(Collectors.toList());
//
//        list.forEach(System.out::println);
//
//    }


}