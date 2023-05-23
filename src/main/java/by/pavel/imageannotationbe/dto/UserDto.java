package by.pavel.imageannotationbe.dto;

import by.pavel.imageannotationbe.model.User;

import java.util.Collections;
import java.util.List;

public record UserDto(
        Long id,
        String email,
        String name,
        String surname,
        String password,
        List<LicenseDto> licenses
) {

    public static UserDto toDto(User entity) {
        return new UserDto(
            entity.getId(),
                entity.getEmail(),
                entity.getName(),
                entity.getSurname(),
                null,
                entity.getLicenses().stream().map(LicenseDto::toDto).toList()
        );
    }

    public static UserDto toDtoNoLicenses(User entity) {
        return new UserDto(
                entity.getId(),
                entity.getEmail(),
                entity.getName(),
                entity.getSurname(),
                null,
                Collections.emptyList()
        );
    }

}
