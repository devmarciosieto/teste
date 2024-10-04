package desafio.catalagosabio.domain.exception;

import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
public enum ExceptionEnum {
    AUTHOR_PARAMETER_NULL("Author parameter is mandatory", Response.Status.BAD_REQUEST.getStatusCode()),
    GENRE_PARAMETER_NULL("Genre parameter is mandatory", Response.Status.BAD_REQUEST.getStatusCode());

    final String message;
    final Integer statusCode;

    ExceptionEnum(String message, Integer statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
