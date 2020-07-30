package chains.materials;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Lifestock implements Resource, Comparable<Lifestock> {

    protected long id;
    protected int lifeExpectancy;
    protected int age;
    protected int meat;
    protected boolean readyForSlaughter;
    protected boolean isAlive = true;

    /**
     * Age all lifestock
     */
    public void age() {
        this.age++;
        if (age > lifeExpectancy / 3) {
            readyForSlaughter = true;
        }
        if (age > lifeExpectancy) {
            die();
        }
    }

    public void die() {
        isAlive = false;
//        System.out.println("A " + this.getClass().getSimpleName() + " has died at the age of " + age);
    }

    @Override
    public int compareTo(Lifestock o) {

        if (this.getAge() > o.getAge()) {
            return 1;
        } else if (o.getAge() > this.getAge()) {
            return -1;
        }

        return 0;
    }



}
