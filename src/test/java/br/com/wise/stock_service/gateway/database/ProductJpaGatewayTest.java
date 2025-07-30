package br.com.wise.stock_service.gateway.database;

import br.com.wise.stock_service.converter.ProductConverter;
import br.com.wise.stock_service.domain.Product;
import br.com.wise.stock_service.gateway.database.jpa.ProductJpaGateway;
import br.com.wise.stock_service.gateway.database.jpa.entity.ProductEntity;
import br.com.wise.stock_service.gateway.database.jpa.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductJpaGatewayTest {

    private ProductJpaGateway productJpaGateway;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductConverter productConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productJpaGateway = new ProductJpaGateway(productRepository, productConverter);
    }

    @Test
    void shouldReturnProductWhenFoundById() {
        Long produtoId = 1L;
        ProductEntity entity = new ProductEntity(1L, produtoId);
        Product domain = new Product(1L, produtoId);

        when(productRepository.findById(produtoId)).thenReturn(Optional.of(entity));
        when(productConverter.toDomain(Optional.of(entity))).thenReturn(Optional.of(domain));

        Optional<Product> result = productJpaGateway.buscaProdutoPorId(produtoId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(domain);

        verify(productRepository).findById(produtoId);
        verify(productConverter).toDomain(Optional.of(entity));
    }

    @Test
    void shouldReturnEmptyWhenProductNotFound() {
        Long produtoId = 2L;

        when(productRepository.findById(produtoId)).thenReturn(Optional.empty());
        when(productConverter.toDomain(Optional.empty())).thenReturn(Optional.empty());

        Optional<Product> result = productJpaGateway.buscaProdutoPorId(produtoId);

        assertThat(result).isEmpty();

        verify(productRepository).findById(produtoId);
        verify(productConverter).toDomain(Optional.empty());
    }

}
