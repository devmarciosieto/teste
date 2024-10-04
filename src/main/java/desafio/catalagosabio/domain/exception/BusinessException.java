package desafio.catalagosabio.domain.exception;

public class BusinessException extends RuntimeException{

    final String message;

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }
}
