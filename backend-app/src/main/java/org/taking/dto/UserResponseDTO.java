package org.taking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class UserResponseDTO {
  private List<UserRepresentation> items;
  private Integer totalCount;
}
