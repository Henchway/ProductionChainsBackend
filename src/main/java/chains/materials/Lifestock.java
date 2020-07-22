package chains.materials;

import chains.Main;
import chains.db.LifestockDbController;
import chains.timeline.GameTimeline;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Lifestock implements Resource {

    @Id
    @GeneratedValue
    protected long id;
    protected int lifeExpectancy;
    protected int age;
    protected int meat;
    protected boolean readyForSlaughter;
    protected boolean isAlive = true;


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

    public boolean isReadyForSlaughter() {
        return readyForSlaughter;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
