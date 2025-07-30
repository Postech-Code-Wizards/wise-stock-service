package br.com.wise.stock_service.infrastructure.rabbitmq;

import br.com.wise.stock_service.application.usecase.ReporEstoqueUseCase;
import br.com.wise.stock_service.converter.StockConverter;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.infrastructure.configuration.RabbitMQConfig;
import br.com.wise.stock_service.infrastructure.rest.dto.request.StockRequestMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReporQuantidadeRabbit {

    private final ReporEstoqueUseCase reporEstoqueUseCase;
    private final StockConverter stockConverter;

    @RabbitListener(queues = RabbitMQConfig.STOCK_REPOR_QUEUE)
    public void reporQuantidade(List<StockRequestMessage> messages) {
        for (StockRequestMessage msg : messages) {
            Stock stock = stockConverter.toDomain(msg.getProdutoId(), msg.getQuantidade());
            reporEstoqueUseCase.execute(stock);
        }
    }
}
