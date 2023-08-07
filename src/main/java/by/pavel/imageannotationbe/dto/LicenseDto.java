package by.pavel.imageannotationbe.dto;

import by.pavel.imageannotationbe.model.License;

import java.time.LocalDateTime;

public record LicenseDto(
        Long licenseId,
        Long ownerId,
        String licenseType,
        LocalDateTime licenseStart,
        LocalDateTime licenseEnd
) {
    public static LicenseDto toDto(License entity) {
        return new LicenseDto(
                entity.getId(),
                entity.getOwner().getId(),
                entity.getLicenseType().getLicenseName(),
                entity.getStartDate(),
                entity.getEndDate()
        );
    }

}
