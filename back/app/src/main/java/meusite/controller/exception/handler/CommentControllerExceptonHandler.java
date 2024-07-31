package meusite.controller.exception.handler;


import jakarta.servlet.http.HttpServletRequest;
import meusite.controller.exception.handler.body.ExceptionResponseBody;
import meusite.repository.coments.exception.ComentsException;
import meusite.repository.post.exception.PostException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice("meusite.controller.comments")
public class CommentControllerExceptonHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { ComentsException.class})
    protected ResponseEntity<ExceptionResponseBody> handleLoginExceptions(
            final ComentsException anException,
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
