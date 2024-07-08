package meusite.controller.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ValidateLoginDtoResponse(
        @JsonProperty("is_Valid") boolean isValid
) {
}
