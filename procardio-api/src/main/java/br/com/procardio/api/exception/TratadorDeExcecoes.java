package br.com.procardio.api.exception;

import java.util.NoSuchElementException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TratadorDeExcecoes {

    @ExceptionHandler(NoSuchElementException.class)
    public String tratarErro404() {
        return "erro/404";
    }

    @ExceptionHandler(Exception.class)
    public String tratarErro500(Exception e) {
        return "erro/500";
    }
    
}
