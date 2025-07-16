package br.com.wise.stock_service.application.service;

import br.com.wise.stock_service.application.usecase.BuscaEstoquePorIdProdutoUseCase;
import br.com.wise.stock_service.application.usecase.EnviaRespostaRabbitUseCase;
import br.com.wise.stock_service.application.usecase.SalvarStockUseCase;
import br.com.wise.stock_service.converter.StockConverter;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.infrastructure.rest.dto.request.QuantidadeRequest;
import br.com.wise.stock_service.infrastructure.rest.dto.request.StockRequestMessage;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponse;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponseMessage;
import br.com.wise.stock_service.infrastructure.rest.exception.ResourceNotFoundException;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockServiceTest {

    @InjectMocks
    private StockService stockService;

    @Mock
    private StockConverter stockConverter;

    @Mock
    private BuscaEstoquePorIdProdutoUseCase buscaEstoquePorIdProdutoUseCase;

    @Mock
    private SalvarStockUseCase salvarStockUseCase;

    @Mock
    private EnviaRespostaRabbitUseCase enviaRespostaRabbitUseCase;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Test
    void shouldVerificaQuantidadeSuccessfully() {
        Long produtoId = 1L;
        Stock stock = Instancio.create(Stock.class);
        StockResponse expectedResponse = Instancio.create(StockResponse.class);

        when(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).thenReturn(Optional.of(stock));
        when(stockConverter.toResponse(Optional.of(stock))).thenReturn(expectedResponse);

        StockResponse result = stockService.verificaQuantidade(produtoId);

        assertThat(result).isEqualTo(expectedResponse);
        verify(buscaEstoquePorIdProdutoUseCase).execute(produtoId);
        verify(stockConverter).toResponse(Optional.of(stock));
    }

    @Test
    void shouldThrowWhenVerificaQuantidadeNotFound() {
        Long produtoId = 1L;
        when(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> stockService.verificaQuantidade(produtoId));
    }

    @Test
    void shouldReporQuantidadeWhenExists() {
        Long produtoId = 1L;
        Stock existing = Instancio.of(Stock.class).set(field("quantidade"), 10).create();
        QuantidadeRequest request = new QuantidadeRequest(5);
        Stock atualizado = Instancio.create(Stock.class);
        Stock salvo = Instancio.create(Stock.class);
        StockResponse response = Instancio.create(StockResponse.class);

        when(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).thenReturn(Optional.of(existing));
        when(stockConverter.toDomain(existing, 15)).thenReturn(atualizado);
        when(salvarStockUseCase.execute(atualizado)).thenReturn(salvo);
        when(stockConverter.toResponse(salvo)).thenReturn(response);

        StockResponse result = stockService.reporQuantidade(produtoId, request);

        assertThat(result).isEqualTo(response);
        verify(salvarStockUseCase).execute(atualizado);
    }

    @Test
    void shouldReporQuantidadeWhenNotExists() {
        Long produtoId = 1L;
        QuantidadeRequest request = new QuantidadeRequest(7);
        Stock novo = Instancio.create(Stock.class);
        Stock salvo = Instancio.create(Stock.class);
        StockResponse response = Instancio.create(StockResponse.class);

        when(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).thenReturn(Optional.empty());
        when(stockConverter.toDomain(produtoId, request)).thenReturn(novo);
        when(salvarStockUseCase.execute(novo)).thenReturn(salvo);
        when(stockConverter.toResponse(salvo)).thenReturn(response);

        StockResponse result = stockService.reporQuantidade(produtoId, request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldBaixaQuantidadeSuccessfully() {
        Long produtoId = 1L;
        QuantidadeRequest request = new QuantidadeRequest(3);
        Stock existing = Instancio.of(Stock.class).set(field("quantidade"), 10).create();
        Stock atualizado = Instancio.create(Stock.class);
        Stock salvo = Instancio.create(Stock.class);
        StockResponse response = Instancio.create(StockResponse.class);

        when(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).thenReturn(Optional.of(existing));
        when(stockConverter.toDomain(existing, 7)).thenReturn(atualizado);
        when(salvarStockUseCase.execute(atualizado)).thenReturn(salvo);
        when(stockConverter.toResponse(salvo)).thenReturn(response);

        StockResponse result = stockService.baixaQuantidade(produtoId, request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void shouldThrowWhenBaixaQuantidadeNotFound() {
        Long produtoId = 1L;
        QuantidadeRequest request = new QuantidadeRequest(5);
        when(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> stockService.baixaQuantidade(produtoId, request));
    }

    @Test
    void shouldHandleBaixaQuantidadeRabbitWithSuccess() {
        Long pedidoId = 1L;
        Long produtoId = 10L;
        Integer quantidade = 3;

        StockRequestMessage message = Instancio.of(StockRequestMessage.class)
                .set(Select.field("pedidoId"), pedidoId)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), quantidade)
                .create();

        Stock stock = Instancio.of(Stock.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), 10)
                .create();

        Stock atualizado = Instancio.create(Stock.class);
        Stock salvo = Instancio.create(Stock.class);
        StockResponse response = Instancio.create(StockResponse.class);

        when(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).thenReturn(Optional.of(stock));
        when(stockConverter.toDomain(stock, 7)).thenReturn(atualizado);
        when(salvarStockUseCase.execute(atualizado)).thenReturn(salvo);
        when(stockConverter.toResponse(salvo)).thenReturn(response);

        stockService.baixaQuantidadeRabbit(List.of(message));

        verify(enviaRespostaRabbitUseCase).enviar(new StockResponseMessage(pedidoId, true));
    }

    @Test
    void shouldHandleBaixaQuantidadeRabbitWithNegativeStock() {
        Long pedidoId = 2L;
        Long produtoId = 20L;
        Integer quantidade = 15;

        StockRequestMessage message = Instancio.of(StockRequestMessage.class)
                .set(Select.field("pedidoId"), pedidoId)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), quantidade)
                .create();

        Stock stock = Instancio.of(Stock.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), 10)
                .create();

        when(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).thenReturn(Optional.of(stock));

        stockService.baixaQuantidadeRabbit(List.of(message));

        verify(enviaRespostaRabbitUseCase).enviar(new StockResponseMessage(pedidoId, false));
    }

    @Test
    void shouldHandleBaixaQuantidadeRabbitWithProdutoNotFound() {
        Long pedidoId = 3L;
        Long produtoId = 30L;

        StockRequestMessage message = Instancio.of(StockRequestMessage.class)
                .set(Select.field("pedidoId"), pedidoId)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), 5)
                .create();

        when(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).thenReturn(Optional.empty());

        stockService.baixaQuantidadeRabbit(List.of(message));

        verify(enviaRespostaRabbitUseCase).enviar(new StockResponseMessage(pedidoId, false));
    }

    @Test
    void shouldHandleReporQuantidadeRabbit() {
        StockRequestMessage msg1 = Instancio.of(StockRequestMessage.class)
                .set(Select.field("produtoId"), 100L)
                .set(Select.field("quantidade"), 5)
                .create();

        StockRequestMessage msg2 = Instancio.of(StockRequestMessage.class)
                .set(Select.field("produtoId"), 200L)
                .set(Select.field("quantidade"), 10)
                .create();

        Stock stock1 = Instancio.create(Stock.class);
        Stock stock2 = Instancio.create(Stock.class);
        StockResponse r1 = Instancio.create(StockResponse.class);
        StockResponse r2 = Instancio.create(StockResponse.class);

        when(buscaEstoquePorIdProdutoUseCase.execute(100L)).thenReturn(Optional.empty());
        when(stockConverter.toDomain(100L, new QuantidadeRequest(5))).thenReturn(stock1);
        when(salvarStockUseCase.execute(stock1)).thenReturn(stock1);
        when(stockConverter.toResponse(stock1)).thenReturn(r1);

        when(buscaEstoquePorIdProdutoUseCase.execute(200L)).thenReturn(Optional.empty());
        when(stockConverter.toDomain(200L, new QuantidadeRequest(10))).thenReturn(stock2);
        when(salvarStockUseCase.execute(stock2)).thenReturn(stock2);
        when(stockConverter.toResponse(stock2)).thenReturn(r2);

        stockService.reporQuantidadeRabbit(List.of(msg1, msg2));

        verify(salvarStockUseCase).execute(stock1);
        verify(salvarStockUseCase).execute(stock2);
    }
}
