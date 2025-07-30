package br.com.wise.stock_service.infrastructure.rabbitmq;

import br.com.wise.stock_service.application.usecase.ReporEstoqueUseCase;
import br.com.wise.stock_service.converter.StockConverter;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.infrastructure.rest.dto.request.StockRequestMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReporQuantidadeRabbitTest {

    private ReporEstoqueUseCase reporEstoqueUseCase;
    private StockConverter stockConverter;
    private ReporQuantidadeRabbit consumer;

    @BeforeEach
    void setUp() {
        reporEstoqueUseCase = mock(ReporEstoqueUseCase.class);
        stockConverter = mock(StockConverter.class);
        consumer = new ReporQuantidadeRabbit(reporEstoqueUseCase, stockConverter);
    }

    @Test
    void shouldReporEstoqueForEachMessage() {
        StockRequestMessage msg1 = new StockRequestMessage();
        msg1.setProdutoId(1L);
        msg1.setQuantidade(5);

        StockRequestMessage msg2 = new StockRequestMessage();
        msg2.setProdutoId(2L);
        msg2.setQuantidade(10);

        Stock stock1 = mock(Stock.class);
        Stock stock2 = mock(Stock.class);

        when(stockConverter.toDomain(1L, 5)).thenReturn(stock1);
        when(stockConverter.toDomain(2L, 10)).thenReturn(stock2);

        consumer.reporQuantidade(List.of(msg1, msg2));

        verify(stockConverter).toDomain(1L, 5);
        verify(stockConverter).toDomain(2L, 10);
        verify(reporEstoqueUseCase).execute(stock1);
        verify(reporEstoqueUseCase).execute(stock2);
    }
}
