package chains.utility;

import chains.occupation.Work;
import chains.occupation.occupations.*;
import chains.worker.Worker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Generator {

    static Random random = new Random();
    static List<String> maleNames;
    static List<String> femaleNames;


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

        return random.nextInt(10) + 1;

    }

    public static int randomHealthDegeneration() {

        return random.nextInt(4);

    }

    public static int randomHealth() {
        return random.nextInt(26);
    }


    public static Work randomWork(Worker worker) {

        int selection = random.nextInt(9);

        return switch (selection) {
            case 0 -> new Hunter(worker);
            case 1 -> new Lumberjack(worker);
            case 2 -> new Miner(worker);
            case 3 -> new Shepherd(worker);
            case 4 -> new Tanner(worker);
            case 5 -> new Farmer(worker);
            case 6 -> new Blacksmith(worker);
            case 7 -> new Stonemason(worker);
            case 8 -> new Butcher(worker);
            default -> null;

        };

    }

    public static int nextInt(int bound) {

        Random random = new Random();
        return random.nextInt(bound);

    }

    public static String randomMaleFirstName() {

        if (maleNames == null) {
            maleNames = readFileIntoList("FirstNames_male.txt");
        }

        return (String) maleNames.get(random.nextInt(maleNames.size()));

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

}
