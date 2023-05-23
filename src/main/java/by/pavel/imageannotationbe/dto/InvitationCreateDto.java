package by.pavel.imageannotationbe.dto;


import java.util.Set;

public record InvitationCreateDto(Long projectId, String email, Set<Long> roleIds) {
}
