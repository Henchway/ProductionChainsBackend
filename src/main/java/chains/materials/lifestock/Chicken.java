package chains.materials.lifestock;

import chains.materials.Lifestock;
import chains.utility.Generator;


public class Chicken extends Lifestock {

    public Chicken() {
        this.meat = 2 + Generator.nextInt(2);
        this.lifeExpectancy = 5 + Generator.nextInt(6);
    }

    @Override
    public int compareTo(Lifestock o) {
        return super.compareTo(o);
    }
}
