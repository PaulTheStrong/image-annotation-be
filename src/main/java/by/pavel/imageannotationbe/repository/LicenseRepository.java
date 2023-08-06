package by.pavel.imageannotationbe.repository;

import by.pavel.imageannotationbe.model.License;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface LicenseRepository extends CrudRepository<License, Long> {

    boolean existsLicenseByOwnerEmailAndEndDateAfter(String email, LocalDateTime time);

}
