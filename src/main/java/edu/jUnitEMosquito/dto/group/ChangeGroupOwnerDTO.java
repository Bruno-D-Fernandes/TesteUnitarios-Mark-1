package edu.jUnitEMosquito.dto.group;

public record ChangeGroupOwnerDTO(
        Long groupId,
        Long newOwnerId
) {
}
