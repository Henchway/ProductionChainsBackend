package chains.db;

import chains.materials.Lifestock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public void saveToDb(List<Lifestock> lifestock) {
        lifestockRepository.saveAll(lifestock);
    }

    public <T> List<Lifestock> retrieveLifestockByType(Class<T> requestedResource, int amount) {
        return lifestockRepository.findLifestocksByClazz(requestedResource, PageRequest.of(0, amount));
    }

    public <T> List<Lifestock> retrieveLifestockReadyForSlaughterByType(Class<T> requestedResource, boolean readyForSlaughter, int amount) {
        return lifestockRepository.removeLifestocksByReadyForSlaughterAndClazz(readyForSlaughter, requestedResource, PageRequest.of(0, amount));
    }

    public List<Lifestock> getAllLifestock() {
        return lifestockRepository.findAll();
    }

    public void removeFromDb(List<Lifestock> list) {
        lifestockRepository.deleteAll(list);
    }





}
