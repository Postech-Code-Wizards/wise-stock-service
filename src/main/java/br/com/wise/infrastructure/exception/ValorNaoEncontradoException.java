package br.com.wise.infrastructure.exception;

import lombok.Getter;

@Getter
public class ValorNaoEncontradoException extends RuntimeException {

    private final String message;

    public ValorNaoEncontradoException(String message) {
        super(message);
        this.message = message;
    }
}
