package desafio.catalagosabio.domain.exception;

import lombok.Getter;

@Getter
public enum ExceptionEnum {
    PARAMETER_NULL("Parâmetro obrigatório não enviado");

    final String message;

    ExceptionEnum(String message) {
        this.message = message;
    }
}
