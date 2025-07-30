package br.com.wise.stock_service.gateway.database.jpa;

import br.com.wise.stock_service.converter.ProductConverter;
import br.com.wise.stock_service.domain.Product;
import br.com.wise.stock_service.gateway.database.ProductGateway;
import br.com.wise.stock_service.gateway.database.jpa.entity.ProductEntity;
import br.com.wise.stock_service.gateway.database.jpa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductJpaGateway implements ProductGateway {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    @Override
    public Optional<Product> buscaProdutoPorId(Long produtoId) {
        Optional<ProductEntity> productEntity = productRepository.findById(produtoId);
        return productConverter.toDomain(productEntity);
    }

}
