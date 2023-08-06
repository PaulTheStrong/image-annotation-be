package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.LicenseType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LicenseTypeRepository extends CrudRepository<LicenseType, Long> {
}
