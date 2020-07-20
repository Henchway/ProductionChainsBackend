package chains.materials.lifestock;

import chains.materials.Lifestock;
import chains.utility.Generator;

import java.util.Random;

public class Chicken extends Lifestock {

    public Chicken() {
        this.meat = 2 + Generator.nextInt(2);
    }
}
