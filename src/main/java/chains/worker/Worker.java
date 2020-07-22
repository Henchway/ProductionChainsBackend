package chains.worker;

import chains.occupation.Work;
import chains.timeline.GameTimeline;
import chains.utility.Generator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Scope("prototype")
public class Worker {


    // Program properties
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    // Worker properties
    private int age;
    private final String name;
    private final char gender;
    private boolean isAlive;
    private Work work;
    private boolean hasWork;
    private boolean isAdult;
    private boolean criticalAge;
    private int health;
    private Worker partner;
    private int fertility;
    private int childCounter;
    private int maxChildCounter;
    private final CopyOnWriteArraySet<Worker> children;
    private final CopyOnWriteArraySet<Worker> parents;
    private CopyOnWriteArraySet<Worker> siblings;
    private final boolean migrated;
    private final GameTimeline gameTimeline;
    private boolean starving;

    /**
     * Worker gets born
     */


    public Worker(GameTimeline gameTimeline, @Value("false") boolean migrated) {

        this.gameTimeline = gameTimeline;
        this.gameTimeline.addWorkerToList(this);

        if (migrated) {
            this.age = Generator.randomAge();
        } else {
            this.age = 1;
        }

        this.gender = Generator.randomGender();
        this.name = selectName(gender);
        this.health = 75 + Generator.randomHealth();
        this.work = null;
        this.hasWork = false;
        this.isAdult = false;
        this.criticalAge = false;
        this.isAlive = true;
        this.fertility = 70 + Generator.randomFertility();
        this.childCounter = 0;
        this.maxChildCounter = maxChildren();
        this.children = new CopyOnWriteArraySet<>();
        this.parents = new CopyOnWriteArraySet<>();
        this.siblings = new CopyOnWriteArraySet<>();
        this.migrated = migrated;
        checkAdulthood();

    }

    public void checkAdulthood() {

        if (work == null && age >= 15) {
            work = chooseWork();
            work.setWorker(this);
            isAdult = true;
        }
    }

    public void lifecycle() {
        age();
        checkAdulthood();
        findPartner();
        procreate();
        work();
        eat();
        checkHealth();
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

    Work chooseWork() {

        Work work = Generator.randomWork(this);
        hasWork = true;
//        System.out.println(this + " has taken up the chains.vocation of " + chains.vocation);
        return work;

    }

    public void age() {

        this.age++;

        if (age > 50) {
            criticalAge = true;
        }

    }


    public void checkHealth() {

        if (age > 50) {
            health -= Generator.randomHealthDegeneration();
        }

        if (health < age) {
            die("Old age");
        }


    }

    public void findPartner() {

        if (isAdult && partner == null
                && Generator.randomBoolean()
                && Generator.randomBoolean()) {

            Worker match = gameTimeline.getWorkersList()
                    .stream()
                    .unordered()
                    .filter(worker -> !worker.hasPartner()
                            && worker.isAlive
                            && Math.abs(worker.getHealth() - worker.getAge()) > 10 // Partners on the brink of death won't be chosen
                            && this.getGender() != worker.getGender()
                            && worker.getAge() > 15
                            && (Math.abs(worker.age - this.age) < 20)
                            && !siblings.contains(worker)
                            && !parents.contains(worker)
                    )
                    .findAny()
                    .orElse(null);

            if (match != null) {
                setPartner(match);
                match.setPartner(this);
            }

        }

    }

    public void procreate() {

        if (hasPartner()
                && gender == 'f'
                && childCounter < maxChildCounter) {

            if ((fertility + getPartner().fertility) / 2 > 80
                    && (age < 50 && getPartner().getAge() < 50)) {

                Worker child = new Worker(gameTimeline, false);
                childCounter++;
                children.add(child);


                if (partner != null) {
                    getPartner().children.add(child);
                }

                // Set parents of the children
                child.parents.add(this);
                child.parents.add(this.partner);

                // set the siblings
                if (children.size() > 1) {

                    for (Worker sibling : children) {

                        CopyOnWriteArraySet<Worker> tempSiblings = new CopyOnWriteArraySet<>(children);
                        tempSiblings.remove(sibling);
                        sibling.siblings = tempSiblings;

                    }

                }

//                System.out.println("A new child has been born: " + child);

            }
        }

    }

    public void work() {

        if (this.hasWork) {
            this.work.setEfficiency();
            this.work.produce();
        }

    }

    public void eat() {

        int requiredEnergy = isAdult ? 50 : (age > 9 ? 20 : 10);
        boolean starvationImminent = gameTimeline.getWarehouse().retrieveFoodFromWarehouse(requiredEnergy);
        if (starvationImminent && starving) {
            die("Starvation");
        }
        starving = starvationImminent;

    }


    public void die(String reason) {

        isAlive = false;
//        System.out.println(ANSI_RED + this + " has died of " + reason + " in the year " + gameTimeline.getStatistics().getCurrentYear() + ANSI_RESET);
//        System.out.println(obituary());

        if (hasPartner()) {
            partner.setPartner(null);
            partner = null;
        }

        gameTimeline.removeWorkerFromList(this);

    }


    public boolean hasVocation() {
        return hasWork;
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

    public Work getWork() {
        return work;
    }

    public boolean hasPartner() {
        return partner != null;
    }

    public Worker getPartner() {
        return partner;
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
                "\t\tchains.vocation =" + "\t\t" + work + "\n" +
                "\t\tisAdult =" + "\t\t" + isAdult + "\n" +
                "\t\tcriticalAge =" + "\t" + criticalAge + "\n" +
                "\t\thealth =" + "\t\t" + health + "\n" +
                "\t\tchildren =" + "\t\t" + children + "\n" +
                "\t\tfertility =" + "\t\t" + fertility + "\n" +
                "\t\tpartner =" + "\t\t" + partner;
    }


    public boolean isMigrated() {
        return migrated;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public String getName() {
        return name;
    }

    public boolean hasWork() {
        return hasWork;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public boolean isCriticalAge() {
        return criticalAge;
    }

    public int getChildCounter() {
        return childCounter;
    }

    public int getMaxChildCounter() {
        return maxChildCounter;
    }

    public CopyOnWriteArraySet<Worker> getChildren() {
        return children;
    }

    public CopyOnWriteArraySet<Worker> getParents() {
        return parents;
    }

    public CopyOnWriteArraySet<Worker> getSiblings() {
        return siblings;
    }

    public GameTimeline getGameTimeline() {
        return gameTimeline;
    }
}

