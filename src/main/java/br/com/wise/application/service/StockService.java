package br.com.wise.application.service;

import br.com.wise.application.usecase.BuscaEstoquePorIdProdutoUseCase;
import br.com.wise.application.usecase.SalvarStockUseCase;
import br.com.wise.converter.StockConverter;
import br.com.wise.infrastructure.exception.ValorNaoEncontradoException;
import br.com.wise.infrastructure.rest.dto.request.QuantidadeRequest;
import br.com.wise.infrastructure.rest.dto.response.StockResponse;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class StockService {

    private final StockConverter stockConverter;
    private final BuscaEstoquePorIdProdutoUseCase buscaEstoquePorIdProdutoUseCase;
    private final SalvarStockUseCase salvarStockUseCase;

    public StockResponse verificaQuantidade(Long produtoId) {
        return buscaEstoquePorIdProdutoUseCase.execute(produtoId)
                .map(stockConverter::toResponse).orElseThrow(() -> new ValorNaoEncontradoException("Produto com ID " + produtoId));
    }

    public StockResponse reporQuantidade(Long produtoId, QuantidadeRequest quantidadeRequest) {
        return buscaEstoquePorIdProdutoUseCase.execute(produtoId)
                .map(existing -> {
                    Integer novaQuantidade = existing.getQuantidade() + quantidadeRequest.getQuantidade();
                    var atualizado = stockConverter.toDomain(existing, novaQuantidade);
                    var salvo = salvarStockUseCase.execute(atualizado);
                    return stockConverter.toResponse(salvo);
                })
                .orElseGet(() -> {
                    var novo = stockConverter.toDomain(produtoId, quantidadeRequest);
                    var salvo = salvarStockUseCase.execute(novo);
                    return stockConverter.toResponse(salvo);
                });
    }

    public StockResponse baixaQuantidade(Long produtoId, QuantidadeRequest quantidadeRequest) {

        return buscaEstoquePorIdProdutoUseCase.execute(produtoId)
                .map(existing -> {
                    Integer novaQuantidade = existing.getQuantidade() - quantidadeRequest.getQuantidade();
                    var atualizado = stockConverter.toDomain(existing, novaQuantidade);
                    var salvo = salvarStockUseCase.execute(atualizado);
                    return stockConverter.toResponse(salvo);
                }).orElseThrow(() -> new ValorNaoEncontradoException("Produto com ID " + produtoId));
    }
}
