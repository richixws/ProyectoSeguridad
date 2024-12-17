package pe.gob.bcrp.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, Object> response=new HashMap<>();

        List<String> errors = ex.getBindingResult().getFieldErrors()
               // .stream().map(err-> "el campo '"+err.getField()+"'"+err.getDefaultMessage())
                .stream().map(err-> "El campo "+err.getDefaultMessage())
                .collect(Collectors.toList());

        response.put("message", errors);
        return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
    }
}
