package br.com.wise.converter;

import br.com.wise.domain.Stock;
import br.com.wise.gateway.database.jpa.entity.StockEntity;
import br.com.wise.infrastructure.rest.dto.response.StockResponse;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
public class StockConverterTest {

    @Inject
    StockConverter stockConverter;

    // Dados de exemplo para os testes
    private Long stockId;
    private Long produtoId;
    private Integer quantidade;
    private ZonedDateTime now;
    private ZonedDateTime earlier;

    @BeforeEach
    void setUp() {
        stockId = 1L;
        produtoId = 100L;
        quantidade = 50;
        now = ZonedDateTime.now();
        // Usar um ZonedDateTime um pouco anterior para o 'criado' para simular um cenário real
        earlier = now.minusHours(1);
    }

    /**
     * Testa o método toDomain(StockEntity stock).
     * Deve converter uma StockEntity em um objeto Stock de domínio.
     */
    @Test
    void toDomain_fromStockEntity_shouldConvertCorrectly() {
        // GIVEN: Uma StockEntity de exemplo
        StockEntity stockEntity = new StockEntity(stockId, produtoId, quantidade, now, earlier);

        // WHEN: Convertendo a StockEntity para um objeto Stock de domínio
        Stock stock = stockConverter.toDomain(stockEntity);

        // THEN: O objeto Stock deve ter os mesmos valores da StockEntity
        assertNotNull(stock);
        assertEquals(stockId, stock.getId());
        assertEquals(produtoId, stock.getProdutoId());
        assertEquals(quantidade, stock.getQuantidade());
        assertEquals(now, stock.getAtualizado());
        assertEquals(earlier, stock.getCriado());
    }

    /**
     * Testa o método toDomain(Stock existing, BigDecimal novaQuantidade).
     * Deve criar um novo objeto Stock de domínio com uma quantidade atualizada,
     * mantendo o ID e o ID do produto do estoque existente e atualizando 'atualizado'.
     */
    @Test
    void toDomain_fromExistingStockAndNewQuantidade_shouldUpdateQuantityAndTimestamp() {
        // GIVEN: Um objeto Stock existente e uma nova quantidade
        Stock existingStock = new Stock(stockId, produtoId, quantidade, earlier, earlier);
        Integer novaQuantidade = 120;

        // WHEN: Convertendo para um objeto Stock com a nova quantidade
        Stock updatedStock = stockConverter.toDomain(existingStock, novaQuantidade);

        // THEN: O objeto Stock deve ter a nova quantidade, ID e ID do produto do existente,
        // e o campo 'atualizado' deve ser a data/hora atual.
        assertNotNull(updatedStock);
        assertEquals(stockId, updatedStock.getId());
        assertEquals(produtoId, updatedStock.getProdutoId());
        assertEquals(novaQuantidade, updatedStock.getQuantidade());
        assertNotNull(updatedStock.getAtualizado());
        assertEquals(existingStock.getCriado(), updatedStock.getCriado()); // Criado deve ser mantido
        // Verificar se 'atualizado' está próximo da hora atual
        assertTrue(updatedStock.getAtualizado().isAfter(ZonedDateTime.now().minusSeconds(5)));
    }

    /**
     * Testa o método toDomain(Optional<StockEntity> stockEntity) quando o Optional contém um valor.
     * Deve converter a StockEntity encapsulada em um Optional<Stock>.
     */
    @Test
    void toDomain_fromOptionalStockEntity_withValue_shouldConvertCorrectly() {
        // GIVEN: Um Optional contendo uma StockEntity
        StockEntity stockEntity = new StockEntity(stockId, produtoId, quantidade, now, earlier);
        Optional<StockEntity> optionalStockEntity = Optional.of(stockEntity);

        // WHEN: Convertendo o Optional<StockEntity> para Optional<Stock>
        Optional<Stock> optionalStock = stockConverter.toDomain(optionalStockEntity);

        // THEN: O Optional<Stock> deve estar presente e conter o Stock convertido corretamente
        assertTrue(optionalStock.isPresent());
        Stock stock = optionalStock.get();
        assertEquals(stockId, stock.getId());
        assertEquals(produtoId, stock.getProdutoId());
        assertEquals(quantidade, stock.getQuantidade());
    }

    /**
     * Testa o método toDomain(Optional<StockEntity> stockEntity) quando o Optional está vazio.
     * Deve retornar um Optional.empty().
     */
    @Test
    void toDomain_fromOptionalStockEntity_withoutValue_shouldReturnEmptyOptional() {
        // GIVEN: Um Optional vazio de StockEntity
        Optional<StockEntity> optionalStockEntity = Optional.empty();

        // WHEN: Convertendo o Optional<StockEntity> para Optional<Stock>
        Optional<Stock> optionalStock = stockConverter.toDomain(optionalStockEntity);

        // THEN: O Optional<Stock> deve estar vazio
        assertFalse(optionalStock.isPresent());
    }

    /**
     * Testa o método toEntity(Stock stock).
     * Deve converter um objeto Stock de domínio em uma StockEntity.
     */
    @Test
    void toEntity_fromStock_shouldConvertCorrectly() {
        // GIVEN: Um objeto Stock de domínio
        Stock stock = new Stock(stockId, produtoId, quantidade, now, earlier);

        // WHEN: Convertendo o objeto Stock para uma StockEntity
        StockEntity stockEntity = stockConverter.toEntity(stock);

        // THEN: A StockEntity deve ter os mesmos valores do objeto Stock
        assertNotNull(stockEntity);
        assertEquals(stockId, stockEntity.getId());
        assertEquals(produtoId, stockEntity.getProdutoId());
        assertEquals(quantidade, stockEntity.getQuantidade());
        assertEquals(now, stockEntity.getAtualizado());
        assertEquals(earlier, stockEntity.getCriado());
    }

    /**
     * Testa o método toResponse(Optional<Stock> existingStock) quando o Optional contém um valor.
     * Deve converter o Stock encapsulado em um Optional<StockResponse>.
     */
    @Test
    void toResponse_fromOptionalStock_withValue_shouldConvertCorrectly() {
        // GIVEN: Um Optional contendo um objeto Stock de domínio
        Stock stock = new Stock(stockId, produtoId, quantidade, now, earlier);
        Optional<Stock> optionalStock = Optional.of(stock);

        // WHEN: Convertendo o Optional<Stock> para StockResponse
        StockResponse stockResponse = stockConverter.toResponse(optionalStock);

        // THEN: O StockResponse deve ser retornado e ter os valores corretos
        assertNotNull(stockResponse);
        assertEquals(stockId, stockResponse.getId());
        assertEquals(produtoId, stockResponse.getProdutoId());
        assertEquals(quantidade, stockResponse.getQuantidade());
        assertEquals(now, stockResponse.getAtualizado());
        assertEquals(earlier, stockResponse.getCriado());
    }

    /**
     * Testa o método toResponse(Optional<Stock> existingStock) quando o Optional está vazio.
     * Deve retornar null.
     */
    @Test
    void toResponse_fromOptionalStock_withoutValue_shouldReturnNull() {
        // GIVEN: Um Optional vazio de Stock
        Optional<Stock> optionalStock = Optional.empty();

        // WHEN: Convertendo o Optional<Stock> para StockResponse
        StockResponse stockResponse = stockConverter.toResponse(optionalStock);

        // THEN: O StockResponse deve ser nulo
        assertNull(stockResponse);
    }


    @Test
    void toResponse_fromStock_shouldConvertCorrectly() {
        // GIVEN: Um objeto Stock de domínio
        Stock stock = new Stock(stockId, produtoId, quantidade, now, earlier);

        // WHEN: Convertendo o objeto Stock para um StockResponse
        StockResponse stockResponse = stockConverter.toResponse(stock);

        // THEN: O StockResponse deve ter os mesmos valores do objeto Stock
        assertNotNull(stockResponse);
        assertEquals(stockId, stockResponse.getId());
        assertEquals(produtoId, stockResponse.getProdutoId());
        assertEquals(quantidade, stockResponse.getQuantidade());
        assertEquals(now, stockResponse.getAtualizado());
        assertEquals(earlier, stockResponse.getCriado());
    }
}
