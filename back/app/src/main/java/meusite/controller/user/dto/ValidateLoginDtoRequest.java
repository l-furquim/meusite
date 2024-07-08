package meusite.controller.user.dto;

import jakarta.validation.constraints.NotBlank;

public record ValidateLoginDtoRequest(
        @NotBlank(message = "token nao pode ser vazio !")String token
) {
}
