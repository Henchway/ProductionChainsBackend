package chains.materials.lifestock;

import chains.materials.Lifestock;
import chains.materials.raw.Grain;
import chains.utility.Generator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Chicken extends Lifestock {

    public Chicken() {
        this.meat = 2 + Generator.nextInt(2);
        this.lifeExpectancy = 5 + Generator.nextInt(6);
        this.fodderAmount = 1;
        this.fodder = Grain.class;
    }

    @Override
    public int compareTo(Lifestock o) {
        return super.compareTo(o);
    }
}
