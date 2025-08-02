package br.com.wise.stock_service.gateway.database;

import br.com.wise.stock_service.converter.StockConverter;
import br.com.wise.stock_service.domain.Product;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.jpa.StockJpaGateway;
import br.com.wise.stock_service.gateway.database.jpa.entity.StockEntity;
import br.com.wise.stock_service.gateway.database.jpa.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StockJpaGatewayTest {

    private StockJpaGateway stockJpaGateway;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockConverter stockConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockJpaGateway = new StockJpaGateway(stockConverter, stockRepository);
    }

    @Test
    void shouldReturnStockWhenFoundByProdutoId() {
        Long produtoId = 1L;

        StockEntity stockEntity = new StockEntity(1L, null, 10, ZonedDateTime.now(), ZonedDateTime.now());
        Stock stock = new Stock(1L, new Product(1L, produtoId), 10, ZonedDateTime.now(), ZonedDateTime.now());

        when(stockRepository.findByProduto_Id(produtoId)).thenReturn(Optional.of(stockEntity));
        when(stockConverter.toDomain(Optional.of(stockEntity))).thenReturn(Optional.of(stock));

        Optional<Stock> result = stockJpaGateway.buscaStockPorIdProduto(produtoId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(stock);

        verify(stockRepository).findByProduto_Id(produtoId);
        verify(stockConverter).toDomain(Optional.of(stockEntity));
    }

    @Test
    void shouldReturnEmptyWhenStockNotFoundByProdutoId() {
        Long produtoId = 2L;

        when(stockRepository.findByProduto_Id(produtoId)).thenReturn(Optional.empty());
        when(stockConverter.toDomain(Optional.empty())).thenReturn(Optional.empty());

        Optional<Stock> result = stockJpaGateway.buscaStockPorIdProduto(produtoId);

        assertThat(result).isEmpty();

        verify(stockRepository).findByProduto_Id(produtoId);
        verify(stockConverter).toDomain(Optional.empty());
    }

    @Test
    void shouldSaveStockSuccessfully() {
        Stock stock = new Stock(3L, new Product(3L, 300L), 15, ZonedDateTime.now(), ZonedDateTime.now());
        StockEntity entity = new StockEntity(3L, null, 15, ZonedDateTime.now(), ZonedDateTime.now());
        StockEntity savedEntity = new StockEntity(3L, null, 15, ZonedDateTime.now(), ZonedDateTime.now());
        Stock expectedStock = new Stock(3L, new Product(3L, 300L), 15, ZonedDateTime.now(), ZonedDateTime.now());

        when(stockConverter.toEntity(stock)).thenReturn(entity);
        when(stockRepository.save(entity)).thenReturn(savedEntity);
        when(stockConverter.toDomain(savedEntity)).thenReturn(expectedStock);

        Stock result = stockJpaGateway.salvar(stock);

        assertThat(result).isEqualTo(expectedStock);

        verify(stockConverter).toEntity(stock);
        verify(stockRepository).save(entity);
        verify(stockConverter).toDomain(savedEntity);
    }
}
