package worker;

import utility.Generator;
import vocation.Vocation;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread {


    // Program properties
    public static final int population = 200;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    private final static int durationOfYear = 100;
    private static ArrayList<Worker> workersList = new ArrayList<>();
    private static int yearsPassed = 0;
    private static Timer timer;
    private static ReentrantLock mutex = new ReentrantLock();

    // Worker properties
    private int age;
    private String name;
    private char gender;
    private boolean isAlive;
    private Vocation vocation;
    private boolean hasVocation;
    private boolean isAdult;
    private boolean criticalAge;
    private int health;
    private Worker partner;
    private int fertility;
    private int childCounter;
    private int maxChildCounter;
    private HashSet<Worker> children;
    private HashSet<Worker> parents;
    private HashSet<Worker> siblings;
    private boolean migrated;

    /**
     * Worker gets born
     */

    public Worker() {

        this.age = 1;
        this.gender = Generator.randomGender();
        this.name = selectName(gender);
        this.health = 75 + Generator.randomHealth();
        this.vocation = null;
        this.hasVocation = false;
        this.isAdult = false;
        this.criticalAge = false;
        this.isAlive = true;
        this.fertility = 70 + Generator.randomFertility();
        this.childCounter = 0;
        this.maxChildCounter = maxChildren();
        this.children = new HashSet<>();
        this.siblings = new HashSet<>();
        this.parents = new HashSet<>();
        this.migrated = false;
        checkAdulthood();

    }

    public Worker(int age) {

        this.age = age;
        this.gender = Generator.randomGender();
        this.name = selectName(gender);
        this.health = 75 + Generator.randomHealth();
        this.vocation = null;
        this.hasVocation = false;
        this.isAdult = false;
        this.criticalAge = false;
        this.isAlive = true;
        this.fertility = 70 + Generator.randomFertility();
        this.childCounter = 0;
        this.maxChildCounter = maxChildren();
        this.children = new HashSet<>();
        this.siblings = new HashSet<>();
        this.parents = new HashSet<>();
        this.migrated = true;
        checkAdulthood();

    }


    boolean checkAdulthood() {

        if (vocation == null && age >= 15) {
            vocation = chooseVocation();
            isAdult = true;
            return true;
        }

        return false;

    }

    String selectName(char gender) {

        if (gender == 'f') {
            return Generator.randomFemaleFirstName();
        } else {
            return Generator.randomMaleFirstName();
        }

    }

    int maxChildren() {

        if (gender == 'f') {
            return maxChildCounter = Generator.randomMaxChildCounter();
        } else {
            return maxChildCounter = 0;
        }

    }

    Vocation chooseVocation() {

        Vocation vocation = Generator.randomVocation();
        hasVocation = true;
//        System.out.println(this + " has taken up the vocation of " + vocation);
        return vocation;

    }

    void workerAges() {

        this.age++;

        if (age > 50) {
            criticalAge = true;
        }

    }


    void checkHealth() {

        if (age > 50) {
            health -= Generator.randomHealthDegeneration();
        }

        if (health < age) {
            workerDies();
        }


    }

    void findPartner() {

        if (age > 15 && partner == null
                && Generator.randomBoolean()
                && Generator.randomBoolean()) {

            mutex.lock();

            for (Worker worker : workersList) {

                if (!worker.hasPartner()
                        && worker.isAlive
                        && Math.abs(worker.getHealth() - worker.getAge()) > 10 // Partners on the brink of death won't be chosen
                        && this.getGender() != worker.getGender()
                        && worker.getAge() > 15
                        && (Math.abs(worker.age - this.age) < 20)
                        && !siblings.contains(worker)
                        && !parents.contains(worker)) {

                    setPartner(worker);
                    worker.setPartner(this);

                    break;

                }

            }

            mutex.unlock();

        }

    }

    void workerProcreates() {

        if (hasPartner()
                && gender == 'f'
                && childCounter < maxChildCounter) {

            if ((fertility + getPartner().fertility) / 2 > 80
                    && (age < 50 && getPartner().getAge() < 50)) {

                mutex.lock();
                Worker child = new Worker();
                childCounter++;
                workersList.add(child);

                children.add(child);
                if (partner != null) {
                    getPartner().children.add(child);
                }

                // Set parents of the children
                child.parents.add(this);
                child.parents.add(this.partner);

                mutex.unlock();

                // set the siblings
                if (children.size() > 1) {

                    mutex.lock();


                    Iterator iterator = children.iterator();

                    while(iterator.hasNext()) {

                        Worker sibling = (Worker) iterator.next();
                        HashSet<Worker> tempSiblings = new HashSet<>(children);
                        tempSiblings.remove(sibling);
                        sibling.siblings = tempSiblings;

                    }

                    mutex.unlock();
                }

                child.start();
//                System.out.println("A new child has been born: " + child);

            }
        }

    }

    public static void workerMigrates() {

        if (Generator.randomMigrationChance() >= 8) {

            Worker worker = new Worker(Generator.randomAge());

            mutex.lock();
            workersList.add(worker);
            mutex.unlock();
            worker.start();

        }


    }


    void workerDies() {

        isAlive = false;
//        System.out.println(ANSI_RED + this + " has died of old age in the year " + yearsPassed + ANSI_RESET);
//        System.out.println(obituary());

        if (hasPartner()) {
            partner.setPartner(null);
            partner = null;
        }
        mutex.lock();
        workersList.remove(this);
        mutex.unlock();

        if (workersList.isEmpty()) {
            timer.cancel();
        }

    }


    @Override
    public void run() {

        while (isAlive) {

            workerAges();
            checkAdulthood();
            findPartner();
            workerProcreates();
            checkHealth();

            try {
                Thread.sleep(durationOfYear);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    public boolean hasVocation() {
        return hasVocation;
    }

    public static ArrayList<Worker> getWorkersList() {
        return workersList;
    }

    public int getAge() {
        return age;
    }

    public int getHealth() {
        return health;
    }

    public char getGender() {
        return gender;
    }

    public Vocation getVocation() {
        return vocation;
    }

    public boolean hasPartner() {
        return partner != null;
    }

    public Worker getPartner() {
        return partner;
    }

    public static void startCalendar() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                yearsPassed++;
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(task, 0, durationOfYear);


    }


    public int getFertility() {
        return fertility;
    }

    public void setFertility(int fertility) {
        this.fertility = fertility;
    }


    public void setPartner(Worker partner) {
        this.partner = partner;
//        System.out.println(this + " has found a partner: " + partner);
    }


    @Override
    public String toString() {
        return name;
    }

    public String obituary() {
        return "\tWorker: " + "\t\t\t" + this + "\n" +
                "\t\tage =" + "\t\t\t" + age + "\n" +
                "\t\tgender =" + "\t\t" + gender + "\n" +
                "\t\tisAlive =" + "\t\t" + isAlive + "\n" +
                "\t\tvocation =" + "\t\t" + vocation + "\n" +
                "\t\tisAdult =" + "\t\t" + isAdult + "\n" +
                "\t\tcriticalAge =" + "\t" + criticalAge + "\n" +
                "\t\thealth =" + "\t\t" + health + "\n" +
                "\t\tchildren =" + "\t\t" + children + "\n" +
                "\t\tfertility =" + "\t\t" + fertility + "\n" +
                "\t\tpartner =" + "\t\t" + partner;
    }


    public static int getYearsPassed() {
        return yearsPassed;
    }

    public boolean isMigrated() {
        return migrated;
    }

    public static void startPopulation() {

        for (int i = 0; i < population; i++) {

            Worker worker = new Worker();
            mutex.lock();
            workersList.add(worker);
            mutex.unlock();
            worker.start();

        }

        startCalendar();

    }

}

