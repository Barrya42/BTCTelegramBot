package root.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import root.DBentitys.CurrencyEntity;

@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String>
{
    List<CurrencyEntity> findAllByenabled(Boolean enabled);

    List<CurrencyEntity> findAllByenabledToGive(Boolean enabled);
}
