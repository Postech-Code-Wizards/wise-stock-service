package br.com.wise.stock_service.converter;

import br.com.wise.stock_service.domain.Product;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.jpa.entity.ProductEntity;
import br.com.wise.stock_service.gateway.database.jpa.entity.StockEntity;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StockConverterTest {

    private StockConverter stockConverter;

    @Mock
    private ProductConverter productConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stockConverter = new StockConverter(productConverter);
    }

    @Test
    void shouldConvertEntityToDomain() {
        ProductEntity productEntity = new ProductEntity(1L, 100L);
        StockEntity stockEntity = new StockEntity(10L, productEntity, 20, ZonedDateTime.now(), ZonedDateTime.now());
        Product productDomain = new Product(1L, 100L);

        when(productConverter.toDomain(productEntity)).thenReturn(productDomain);

        Stock stock = stockConverter.toDomain(stockEntity);

        assertThat(stock).isNotNull();
        assertThat(stock.getId()).isEqualTo(10L);
        assertThat(stock.getProduto()).isEqualTo(productDomain);
        assertThat(stock.getQuantidade()).isEqualTo(20);

        verify(productConverter).toDomain(productEntity);
    }

    @Test
    void shouldConvertOptionalEntityToOptionalDomain() {
        ProductEntity productEntity = new ProductEntity(2L, 200L);
        StockEntity stockEntity = new StockEntity(20L, productEntity, 15, ZonedDateTime.now(), ZonedDateTime.now());
        Product productDomain = new Product(2L, 200L);

        when(productConverter.toDomain(productEntity)).thenReturn(productDomain);

        Optional<Stock> optionalStock = stockConverter.toDomain(Optional.of(stockEntity));
        Optional<Stock> emptyOptional = stockConverter.toDomain(Optional.empty());

        assertThat(optionalStock).isPresent();
        assertThat(optionalStock.get().getId()).isEqualTo(20L);
        assertThat(optionalStock.get().getProduto()).isEqualTo(productDomain);
        assertThat(emptyOptional).isEmpty();

        verify(productConverter).toDomain(productEntity);
    }

    @Test
    void shouldConvertDomainToEntity() {
        Product productDomain = new Product(3L, 300L);
        Stock stockDomain = new Stock(30L, productDomain, 25, ZonedDateTime.now(), ZonedDateTime.now());
        ProductEntity productEntity = new ProductEntity(3L, 300L);

        when(productConverter.toEntity(productDomain)).thenReturn(productEntity);

        StockEntity stockEntity = stockConverter.toEntity(stockDomain);

        assertThat(stockEntity).isNotNull();
        assertThat(stockEntity.getId()).isEqualTo(30L);
        assertThat(stockEntity.getProduto()).isEqualTo(productEntity);
        assertThat(stockEntity.getQuantidade()).isEqualTo(25);

        verify(productConverter).toEntity(productDomain);
    }

    @Test
    void shouldConvertDomainToResponse() {
        ZonedDateTime now = ZonedDateTime.now();
        Product product = new Product(5L, 500L);
        Stock stock = new Stock(50L, product, 35, now, now);

        StockResponse response = stockConverter.toResponse(stock);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(50L);
        assertThat(response.getProdutoId()).isEqualTo(500L);
        assertThat(response.getQuantidade()).isEqualTo(35);
        assertThat(response.getAtualizado()).isEqualTo(now);
        assertThat(response.getCriado()).isEqualTo(now);
    }

    @Test
    void shouldCreateDomainFromProdutoIdAndQuantidade() {
        Long produtoId = 7L;
        Integer quantidade = 40;

        Stock stock = stockConverter.toDomain(produtoId, quantidade);

        assertThat(stock).isNotNull();
        assertThat(stock.getId()).isNull();
        assertThat(stock.getProduto().getProdutoId()).isEqualTo(produtoId);
        assertThat(stock.getQuantidade()).isEqualTo(quantidade);
        assertThat(stock.getAtualizado()).isNull();
        assertThat(stock.getCriado()).isNull();
    }
}
