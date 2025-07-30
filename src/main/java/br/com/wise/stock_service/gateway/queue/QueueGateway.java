package br.com.wise.stock_service.gateway.queue;

public interface QueueGateway {

    void notificarSucesso(Long pedidoId);
    void notificarFalha(Long pedidoId);
}
