package br.com.wise.jpa.entity;

import br.com.wise.gateway.database.jpa.entity.StockEntity;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@QuarkusTest
public class StockEntityTest {

    // Dados de exemplo para os testes
    private Long id;
    private Long produtoId;
    private Integer quantidade;
    private ZonedDateTime atualizado;
    private ZonedDateTime criado;

    @BeforeEach
    void setUp() {
        id = 1L;
        produtoId = 100L;
        quantidade = 50;
        atualizado = ZonedDateTime.now();
        criado = ZonedDateTime.now().minusDays(1);
    }

    /**
     * Testa o construtor sem argumentos (NoArgsConstructor) e os setters.
     * Deve criar uma instância de StockEntity com valores nulos/padrão
     * e permitir que os valores sejam definidos corretamente via setters.
     */
    @Test
    void noArgsConstructorAndSetters_shouldWorkCorrectly() {
        // GIVEN: Uma nova instância de StockEntity usando o construtor sem argumentos
        StockEntity stockEntity = new StockEntity();

        // WHEN: Definindo os valores usando os setters
        stockEntity.setId(id);
        stockEntity.setProdutoId(produtoId);
        stockEntity.setQuantidade(quantidade);
        stockEntity.setAtualizado(atualizado);
        stockEntity.setCriado(criado);

        // THEN: Os valores devem ser recuperados corretamente via getters
        assertNotNull(stockEntity);
        assertEquals(id, stockEntity.getId());
        assertEquals(produtoId, stockEntity.getProdutoId());
        assertEquals(quantidade, stockEntity.getQuantidade());
        assertEquals(atualizado, stockEntity.getAtualizado());
        assertEquals(criado, stockEntity.getCriado());
    }

    /**
     * Testa o construtor com todos os argumentos (AllArgsConstructor).
     * Deve criar uma instância de StockEntity com todos os campos inicializados.
     */
    @Test
    void allArgsConstructor_shouldWorkCorrectly() {
        // GIVEN: Uma nova instância de StockEntity usando o construtor com todos os argumentos
        StockEntity stockEntity = new StockEntity(id, produtoId, quantidade, atualizado, criado);

        // THEN: Os valores devem ser recuperados corretamente via getters
        assertNotNull(stockEntity);
        assertEquals(id, stockEntity.getId());
        assertEquals(produtoId, stockEntity.getProdutoId());
        assertEquals(quantidade, stockEntity.getQuantidade());
        assertEquals(atualizado, stockEntity.getAtualizado());
        assertEquals(criado, stockEntity.getCriado());
    }

    /**
     * Testa o padrão de construção (Builder).
     * Deve criar uma instância de StockEntity usando o builder Lombok.
     */
    @Test
    void builder_shouldCreateStockEntityCorrectly() {
        // GIVEN: Uma nova instância de StockEntity usando o builder
        StockEntity stockEntity = StockEntity.builder()
                .id(id)
                .produtoId(produtoId)
                .quantidade(quantidade)
                .atualizado(atualizado)
                .criado(criado)
                .build();

        // THEN: Os valores devem ser recuperados corretamente via getters
        assertNotNull(stockEntity);
        assertEquals(id, stockEntity.getId());
        assertEquals(produtoId, stockEntity.getProdutoId());
        assertEquals(quantidade, stockEntity.getQuantidade());
        assertEquals(atualizado, stockEntity.getAtualizado());
        assertEquals(criado, stockEntity.getCriado());
    }

    /**
     * Testa o comportamento do builder com campos opcionais ou nulos.
     * Garante que o builder pode criar uma entidade mesmo com campos nulos,
     * se permitido pela lógica de negócio (por exemplo, ID gerado pelo banco de dados).
     */
    @Test
    void builder_withNullId_shouldWorkCorrectly() {
        // GIVEN: Uma nova instância de StockEntity usando o builder com ID nulo
        StockEntity stockEntity = StockEntity.builder()
                .produtoId(produtoId)
                .quantidade(quantidade)
                .atualizado(atualizado)
                .criado(criado)
                .build();

        // THEN: Os valores devem ser recuperados corretamente, com ID nulo
        assertNotNull(stockEntity);
        assertNull(stockEntity.getId()); // ID deve ser nulo
        assertEquals(produtoId, stockEntity.getProdutoId());
        assertEquals(quantidade, stockEntity.getQuantidade());
    }
}
