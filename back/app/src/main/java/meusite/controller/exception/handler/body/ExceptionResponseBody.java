package meusite.controller.exception.handler.body;

public record ExceptionResponseBody(
        String errormessage,
        Integer status,
        String uri
) {
}
