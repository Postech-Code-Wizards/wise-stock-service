package br.com.wise.stock_service.application.usecase;

import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalvarStockUseCaseTest {

    @InjectMocks
    private SalvarStockUseCase useCase;

    @Mock
    private StockGateway stockGateway;

    @Test
    void shouldSalvarStockSuccessfully() {
        Stock input = Instancio.create(Stock.class);
        Stock output = Instancio.create(Stock.class);

        when(stockGateway.salvar(input)).thenReturn(output);

        Stock result = useCase.execute(input);

        assertThat(result).isEqualTo(output);
        verify(stockGateway).salvar(input);
    }
}

