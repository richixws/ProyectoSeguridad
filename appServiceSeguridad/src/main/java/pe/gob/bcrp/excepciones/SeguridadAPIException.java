package pe.gob.bcrp.excepciones;

import org.springframework.http.HttpStatus;

public class SeguridadAPIException extends RuntimeException{

    private HttpStatus status;
    private String message;

    public SeguridadAPIException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public SeguridadAPIException(String message, HttpStatus status, String message1) {
        super(message);
        this.status = status;
        this.message = message1;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
