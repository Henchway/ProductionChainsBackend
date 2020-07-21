package chains.materials.lifestock;

import chains.materials.Lifestock;
import chains.utility.Generator;

public class Sheep extends Lifestock {

    private final int wool;

    public Sheep() {
        this.meat = 8 + Generator.nextInt(5);
        this.wool = 5 + Generator.nextInt(5);
        this.lifeExpectancy = 10 + Generator.nextInt(3);
    }

    public int getWool() {
        return wool;
    }
}
