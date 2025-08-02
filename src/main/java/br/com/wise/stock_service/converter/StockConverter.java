package br.com.wise.stock_service.converter;

import br.com.wise.stock_service.domain.Product;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.jpa.entity.StockEntity;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StockConverter {

    private final ProductConverter productConverter;

    public Stock toDomain(StockEntity stock) {
        return new Stock(
                stock.getId(),
                productConverter.toDomain(stock.getProduto()),
                stock.getQuantidade(),
                stock.getAtualizado(),
                stock.getCriado()
        );
    }

    public Optional<Stock> toDomain(Optional<StockEntity> stockEntity) {
        return stockEntity.map(this::toDomain);
    }

    public StockEntity toEntity(Stock stock) {
        return new StockEntity(
                stock.getId(),
                productConverter.toEntity(stock.getProduto()),
                stock.getQuantidade(),
                stock.getAtualizado(),
                stock.getCriado()
        );
    }

    public StockResponse toResponse(Stock stock) {
        return new StockResponse(
                stock.getId(),
                stock.getProduto().getProdutoId(),
                stock.getQuantidade(),
                stock.getAtualizado(),
                stock.getCriado()
        );
    }

    public Stock toDomain(Long produtoId, Integer quantidade) {
        return new Stock(
                null,
                new Product(produtoId),
                quantidade,
                null,
                null
        );
    }
}
