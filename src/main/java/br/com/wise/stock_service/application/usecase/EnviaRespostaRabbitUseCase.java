package br.com.wise.stock_service.application.usecase;

import br.com.wise.stock_service.infrastructure.configuration.RabbitMQConfig;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnviaRespostaRabbitUseCase {

    private final RabbitTemplate rabbitTemplate;

    public void enviar(StockResponseMessage message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.STOCK_RESPOSTA_ROUNTING_KEY, message);
    }
}
