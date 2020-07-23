package chains.materials.lifestock;

import chains.materials.Lifestock;
import chains.utility.Generator;


public class Cow extends Lifestock {

    public Cow() {
        this.meat = 20 + Generator.nextInt(10);
        this.lifeExpectancy = 18 + Generator.nextInt(5);
    }
}
