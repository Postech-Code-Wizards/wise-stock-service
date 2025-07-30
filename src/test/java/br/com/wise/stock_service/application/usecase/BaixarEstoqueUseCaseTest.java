package br.com.wise.stock_service.application.usecase;

import br.com.wise.stock_service.domain.Product;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import br.com.wise.stock_service.infrastructure.rest.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaixarEstoqueUseCaseTest {

    @InjectMocks
    private BaixarEstoqueUseCase useCase;

    @Mock
    private StockGateway stockGateway;

    @Test
    void shouldBaixarQuantidadeWhenStockExists() {
        Long produtoId = 1L;
        Product product = new Product(produtoId);
        Stock stockBaixa = new Stock(product, 5); // diminuir 5

        Stock stockExistente = new Stock(product, 10); // tem 10 no estoque

        Stock stockAtualizado = new Stock(product, 5); // após a baixa

        when(stockGateway.buscaStockPorIdProduto(produtoId)).thenReturn(Optional.of(stockExistente));
        when(stockGateway.salvar(stockExistente)).thenReturn(stockAtualizado);

        Stock result = useCase.execute(stockBaixa);

        assertThat(result.getQuantidade()).isEqualTo(5); // 10 - 5
        verify(stockGateway).buscaStockPorIdProduto(produtoId);
        verify(stockGateway).salvar(stockExistente);
    }

    @Test
    void shouldThrowExceptionWhenStockNotFound() {
        Long produtoId = 2L;
        Product product = new Product(produtoId);
        Stock stockBaixa = new Stock(product, 5);

        when(stockGateway.buscaStockPorIdProduto(produtoId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(stockBaixa))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produto com ID " + produtoId);

        verify(stockGateway).buscaStockPorIdProduto(produtoId);
        verify(stockGateway, never()).salvar(any());
    }
}
