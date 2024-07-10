package meusite.controller.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import meusite.controller.exception.handler.body.ExceptionResponseBody;
import meusite.repository.post.exception.PostException;
import meusite.repository.user.exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice("meusite.controller.user")
public class UserControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { UserException.class})
    protected ResponseEntity<ExceptionResponseBody> handleLoginExceptions(
            final UserException anException,
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
