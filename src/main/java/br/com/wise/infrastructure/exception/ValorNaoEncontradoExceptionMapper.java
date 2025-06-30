package br.com.wise.infrastructure.exception;

import br.com.wise.infrastructure.configuration.MessageService;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Provider
@RequiredArgsConstructor
public class ValorNaoEncontradoExceptionMapper implements ExceptionMapper<ValorNaoEncontradoException> {

    @Inject
    MessageService messageService;

    @Override
    public Response toResponse(ValorNaoEncontradoException exception) {
        String base = messageService.getMessage("erro.valor.nao.encontrado");
        String fullMessage = exception.getMessage() + " " + base;

        var erro = Map.of(
                "mensagem", fullMessage,
                "codigo", "404",
                "erro", "ValorNãoEncontrado"
        );
        return Response.status(Response.Status.NOT_FOUND).entity(erro).build();
    }
}
