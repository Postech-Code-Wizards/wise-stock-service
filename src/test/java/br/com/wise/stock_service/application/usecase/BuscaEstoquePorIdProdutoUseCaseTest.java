package br.com.wise.stock_service.application.usecase;

import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscaEstoquePorIdProdutoUseCaseTest {

    @InjectMocks
    private BuscaEstoquePorIdProdutoUseCase useCase;

    @Mock
    private StockGateway stockGateway;

    @Test
    void shouldReturnStockWhenExists() {
        Long produtoId = 10L;
        Stock stock = Instancio.create(Stock.class);
        when(stockGateway.buscaStockPorIdProduto(produtoId)).thenReturn(Optional.of(stock));

        Optional<Stock> result = useCase.execute(produtoId);

        assertThat(result).isPresent().contains(stock);
    }

    @Test
    void shouldReturnEmptyWhenNotExists() {
        Long produtoId = 1L;
        when(stockGateway.buscaStockPorIdProduto(produtoId)).thenReturn(Optional.empty());

        Optional<Stock> result = useCase.execute(produtoId);

        assertThat(result).isEmpty();
    }
}

