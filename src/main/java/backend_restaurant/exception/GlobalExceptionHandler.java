package backend_restaurant.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<Map<String, String>> handleUnrecognizedProperty(UnrecognizedPropertyException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Propiedad no reconocida en el JSON: '" + ex.getPropertyName() + "'");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
