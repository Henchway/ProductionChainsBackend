package chains.materials.lifestock;

import chains.materials.Lifestock;
import chains.materials.raw.Hay;
import chains.utility.Generator;
import lombok.Getter;

@Getter
public class Cow extends Lifestock {

    protected int milk;

    public Cow() {
        this.meat = 15 + Generator.nextInt(10);
        this.lifeExpectancy = 18 + Generator.nextInt(5);
        this.milk = 5 + Generator.nextInt(5);
        this.fodderAmount = 10;
        this.fodder = Hay.class;
    }
}
