package meusite.controller.user.dto;

public record VerifierCodeRequestDto(String code, String newpassword) {
}
