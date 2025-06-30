package br.com.wise.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

class StockTest {

    private Stock stock;
    private final Long id = 1L;
    private final Long produtoId = 100L;
    private final Integer quantidade = 10;
    private final ZonedDateTime criado = ZonedDateTime.now().minusDays(1);
    private final ZonedDateTime atualizado = ZonedDateTime.now();

    @BeforeEach
    void setUp() {
        stock = new Stock();
    }

    @Test
    void testNoArgsConstructor() {
        assertNull(stock.getId(), "ID deve ser nulo");
        assertNull(stock.getProdutoId(), "ProdutoId deve ser nulo");
        assertNull(stock.getQuantidade(), "Quantidade deve ser nula");
        assertNull(stock.getCriado(), "Criado deve ser nulo");
        assertNull(stock.getAtualizado(), "Atualizado deve ser nulo");
    }

    @Test
    void testAllArgsConstructor() {
        Stock stock = new Stock(id, produtoId, quantidade, atualizado, criado);

        assertEquals(id, stock.getId(), "ID deve ser igual ao fornecido");
        assertEquals(produtoId, stock.getProdutoId(), "ProdutoId deve ser igual ao fornecido");
        assertEquals(quantidade, stock.getQuantidade(), "Quantidade deve ser igual ao fornecido");
        assertEquals(atualizado, stock.getAtualizado(), "Atualizado deve ser igual ao fornecido");
        assertEquals(criado, stock.getCriado(), "Criado deve ser igual ao fornecido");
    }

    @Test
    void testBuilder() {
        Stock stock = Stock.builder()
                .id(id)
                .produtoId(produtoId)
                .quantidade(quantidade)
                .atualizado(atualizado)
                .criado(criado)
                .build();

        assertEquals(id, stock.getId(), "ID deve ser igual ao fornecido no builder");
        assertEquals(produtoId, stock.getProdutoId(), "ProdutoId deve ser igual ao fornecido no builder");
        assertEquals(quantidade, stock.getQuantidade(), "Quantidade deve ser igual ao fornecido no builder");
        assertEquals(atualizado, stock.getAtualizado(), "Atualizado deve ser igual ao fornecido no builder");
        assertEquals(criado, stock.getCriado(), "Criado deve ser igual ao fornecido no builder");
    }

    @Test
    void testSettersAndGetters() {
        Stock stock;
        stock = Stock.builder()
                .id(id)
                .produtoId(produtoId)
                .quantidade(quantidade)
                .atualizado(atualizado)
                .criado(criado)
                .build();

        assertEquals(id, stock.getId(), "Getter de ID deve retornar o valor correto");
        assertEquals(produtoId, stock.getProdutoId(), "Getter de ProdutoId deve retornar o valor correto");
        assertEquals(quantidade, stock.getQuantidade(), "Getter de Quantidade deve retornar o valor correto");
        assertEquals(atualizado, stock.getAtualizado(), "Getter de Atualizado deve retornar o valor correto");
        assertEquals(criado, stock.getCriado(), "Getter de Criado deve retornar o valor correto");
    }

    @Test
    void testNullValuesInBuilder() {
        Stock stock = Stock.builder()
                .id(null)
                .produtoId(null)
                .quantidade(null)
                .atualizado(null)
                .criado(null)
                .build();

        assertNull(stock.getId(), "ID deve ser nulo no builder");
        assertNull(stock.getProdutoId(), "ProdutoId deve ser nulo no builder");
        assertNull(stock.getQuantidade(), "Quantidade deve ser nula no builder");
        assertNull(stock.getAtualizado(), "Atualizado deve ser nulo no builder");
        assertNull(stock.getCriado(), "Criado deve ser nulo no builder");
    }

    @Test
    void testPartialBuilder() {
        Stock stock = Stock.builder()
                .id(id)
                .quantidade(quantidade)
                .build();

        assertEquals(id, stock.getId(), "ID deve ser igual ao fornecido no builder parcial");
        assertNull(stock.getProdutoId(), "ProdutoId deve ser nulo no builder parcial");
        assertEquals(quantidade, stock.getQuantidade(), "Quantidade deve ser igual ao fornecido no builder parcial");
        assertNull(stock.getAtualizado(), "Atualizado deve ser nulo no builder parcial");
        assertNull(stock.getCriado(), "Criado deve ser nulo no builder parcial");
    }

    @Test
    void testApplicationScoped() {
        Stock stock1 = new Stock();
        Stock stock2 = new Stock();
        assertNotSame(stock1, stock2, "Instâncias diferentes devem ser criadas");
    }
}
