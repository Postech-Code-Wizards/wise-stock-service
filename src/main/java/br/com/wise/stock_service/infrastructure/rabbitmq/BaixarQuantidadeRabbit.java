package br.com.wise.stock_service.infrastructure.rabbitmq;

import br.com.wise.stock_service.application.usecase.BaixarEstoqueEmLoteUseCase;
import br.com.wise.stock_service.converter.StockItensConverter;
import br.com.wise.stock_service.infrastructure.configuration.RabbitMQConfig;
import br.com.wise.stock_service.infrastructure.rest.dto.request.StockItens;
import br.com.wise.stock_service.infrastructure.rest.dto.request.StockRequestMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BaixarQuantidadeRabbit {

    private final StockItensConverter stockItensConverter;
    private final BaixarEstoqueEmLoteUseCase baixarEstoqueEmLoteUseCase;

    @RabbitListener(queues = RabbitMQConfig.STOCK_BAIXA_QUEUE)
    public void baixarQuantidade(List<StockRequestMessage> messages) {
        if (messages == null || messages.isEmpty()) return;

        List<StockItens> stockItens = stockItensConverter.toDomainList(messages);

        baixarEstoqueEmLoteUseCase.execute(stockItens);
    }

}
