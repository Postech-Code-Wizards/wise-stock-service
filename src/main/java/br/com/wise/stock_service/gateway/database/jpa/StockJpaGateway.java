package br.com.wise.stock_service.gateway.database.jpa;

import br.com.wise.stock_service.converter.StockConverter;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import br.com.wise.stock_service.gateway.database.jpa.entity.StockEntity;
import br.com.wise.stock_service.gateway.database.jpa.repository.StockRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StockJpaGateway implements StockGateway {

    private final StockConverter stockConverter;
    private final StockRepository stockRepository;

    @Override
    public Optional<Stock> buscaStockPorIdProduto(Long produtoId) {
        Optional<StockEntity> stockEntity = stockRepository.findByProduto_Id(produtoId);
        return stockConverter.toDomain(stockEntity);
    }

    @Transactional
    @Override
    public Stock salvar(Stock stock) {
        StockEntity stockEntity = stockConverter.toEntity(stock);
        StockEntity stockSaved = stockRepository.save(stockEntity);
        return stockConverter.toDomain(stockSaved);
    }
}
