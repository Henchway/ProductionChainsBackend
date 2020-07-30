package chains.materials.lifestock;

import chains.materials.Lifestock;
import chains.utility.Generator;
import lombok.Getter;

@Getter
public class Cow extends Lifestock {

    protected int milk;

    public Cow() {
        this.meat = 20 + Generator.nextInt(10);
        this.lifeExpectancy = 18 + Generator.nextInt(5);
        this.milk = 5 + Generator.nextInt(5);
    }
}
