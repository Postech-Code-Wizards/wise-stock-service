package br.com.wise.stock_service.infrastructure.rabbitmq;

import br.com.wise.stock_service.application.usecase.BaixarEstoqueEmLoteUseCase;
import br.com.wise.stock_service.converter.StockItensConverter;
import br.com.wise.stock_service.infrastructure.rest.dto.request.StockItens;
import br.com.wise.stock_service.infrastructure.rest.dto.request.StockRequestMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class BaixarQuantidadeRabbitTest {

    private StockItensConverter stockItensConverter;
    private BaixarEstoqueEmLoteUseCase baixarEstoqueEmLoteUseCase;
    private BaixarQuantidadeRabbit consumer;

    @BeforeEach
    void setUp() {
        stockItensConverter = mock(StockItensConverter.class);
        baixarEstoqueEmLoteUseCase = mock(BaixarEstoqueEmLoteUseCase.class);
        consumer = new BaixarQuantidadeRabbit(stockItensConverter, baixarEstoqueEmLoteUseCase);
    }

    @Test
    void shouldNotCallUseCaseWhenMessageListIsNull() {
        consumer.baixarQuantidade(null);
        verifyNoInteractions(stockItensConverter, baixarEstoqueEmLoteUseCase);
    }

    @Test
    void shouldNotCallUseCaseWhenMessageListIsEmpty() {
        consumer.baixarQuantidade(List.of());
        verifyNoInteractions(stockItensConverter, baixarEstoqueEmLoteUseCase);
    }

    @Test
    void shouldConvertAndCallUseCaseWhenMessagesPresent() {
        StockRequestMessage msg1 = new StockRequestMessage();
        msg1.setPedidoId(1L);
        msg1.setProdutoId(10L);
        msg1.setQuantidade(3);

        List<StockRequestMessage> messages = List.of(msg1);
        List<StockItens> stockItensList = mock(List.class);

        when(stockItensConverter.toDomainList(messages)).thenReturn(stockItensList);

        consumer.baixarQuantidade(messages);

        verify(stockItensConverter).toDomainList(messages);
        verify(baixarEstoqueEmLoteUseCase).execute(stockItensList);
    }
}
