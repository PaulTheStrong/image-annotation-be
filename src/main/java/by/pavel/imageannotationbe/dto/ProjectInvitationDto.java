package by.pavel.imageannotationbe.dto;

import java.util.Set;

public record ProjectInvitationDto(Long id, String email, Set<Long> roles) {

}
