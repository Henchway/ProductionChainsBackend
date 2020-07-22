package chains.db;

import chains.materials.Lifestock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LifestockRepository extends CrudRepository<Lifestock, Long> {

    List<Lifestock> findLifestocksByReadyForSlaughterAndClazz(boolean readyForSlaughter, Class<?> clazz, Pageable pageable);

    List<Lifestock> findLifestocksByClazz(Class<?> clazz, Pageable pageable);

}
