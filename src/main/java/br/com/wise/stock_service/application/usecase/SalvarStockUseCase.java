package br.com.wise.stock_service.application.usecase;

import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.gateway.database.StockGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SalvarStockUseCase {

    private final StockGateway stockGateway;

    public Stock execute(Stock stock) {
        return stockGateway.salvar(stock);
    }
}
