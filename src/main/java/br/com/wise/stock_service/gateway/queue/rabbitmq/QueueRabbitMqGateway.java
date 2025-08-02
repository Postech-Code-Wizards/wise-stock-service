package br.com.wise.stock_service.gateway.queue.rabbitmq;

import br.com.wise.stock_service.gateway.queue.QueueGateway;
import br.com.wise.stock_service.infrastructure.configuration.RabbitMQConfig;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class QueueRabbitMqGateway implements QueueGateway {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void notificarSucesso(Long pedidoId) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.STOCK_RESPOSTA_ROUNTING_KEY, new StockResponseMessage(pedidoId, true));
    }

    @Override
    public void notificarFalha(Long pedidoId) {
        log.error("Produto {} sem estoque", pedidoId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.STOCK_RESPOSTA_ROUNTING_KEY, new StockResponseMessage(pedidoId, false));
    }
}
