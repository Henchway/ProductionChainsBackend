package utility;

import vocation.*;
import worker.Worker;

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
    static List maleNames;
    static List femaleNames;


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


    public static Vocation randomVocation() {


        int selection = random.nextInt(5);
        Vocation vocation;

        switch (selection) {
            case 0:
                vocation = new Hunter();
                break;
            case 1:
                vocation = new Lumberjack();
                break;
            case 2:
                vocation = new Miner();
                break;
            case 3:
                vocation = new Shepherd();
                break;
            case 4:
                vocation = new Tanner();
                break;
            default:
                vocation = null;
                break;
        }

        return vocation;

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
        return (String) femaleNames.get(random.nextInt(femaleNames.size()));

    }

    public static int randomFertility() {

        return random.nextInt(31);

    }

    /**
     * Determines whether a migration happens
     * @return
     */

    public static int randomMigrationChance() {

        return random.nextInt(11);

    }

    /**
     * Determines how many settlers potentially migrate this year
     * @return
     */
    public static int randomMigrationRate() {

        if (Statistics.statistics == null) {
            Statistics.createStatistics();
        }

        return random.nextInt((int) Statistics.statistics.getWorkerCount() / 100) + 1;

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
