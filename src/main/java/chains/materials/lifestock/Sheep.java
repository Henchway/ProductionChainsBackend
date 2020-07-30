package chains.materials.lifestock;

import chains.materials.Lifestock;
import chains.materials.raw.Hay;
import chains.utility.Generator;


public class Sheep extends Lifestock {

    private final int wool;

    public Sheep() {
        this.meat = 7 + Generator.nextInt(5);
        this.wool = 5 + Generator.nextInt(5);
        this.lifeExpectancy = 10 + Generator.nextInt(3);
        this.fodderAmount = 5;
        this.fodder = Hay.class;
    }

    public int getWool() {
        return wool;
    }
}
