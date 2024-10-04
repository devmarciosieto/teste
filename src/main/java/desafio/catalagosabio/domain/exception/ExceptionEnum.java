package desafio.catalagosabio.domain.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public enum ExceptionEnum {
    PARAMETER_NULL("", Response.Status.BAD_REQUEST.getStatusCode());

    final String message;
    final Integer statusCode;

    ExceptionEnum(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
