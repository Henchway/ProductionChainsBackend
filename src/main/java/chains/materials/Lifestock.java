package chains.materials;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class Lifestock implements Resource {

    @Id
    @GeneratedValue
    protected long id;
    protected int lifeExpectancy;
    protected int age;
    protected int meat;
    protected boolean readyForSlaughter;
    protected boolean isAlive = true;
    protected Class<?> clazz = this.getClass();


    /**
     * Age all lifestock
     */
    public void age() {
        this.age++;
        if (age > lifeExpectancy / 2) {
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

}
