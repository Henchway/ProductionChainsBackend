package chains.materials.lifestock;

import chains.materials.Lifestock;
import chains.utility.Generator;

import javax.persistence.Entity;

@Entity
public class Pig extends Lifestock {

    public Pig() {
        meat = 15 + Generator.nextInt(5);
        this.lifeExpectancy = 15 + Generator.nextInt(6);
    }
}
