package by.pavel.imageannotationbe.dto;


import java.util.Map;
import java.util.Set;

public record InvitationBatchDto(Long projectId, Map<String, Set<Long>> emailToRoleIds) {
}
