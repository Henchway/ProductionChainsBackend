package chains.utility;

import chains.materials.Food;
import chains.materials.Lifestock;
import chains.occupation.Work;
import chains.occupation.occupations.*;
import chains.worker.Worker;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.apache.logging.log4j.spi.CopyOnWrite;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

public class Generator {

    static Random random = new Random();
    static List<String> maleNames;
    static List<String> femaleNames;
    static List<Pair<Class<? extends Work>, Double>> workWeight = null;

    public static char randomGender() {

        if (random.nextInt(2) == 1) {
            return 'm';
        } else {
            return 'f';
        }

    }

    public static boolean randomBoolean() {

        return random.nextBoolean();

    }

    public static int randomAge() {

        return random.nextInt(10) + 15;

    }

    public static int randomHealthDegeneration() {

        return random.nextInt(4);

    }

    public static int randomHealth() {
        return random.nextInt(26);
    }

    public static void generateWorkWeight() {

        /**
         * The reflection library is used to get all classes which implement work
         * As this also includes interfaces and abstract classes,
         * these need to be removed later on
         */
        Reflections reflections = new Reflections("chains.occupation");
        Set<Class<? extends Work>> classes = reflections.getSubTypesOf(Work.class);
        classes.removeIf(clazz -> Modifier.isAbstract(clazz.getModifiers()) || clazz.isInterface());

        /**
         * In order to be able to weigh the different occupations, a Pair is being created for
         * each Class with it's respective weight. It's important that each weighed class has the
         * static getWeight method implemented, as well as the static weight variable
         * This variable can be changed to allow preference of certain occupations
         */
        final List<Pair<Class<? extends Work>, Double>> itemWeights = new ArrayList<>();
        classes.forEach(aClass -> {
            Double weight = 0.0;
            try {
                Method m = aClass.getMethod("getWeight");
                weight = (Double) m.invoke(null);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            itemWeights.add(new Pair<>(aClass, weight));

        });

        workWeight = itemWeights;

    }

    public static Work randomWork(Worker worker) {


        /**
         * With the weighed list available, the EnumeratedDistribution.sample() will select a weighed item
         * This can be useful when priorities need to be shifted due to low resources.
         */
        if (workWeight == null) {
            generateWorkWeight();
        }

        Class<? extends Work> clazz = new EnumeratedDistribution<>(workWeight).sample();
        Work work = null;

        try {
            Class<Worker>[] cArg = new Class[]{Worker.class};
            work = clazz.getDeclaredConstructor(cArg).newInstance(worker);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return work;

    }

    public static int nextInt(int bound) {

        Random random = new Random();
        return random.nextInt(bound);

    }

    public static String randomMaleFirstName() {

        if (maleNames == null) {
            maleNames = readFileIntoList("FirstNames_male.txt");
        }

        return maleNames.get(random.nextInt(maleNames.size()));

    }

    public static String randomFemaleFirstName() {

        if (femaleNames == null) {
            femaleNames = readFileIntoList("FirstNames_female.txt");
        }
        return femaleNames.get(random.nextInt(femaleNames.size()));

    }

    public static int randomFertility() {

        return random.nextInt(31);

    }

    /**
     * Determines whether a migration happens
     *
     * @return
     */

    public static int randomMigrationChance() {

        return random.nextInt(11);

    }

    /**
     * Determines how many settlers potentially migrate this year
     *
     * @return
     */
    public static int randomMigrationRate(int workerCount) {

        return random.nextInt((workerCount / 100) + 1) + 1;

    }


    public static int randomMaxChildCounter() {

        return random.nextInt(6);

    }

    public static <T> ConcurrentLinkedQueue<T> createConcurrentLinkedQueue(Class<T> clazz) {
        return new ConcurrentLinkedQueue<>();
    }

    public static <T extends Comparable> ConcurrentSkipListSet<T> createConcurrentSkipListSet(Class<T> clazz) {
        return new ConcurrentSkipListSet<>();
    }

    public static <T extends Comparable> TreeSet<T> createTreeSet(Class<T> clazz) {
        return new TreeSet<>();
    }


    public static List<String> readFileIntoList(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> lines = Collections.emptyList();
        try {
            ClassLoader classLoader = Generator.class.getClassLoader();
            File file = new File(Objects.requireNonNull(classLoader.getResource(fileName)).getFile());
            lines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Problem reading file " + fileName + " " + e.getMessage());
        }
        return lines;
    }

    public static Comparator<Lifestock> createLifestockComparator() {

        return Comparator.comparingInt(Lifestock::getAge);

    }

}
