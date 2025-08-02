package br.com.wise.stock_service.converter;

import br.com.wise.stock_service.domain.Product;
import br.com.wise.stock_service.gateway.database.jpa.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductConverter {

    public Product toDomain(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getProdutoId()
        );
    }

    public Optional<Product> toDomain(Optional<ProductEntity> productEntity) {
        return productEntity.map(this::toDomain);

    }

    public ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getId(),
                product.getProdutoId()
        );
    }

}
