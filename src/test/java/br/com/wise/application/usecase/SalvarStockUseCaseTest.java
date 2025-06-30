package br.com.wise.application.usecase;

import br.com.wise.domain.Stock;
import br.com.wise.gateway.database.StockGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class SalvarStockUseCaseTest {

    @Mock
    private StockGateway stockGateway;

    @InjectMocks
    private SalvarStockUseCase useCase;

    private Stock stock;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        stock = Stock.builder()
                .id(10L)
                .produtoId(1L)
                .quantidade(10)
                .atualizado(ZonedDateTime.now())
                .criado(ZonedDateTime.now().minusDays(1))
                .build();
    }

    @Test
    void deveSalvarStock() {
        when(stockGateway.salvar(stock)).thenReturn(stock);

        Stock resultado = useCase.execute(stock);

        assertNotNull(resultado);
        assertEquals(stock, resultado);
    }
}
