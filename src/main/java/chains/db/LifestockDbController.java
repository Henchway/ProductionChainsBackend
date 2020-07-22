package chains.db;

import chains.materials.Lifestock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LifestockDbController {

    private final LifestockRepository lifestockRepository;

    @Autowired
    public LifestockDbController(LifestockRepository lifestockRepository) {
        this.lifestockRepository = lifestockRepository;
    }

    public void saveToDb(Lifestock lifestock) {
        lifestockRepository.save(lifestock);
    }


}
