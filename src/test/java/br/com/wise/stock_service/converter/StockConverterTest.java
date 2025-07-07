package br.com.wise.stock_service.converter;

import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.jpa.entity.StockEntity;
import br.com.wise.stock_service.infrastructure.rest.dto.request.QuantidadeRequest;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponse;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class StockConverterTest {

    private StockConverter converter;

    @BeforeEach
    void setUp() {
        converter = new StockConverter();
    }

    @Test
    void shouldConvertStockEntityToDomain() {
        StockEntity entity = Instancio.create(StockEntity.class);

        Stock domain = converter.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(entity.getId());
        assertThat(domain.getProdutoId()).isEqualTo(entity.getProdutoId());
        assertThat(domain.getQuantidade()).isEqualTo(entity.getQuantidade());
        assertThat(domain.getAtualizado()).isEqualTo(entity.getAtualizado());
        assertThat(domain.getCriado()).isEqualTo(entity.getCriado());
    }

    @Test
    void shouldConvertProdutoIdAndQuantidadeRequestToDomain() {
        Long produtoId = 123L;
        QuantidadeRequest request = new QuantidadeRequest(5);

        Stock result = converter.toDomain(produtoId, request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getProdutoId()).isEqualTo(produtoId);
        assertThat(result.getQuantidade()).isEqualTo(request.getQuantidade());
        assertThat(result.getAtualizado()).isNotNull();
        assertThat(result.getCriado()).isNotNull();
    }

    @Test
    void shouldConvertExistingStockAndNovaQuantidadeToDomain() {
        Stock existing = Instancio.create(Stock.class);
        int novaQuantidade = 42;

        Stock updated = converter.toDomain(existing, novaQuantidade);

        assertThat(updated).isNotNull();
        assertThat(updated.getId()).isEqualTo(existing.getId());
        assertThat(updated.getProdutoId()).isEqualTo(existing.getProdutoId());
        assertThat(updated.getQuantidade()).isEqualTo(novaQuantidade);
        assertThat(updated.getAtualizado()).isNotNull();
        assertThat(updated.getCriado()).isEqualTo(existing.getCriado());
    }

    @Test
    void shouldConvertOptionalStockEntityToOptionalDomain() {
        StockEntity entity = Instancio.create(StockEntity.class);

        Optional<Stock> result = converter.toDomain(Optional.of(entity));

        assertThat(result).isPresent();
        assertThat(result.get().getProdutoId()).isEqualTo(entity.getProdutoId());
    }

    @Test
    void shouldConvertStockToEntity() {
        Stock stock = Instancio.create(Stock.class);

        StockEntity entity = converter.toEntity(stock);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(stock.getId());
        assertThat(entity.getProdutoId()).isEqualTo(stock.getProdutoId());
        assertThat(entity.getQuantidade()).isEqualTo(stock.getQuantidade());
        assertThat(entity.getAtualizado()).isEqualTo(stock.getAtualizado());
        assertThat(entity.getCriado()).isEqualTo(stock.getCriado());
    }

    @Test
    void shouldConvertOptionalStockToResponse() {
        Stock stock = Instancio.create(Stock.class);

        StockResponse response = converter.toResponse(Optional.of(stock));

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(stock.getId());
        assertThat(response.getProdutoId()).isEqualTo(stock.getProdutoId());
        assertThat(response.getQuantidade()).isEqualTo(stock.getQuantidade());
    }

    @Test
    void shouldReturnNullWhenOptionalStockIsEmpty() {
        StockResponse response = converter.toResponse(Optional.empty());

        assertThat(response).isNull();
    }

    @Test
    void shouldConvertStockToResponse() {
        Stock stock = Instancio.create(Stock.class);

        StockResponse response = converter.toResponse(stock);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(stock.getId());
        assertThat(response.getProdutoId()).isEqualTo(stock.getProdutoId());
        assertThat(response.getQuantidade()).isEqualTo(stock.getQuantidade());
        assertThat(response.getAtualizado()).isEqualTo(stock.getAtualizado());
        assertThat(response.getCriado()).isEqualTo(stock.getCriado());
    }
}
