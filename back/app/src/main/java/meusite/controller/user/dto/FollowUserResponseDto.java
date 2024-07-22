package meusite.controller.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FollowUserResponseDto(
        @JsonProperty("followed") boolean followed
) {
}
