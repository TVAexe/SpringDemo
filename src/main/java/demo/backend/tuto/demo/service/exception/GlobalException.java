package demo.backend.tuto.demo.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import demo.backend.tuto.demo.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<RestResponse<Object> > handleIdException(IdInvalidException idInvalidException) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(idInvalidException.getMessage());
        restResponse.setMessage("Error occurred");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
}
