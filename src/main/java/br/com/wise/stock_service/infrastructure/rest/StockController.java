package br.com.wise.stock_service.infrastructure.rest;

import br.com.wise.stock_service.application.service.StockService;
import br.com.wise.stock_service.infrastructure.rest.dto.request.QuantidadeRequest;
import br.com.wise.stock_service.infrastructure.rest.dto.request.StockRequestMessage;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponse;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/estoque")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/verifica/{produtoId}")
    public ResponseEntity<StockResponse> verificaQuantidade(@PathVariable("produtoId") Long produtoId) {
        return ResponseEntity.ok(stockService.verificaQuantidade(produtoId));
    }

    @PutMapping("/repor/{produtoId}")
    public ResponseEntity<StockResponse> reporQuantidade(@PathVariable("produtoId") Long produtoId, @RequestBody QuantidadeRequest quantidade) {
        return ResponseEntity.ok(stockService.reporQuantidade(produtoId, quantidade));
    }

/*    @PostMapping("/teste")
    public ResponseEntity<Void> testereporQuantidade(@RequestBody List<StockRequestMessage> message) {
        stockService.reporQuantidadeTeste(message);
        return ResponseEntity.status(HttpStatus.OK).build();
    }*/

    @PutMapping("/baixa/{produtoId}")
    public ResponseEntity<StockResponse> baixaQuantidade(@PathVariable("produtoId") Long produtoId, @RequestBody QuantidadeRequest quantidade) {
        return ResponseEntity.ok(stockService.baixaQuantidade(produtoId, quantidade));
    }
}
