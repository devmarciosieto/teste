package desafio.catalagosabio.domain.exception;


import desafio.catalagosabio.application.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponseDTO> handleBusinessException(BusinessException businessException) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(businessException.getMessage(), businessException.code);
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException notFoundException) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(notFoundException.getMessage(), notFoundException.code);
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

}
