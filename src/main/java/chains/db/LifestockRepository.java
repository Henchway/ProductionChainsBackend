package chains.db;

import chains.materials.Lifestock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public interface LifestockRepository extends CrudRepository<Lifestock, Long> {

    List<Lifestock> findLifestocksByReadyForSlaughter(boolean bool);

}
