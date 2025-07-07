package br.com.wise.stock_service.application.service;

import br.com.wise.stock_service.application.usecase.BuscaEstoquePorIdProdutoUseCase;
import br.com.wise.stock_service.application.usecase.SalvarStockUseCase;
import br.com.wise.stock_service.converter.StockConverter;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.infrastructure.rest.dto.request.QuantidadeRequest;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponse;
import br.com.wise.stock_service.infrastructure.rest.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockConverter stockConverter;
    private final BuscaEstoquePorIdProdutoUseCase buscaEstoquePorIdProdutoUseCase;
    private final SalvarStockUseCase salvarStockUseCase;

    public StockResponse verificaQuantidade(Long produtoId) {
        Optional<Stock> stock = Optional.ofNullable(buscaEstoquePorIdProdutoUseCase.execute(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque ")));
        return stockConverter.toResponse(stock);
    }

    /*@PostConstruct
    void listen() {
        rabbitMQClient.basicConsumer(QUEUE)
                .subscribe().with(
                        consumer -> {
                            consumer.handler(this::handleReporQuantidade);
                            System.out.println("Escutando fila: " + QUEUE);
                        },
                        failure -> System.err.println("Erro ao escutar fila: " + failure.getMessage())
                );
    }

    private void handleReporQuantidade(RabbitMQMessage message) {
        JsonObject json = message.body().toJsonObject();
        StockRequestMessage dto = json.mapTo(StockRequestMessage.class);
        QuantidadeRequest qtd = new  QuantidadeRequest();
        qtd.setQuantidade(dto.getQuantidade());

        reporQuantidade(dto.getProdutoId(), qtd);
    }*/

    /*@Incoming("stock-soma")
    public void testRabbit(StockRequestMessage message) {
        log.info(message.getQuantidade().toString());
    }

    *//*@Incoming("stock-baixa")
    public void testRabbit(StockRequestMessage message) {
        log.info(message.toString());
    }*//*


    public StockRequestMessage product() {
        return StockRequestMessage.builder()
                .quantidade(10)
                .produtoId(1L)
                .build();
    }*/


    /*public void publishStockUpdate() {

        String message = "stock.soma" + "|" + product();

        // Envia a mensagem
        stockEmitter.send(message)
                .whenComplete((v, t) -> {
                    if (t != null) {
                        log.error("erro ao enviar");
                    } else {
                        log.error("tudo certo");
                    }
                });
    }*/

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
                }).orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + produtoId));
    }
}
