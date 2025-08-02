package br.com.wise.stock_service.gateway.queue;

import br.com.wise.stock_service.gateway.queue.rabbitmq.QueueRabbitMqGateway;
import br.com.wise.stock_service.infrastructure.configuration.RabbitMQConfig;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponseMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class QueueRabbitMqGatewayTest {

    private RabbitTemplate rabbitTemplate;
    private QueueRabbitMqGateway queueGateway;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        queueGateway = new QueueRabbitMqGateway(rabbitTemplate);
    }

    @Test
    void shouldSendSuccessMessage() {
        Long pedidoId = 1L;

        queueGateway.notificarSucesso(pedidoId);

        ArgumentCaptor<StockResponseMessage> captor = ArgumentCaptor.forClass(StockResponseMessage.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_NAME),
                eq(RabbitMQConfig.STOCK_RESPOSTA_ROUNTING_KEY),
                captor.capture()
        );

        StockResponseMessage message = captor.getValue();
        assertThat(message.getPedidoId()).isEqualTo(pedidoId);
        assertThat(message.getStatus()).isTrue();
    }

    @Test
    void shouldSendFailureMessage() {
        Long pedidoId = 2L;

        queueGateway.notificarFalha(pedidoId);

        ArgumentCaptor<StockResponseMessage> captor = ArgumentCaptor.forClass(StockResponseMessage.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE_NAME),
                eq(RabbitMQConfig.STOCK_RESPOSTA_ROUNTING_KEY),
                captor.capture()
        );

        StockResponseMessage message = captor.getValue();
        assertThat(message.getPedidoId()).isEqualTo(pedidoId);
        assertThat(message.getStatus()).isFalse();
    }
}
