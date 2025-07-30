package br.com.wise.stock_service.application.usecase;

import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import br.com.wise.stock_service.gateway.queue.QueueGateway;
import br.com.wise.stock_service.infrastructure.rest.dto.request.StockItens;
import br.com.wise.stock_service.infrastructure.rest.exception.StockNegativeException;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaixarEstoqueEmLoteUseCaseTest {

    @InjectMocks
    private BaixarEstoqueEmLoteUseCase useCase;

    @Mock
    private StockGateway stockGateway;

    @Mock
    private QueueGateway queueGateway;

    private Long pedidoId;
    private List<StockItens> itens;
    private Stock estoque;

    @BeforeEach
    void setup() {
        pedidoId = 100L;

        itens = List.of(
                new StockItens(pedidoId, 1L , 3),
                new StockItens(pedidoId, 2L, 5)
        );

        estoque = Instancio.create(Stock.class);
    }

    @Test
    void shouldBaixarEstoqueComSucessoENotificar() {
        for (StockItens item : itens) {
            when(stockGateway.buscaStockPorIdProduto(item.getProdutoId())).thenReturn(Optional.of(estoque));
        }

        useCase.execute(itens);

        verify(stockGateway, times(2)).salvar(estoque);
        verify(queueGateway).notificarSucesso(pedidoId);
        verify(queueGateway, never()).notificarFalha(any());
    }

    @Test
    void shouldNotificarFalhaQuandoEstoqueNegativo() {
        Stock estoqueComErro = mock(Stock.class);
        when(stockGateway.buscaStockPorIdProduto(any())).thenReturn(Optional.of(estoqueComErro));

        doThrow(new StockNegativeException("Estoque insuficiente"))
                .when(estoqueComErro)
                .baixarQuantidade(anyInt());

        useCase.execute(itens);

        verify(stockGateway, never()).salvar(any());
        verify(queueGateway).notificarFalha(pedidoId);
        verify(queueGateway, never()).notificarSucesso(any());
    }
}
