package worker;

import utility.Generator;
import vocation.Vocation;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread {


    // Program properties
    public final static int durationOfYear = 100;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    static ArrayList<Worker> workersList = new ArrayList<>();
    static int yearsPassed = 0;
    static Timer timer;
    private static ReentrantLock mutex = new ReentrantLock();

    // Worker properties
    int age;
    String name;
    char gender;
    boolean isAlive;
    Vocation vocation;
    boolean isAdult;
    boolean criticalAge;
    int health;
    Worker partner;
    int fertility;
    int childCounter;
    int maxChildCounter;
    ArrayList<Worker> children;


    /**
     * Worker gets born
     */
//    public Worker() {
//
//        this.age = 1;
//        this.gender = Generator.randomGender();
//        this.vocation = null;
//        this.isAlive = true;
//        this.isAdult = false;
//
//    }

    /**
     * Worker gets created artificially
     */

    public Worker() {

        this.age = 1;
        this.gender = Generator.randomGender();
        this.name = selectName(gender);
        this.health = 75 + Generator.randomHealth();
        this.vocation = null;
        this.isAdult = false;
        this.criticalAge = false;
        this.isAlive = true;
        this.fertility = 70 + Generator.randomFertility();
        this.childCounter = 0;
        this.maxChildCounter = maxChildren();
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

        this.children = new ArrayList<>();
        if (gender == 'f') {
            return maxChildCounter = Generator.randomMaxChildCounter();
        } else {
            return maxChildCounter = 0;
        }

    }

    Vocation chooseVocation() {

        Vocation vocation = Generator.randomVocation();
        System.out.println(this + " has taken up the vocation of " + vocation);
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

                if (!worker.hasPartner() && worker.isAlive
                        && this.getGender() != worker.getGender()
                        && worker.getAge() > 15
                        && (Math.abs(worker.age - this.age) < 20)) {

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

                assert age > 15;
                assert getPartner().getAge() > 15;

                Worker child = new Worker();
                childCounter++;

                mutex.lock();
                workersList.add(child);
                mutex.unlock();

                children.add(child);
                getPartner().children.add(child);
                child.start();
                System.out.println("A new child has been born: " + child);

            }
        }

    }


    void workerDies() {

        isAlive = false;
        System.out.println(ANSI_RED + this + " has died of old age in the year " + yearsPassed + ANSI_RESET);
        System.out.println(obituary());

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


    public int getAge() {
        return age;
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
        System.out.println(this + " has found a partner: " + partner);
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

    public static ArrayList<Worker> getWorkersList() {
        return workersList;
    }

    public static int getYearsPassed() {
        return yearsPassed;
    }


    public static void startPopulation() {

        int population = 200;

        for (int i = 0; i < population; i++) {

            Worker worker = new Worker();
            workersList.add(worker);
            worker.start();

        }

        startCalendar();

    }


}

