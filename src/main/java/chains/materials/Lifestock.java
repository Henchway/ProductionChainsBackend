package chains.materials;

import chains.timeline.GameTimeline;

public abstract class Lifestock implements Resource {

    protected int lifeExpectancy;
    protected int age;
    protected int meat;
    protected boolean readyForSlaughter;


    public int getLifeExpectancy() {
        return lifeExpectancy;
    }

    public void setLifeExpectancy(int lifeExpectancy) {
        this.lifeExpectancy = lifeExpectancy;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getMeat() {
        return meat;
    }

    public void setMeat(int meat) {
        this.meat = meat;
    }

    /**
     * Age all lifestock
     */
    public void age(GameTimeline gameTimeline) {
        this.age++;
        if (age > lifeExpectancy / 2) {
            readyForSlaughter = true;
        }
        if (age > lifeExpectancy) {
            die(gameTimeline);
        }


    }

    public void die(GameTimeline gameTimeline) {
        gameTimeline.getWarehouse().getResourcesToBeRemoved().offer(this);
//        System.out.println("A " + this.getClass().getSimpleName() + " has died at the age of " + age);
    }

    public boolean isReadyForSlaughter() {
        return readyForSlaughter;
    }
}
