package root.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import root.entitys.CurrencyEntity;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, Long>
{
    List<CurrencyEntity> findAllByenabled(Boolean enabled);

    List<CurrencyEntity> findAllByenabledToGive(Boolean enabled);
}
