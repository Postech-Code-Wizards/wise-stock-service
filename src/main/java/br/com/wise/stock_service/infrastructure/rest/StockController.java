package br.com.wise.stock_service.infrastructure.rest;

import br.com.wise.stock_service.application.usecase.BaixarEstoqueUseCase;
import br.com.wise.stock_service.application.usecase.BuscaEstoquePorIdProdutoUseCase;
import br.com.wise.stock_service.application.usecase.ReporEstoqueUseCase;
import br.com.wise.stock_service.converter.StockConverter;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.infrastructure.rest.dto.request.QuantidadeRequest;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/estoque")
@RequiredArgsConstructor
public class StockController {

    private final StockConverter stockConverter;
    private final ReporEstoqueUseCase reporEstoqueUseCase;
    private final BuscaEstoquePorIdProdutoUseCase buscaEstoquePorIdProdutoUseCase;

    private final BaixarEstoqueUseCase baixarEstoqueUseCase;

    @GetMapping("/verifica/{produtoId}")
    public StockResponse verificaQuantidade(@PathVariable("produtoId") Long produtoId) {
        Stock stock = buscaEstoquePorIdProdutoUseCase.execute(produtoId);
        return stockConverter.toResponse(stock);
    }

    @PutMapping("/repor/{produtoId}")
    public StockResponse reporQuantidade(@PathVariable("produtoId") Long produtoId, @RequestBody QuantidadeRequest quantidade) {
        var stockRepor = stockConverter.toDomain(produtoId, quantidade.getQuantidade());
        var stock = reporEstoqueUseCase.execute(stockRepor);
        return stockConverter.toResponse(stock);
    }

    @PutMapping("/baixa/{produtoId}")
    public StockResponse baixaQuantidade(@PathVariable("produtoId") Long produtoId, @RequestBody QuantidadeRequest quantidade) {
        var stockBaixa = stockConverter.toDomain(produtoId, quantidade.getQuantidade());
        var stock = baixarEstoqueUseCase.execute(stockBaixa);
        return stockConverter.toResponse(stock);
    }
}
