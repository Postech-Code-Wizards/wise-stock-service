package br.com.wise.application.usecase;

import br.com.wise.domain.Stock;
import br.com.wise.gateway.database.StockGateway;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SalvarStockUseCase {

    private final StockGateway stockGateway;

    public Stock execute(Stock stock) {
        return stockGateway.salvar(stock);
    }
}
