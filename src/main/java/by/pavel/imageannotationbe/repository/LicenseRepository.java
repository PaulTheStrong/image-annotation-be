package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.License;
import org.springframework.data.repository.CrudRepository;

public interface LicenseRepository extends CrudRepository<License, Long> {

    boolean existsLicenseByOwnerEmail(String email);

}
