package imt.fisa.invocations.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,Object> handleBadRequestException(BadRequestException e){
        return Map.of(
                "code", "400",
                "message", e.getMessage(),
                "timestamp", LocalDateTime.now()
        );

    }


    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String,Object> handleUnauthorizedException(UnauthorizedException e){
        return Map.of(
                "code", "401",
                "message", e.getMessage(),
                "timestamp", LocalDateTime.now()
        );

    }


    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,Object> handleInternalServerErrorException(InternalServerErrorException e){
        return Map.of(
                "code", "500",
                "message", e.getMessage(),
                "timestamp", LocalDateTime.now()
        );

    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,Object> handleNullPointerException(NullPointerException e){
        return Map.of(
                "code", "500",
                "message", e.getMessage(),
                "timestamp", LocalDateTime.now()
        );

    }


}
