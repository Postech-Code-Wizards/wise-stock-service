package br.com.wise.stock_service.converter;

import br.com.wise.stock_service.domain.Product;
import br.com.wise.stock_service.gateway.database.jpa.entity.ProductEntity;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ProductConverterTest {

    private final ProductConverter converter = new ProductConverter();

    @Test
    void shouldConvertEntityToDomain() {
        ProductEntity entity = new ProductEntity(1L, 100L);

        Product domain = converter.toDomain(entity);

        assertThat(domain).isNotNull();
        assertThat(domain.getId()).isEqualTo(1L);
        assertThat(domain.getProdutoId()).isEqualTo(100L);
    }

    @Test
    void shouldConvertOptionalEntityToOptionalDomain() {
        ProductEntity entity = new ProductEntity(2L, 200L);

        Optional<Product> optDomain = converter.toDomain(Optional.of(entity));
        Optional<Product> emptyDomain = converter.toDomain(Optional.empty());

        assertThat(optDomain).isPresent();
        assertThat(optDomain.get().getId()).isEqualTo(2L);
        assertThat(optDomain.get().getProdutoId()).isEqualTo(200L);
        assertThat(emptyDomain).isEmpty();
    }

    @Test
    void shouldConvertDomainToEntity() {
        Product domain = new Product(3L, 300L);

        ProductEntity entity = converter.toEntity(domain);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(3L);
        assertThat(entity.getProdutoId()).isEqualTo(300L);
    }
}
