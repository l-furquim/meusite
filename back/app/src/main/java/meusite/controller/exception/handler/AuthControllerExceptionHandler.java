package meusite.controller.exception.handler;

import jakarta.servlet.http.HttpServletRequest;
import meusite.controller.exception.handler.body.ExceptionResponseBody;
import meusite.service.auth.exception.AuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.auth.login.LoginException;

@ControllerAdvice("meusite.controller.user")
public class AuthControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { AuthException.class})
    protected ResponseEntity<ExceptionResponseBody> handleLoginExceptions(
            final AuthException anException,
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
