package chains.materials.lifestock;

import chains.materials.Lifestock;
import chains.materials.raw.Hay;
import chains.utility.Generator;


public class Pig extends Lifestock {

    public Pig() {
        meat = 10 + Generator.nextInt(5);
        this.lifeExpectancy = 15 + Generator.nextInt(6);
        this.fodderAmount = 6;
        this.fodder = Hay.class;
    }
}
