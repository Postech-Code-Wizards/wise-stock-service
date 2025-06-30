package br.com.wise.converter;

import br.com.wise.gateway.database.jpa.entity.StockEntity;
import br.com.wise.domain.Stock;
import br.com.wise.infrastructure.rest.dto.request.QuantidadeRequest;
import br.com.wise.infrastructure.rest.dto.response.StockResponse;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.ZonedDateTime;
import java.util.Optional;

@ApplicationScoped
public class StockConverter {

    public Stock toDomain(StockEntity stock) {
        return new Stock(
                stock.getId(),
                stock.getProdutoId(),
                stock.getQuantidade(),
                stock.getAtualizado(),
                stock.getCriado()
        );
    }

    public Stock toDomain(Long produtoId, QuantidadeRequest quantidadeRequest) {
        return Stock.builder()
                .id(null)
                .produtoId(produtoId)
                .quantidade(quantidadeRequest.getQuantidade())
                .atualizado(ZonedDateTime.now())
                .criado(ZonedDateTime.now())
                .build();
    }

    public Stock toDomain(Stock existing, Integer novaQuantidade) {
        return new Stock(
                existing.getId(),
                existing.getProdutoId(),
                novaQuantidade,
                ZonedDateTime.now(),
                existing.getCriado()
        );
    }

    public Optional<Stock> toDomain(Optional<StockEntity> stockEntity) {
        return stockEntity.map(this::toDomain);
    }

    public StockEntity toEntity(Stock stock) {
        return new StockEntity(
                stock.getId(),
                stock.getProdutoId(),
                stock.getQuantidade(),
                stock.getAtualizado(),
                stock.getCriado()
        );
    }

    public StockResponse toResponse(Optional<Stock> exinstingStock) {
        return exinstingStock.map(stock -> StockResponse.builder()
                .id(stock.getId())
                .produtoId(stock.getProdutoId())
                .quantidade(stock.getQuantidade())
                .atualizado(stock.getAtualizado())
                .criado(stock.getCriado())
                .build()).orElse(null);

    }

    public StockResponse toResponse(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .produtoId(stock.getProdutoId())
                .quantidade(stock.getQuantidade())
                .atualizado(stock.getAtualizado())
                .criado(stock.getCriado())
                .build();
    }
}
