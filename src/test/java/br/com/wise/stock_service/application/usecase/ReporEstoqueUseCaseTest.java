package br.com.wise.stock_service.application.usecase;

import br.com.wise.stock_service.domain.Product;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReporEstoqueUseCaseTest {

    @InjectMocks
    private ReporEstoqueUseCase useCase;

    @Mock
    private StockGateway stockGateway;

    @Test
    void shouldReporQuantidadeWhenStockAlreadyExists() {
        // Arrange
        Long produtoId = 1L;
        Product product = new Product(produtoId);
        Stock incomingStock = new Stock(product, 10);

        // Estoque atual no banco (simulado)
        Stock existingStock = new Stock(product, 20);

        Stock expectedSavedStock = new Stock(product, 30); // 20 + 10

        when(stockGateway.buscaStockPorIdProduto(produtoId)).thenReturn(Optional.of(existingStock));
        when(stockGateway.salvar(existingStock)).thenReturn(expectedSavedStock);

        // Act
        Stock result = useCase.execute(incomingStock);

        // Assert
        assertThat(result.getQuantidade()).isEqualTo(30);
        verify(stockGateway).buscaStockPorIdProduto(produtoId);
        verify(stockGateway).salvar(existingStock);
    }

    @Test
    void shouldSaveNewStockWhenNotExists() {
        // Arrange
        Long produtoId = 2L;
        Product product = new Product(produtoId);
        Stock incomingStock = new Stock(product, 15);

        Stock savedStock = new Stock(product, 15);

        when(stockGateway.buscaStockPorIdProduto(produtoId)).thenReturn(Optional.empty());
        when(stockGateway.salvar(any(Stock.class))).thenReturn(savedStock);

        // Act
        Stock result = useCase.execute(incomingStock);

        // Assert
        assertThat(result.getQuantidade()).isEqualTo(15);
        assertThat(result.getProduto().getProdutoId()).isEqualTo(produtoId);
        verify(stockGateway).buscaStockPorIdProduto(produtoId);
        verify(stockGateway).salvar(any(Stock.class));
    }

}
