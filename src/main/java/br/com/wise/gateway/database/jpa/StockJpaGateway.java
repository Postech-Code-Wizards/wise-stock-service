package br.com.wise.gateway.database.jpa;

import br.com.wise.converter.StockConverter;
import br.com.wise.domain.Stock;
import br.com.wise.gateway.database.StockGateway;
import br.com.wise.gateway.database.jpa.entity.StockEntity;
import br.com.wise.gateway.database.jpa.repository.StockRepository;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class StockJpaGateway implements StockGateway {

    private final StockConverter stockConverter;
    private final StockRepository stockRepository;

    @Override
    public Optional<Stock> buscaStockPorIdProduto(Long produtoId) {
        Optional<StockEntity> stockEntity = stockRepository.findByProdutoId(produtoId);
        return stockConverter.toDomain(stockEntity);
    }

    @Override
    public Stock salvar(Stock stock) {
        StockEntity stockEntity = stockConverter.toEntity(stock);
        StockEntity stockSaved = stockRepository.save(stockEntity);
        return stockConverter.toDomain(stockSaved);
    }
}
