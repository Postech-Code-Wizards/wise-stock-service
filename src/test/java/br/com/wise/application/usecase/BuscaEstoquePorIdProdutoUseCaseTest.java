package br.com.wise.application.usecase;

import br.com.wise.domain.Stock;
import br.com.wise.gateway.database.StockGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class BuscaEstoquePorIdProdutoUseCaseTest {

    @Mock
    private StockGateway stockGateway;

    @InjectMocks
    private BuscaEstoquePorIdProdutoUseCase useCase;

    private final Long produtoId = 1L;
    private Stock stock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        stock = Stock.builder()
                .id(10L)
                .produtoId(produtoId)
                .quantidade(10)
                .atualizado(ZonedDateTime.now())
                .criado(ZonedDateTime.now().minusDays(1))
                .build();
    }

    @Test
    void deveRetornarEstoqueQuandoExistir() {
        when(stockGateway.buscaStockPorIdProduto(produtoId)).thenReturn(Optional.of(stock));

        Optional<Stock> resultado = useCase.execute(produtoId);

        assertTrue(resultado.isPresent());
        assertEquals(stock, resultado.get());
    }

    @Test
    void deveRetornarVazioQuandoNaoExistir() {
        when(stockGateway.buscaStockPorIdProduto(produtoId)).thenReturn(Optional.empty());

        Optional<Stock> resultado = useCase.execute(produtoId);

        assertTrue(resultado.isEmpty());
    }
}
