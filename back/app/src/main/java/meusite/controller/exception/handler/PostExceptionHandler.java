package meusite.controller.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import meusite.controller.exception.handler.body.ExceptionResponseBody;
import meusite.repository.post.exception.PostException;
import meusite.service.auth.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice("meusite.controller.post")
public class PostExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { PostException.class})
    protected ResponseEntity<ExceptionResponseBody> handleLoginExceptions(
            final PostException anException,
            final HttpServletRequest aRequest
    ){
        final var aBody = new ExceptionResponseBody(
                anException.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                aRequest.getRequestURI()
        );

        return ResponseEntity.badRequest().body(aBody);
    }
}
