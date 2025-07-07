package br.com.wise.stock_service.gateway.database.jpa;

import br.com.wise.stock_service.converter.StockConverter;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import br.com.wise.stock_service.gateway.database.jpa.entity.StockEntity;
import br.com.wise.stock_service.gateway.database.jpa.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
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
