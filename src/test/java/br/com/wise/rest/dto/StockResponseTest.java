package br.com.wise.rest.dto;

import br.com.wise.infrastructure.rest.dto.response.StockResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StockResponseTest {

    private StockResponse stockResponse;
    private final Long id = 1L;
    private final Long produtoId = 100L;
    private final Integer quantidade = 10;
    private final ZonedDateTime criado = ZonedDateTime.now().minusDays(1);
    private final ZonedDateTime atualizado = ZonedDateTime.now();

    @BeforeEach
    void setUp() {
        stockResponse = new StockResponse();
    }

    @Test
    void testNoArgsConstructor() {
        assertNull(stockResponse.getId(), "ID deve ser nulo");
        assertNull(stockResponse.getProdutoId(), "ProdutoId deve ser nulo");
        assertNull(stockResponse.getQuantidade(), "Quantidade deve ser nula");
        assertNull(stockResponse.getCriado(), "Criado deve ser nulo");
        assertNull(stockResponse.getAtualizado(), "Atualizado deve ser nulo");
    }

    @Test
    void testAllArgsConstructor() {
        StockResponse stockResponse = new StockResponse(id, produtoId, quantidade, atualizado, criado);

        assertEquals(id, stockResponse.getId(), "ID deve ser igual ao fornecido");
        assertEquals(produtoId, stockResponse.getProdutoId(), "ProdutoId deve ser igual ao fornecido");
        assertEquals(quantidade, stockResponse.getQuantidade(), "Quantidade deve ser igual ao fornecido");
        assertEquals(atualizado, stockResponse.getAtualizado(), "Atualizado deve ser igual ao fornecido");
        assertEquals(criado, stockResponse.getCriado(), "Criado deve ser igual ao fornecido");
    }

    @Test
    void testBuilder() {
        StockResponse stockResponse = StockResponse.builder()
                .id(id)
                .produtoId(produtoId)
                .quantidade(quantidade)
                .atualizado(atualizado)
                .criado(criado)
                .build();

        assertEquals(id, stockResponse.getId(), "ID deve ser igual ao fornecido no builder");
        assertEquals(produtoId, stockResponse.getProdutoId(), "ProdutoId deve ser igual ao fornecido no builder");
        assertEquals(quantidade, stockResponse.getQuantidade(), "Quantidade deve ser igual ao fornecido no builder");
        assertEquals(atualizado, stockResponse.getAtualizado(), "Atualizado deve ser igual ao fornecido no builder");
        assertEquals(criado, stockResponse.getCriado(), "Criado deve ser igual ao fornecido no builder");
    }

    @Test
    void testSettersAndGetters() {
        stockResponse.setId(id);
        stockResponse.setProdutoId(produtoId);
        stockResponse.setQuantidade(quantidade);
        stockResponse.setAtualizado(atualizado);
        stockResponse.setCriado(criado);

        assertEquals(id, stockResponse.getId(), "Getter de ID deve retornar o valor correto");
        assertEquals(produtoId, stockResponse.getProdutoId(), "Getter de ProdutoId deve retornar o valor correto");
        assertEquals(quantidade, stockResponse.getQuantidade(), "Getter de Quantidade deve retornar o valor correto");
        assertEquals(atualizado, stockResponse.getAtualizado(), "Getter de Atualizado deve retornar o valor correto");
        assertEquals(criado, stockResponse.getCriado(), "Getter de Criado deve retornar o valor correto");
    }

    @Test
    void testNullValuesInBuilder() {
        StockResponse stockResponse = StockResponse.builder()
                .id(null)
                .produtoId(null)
                .quantidade(null)
                .atualizado(null)
                .criado(null)
                .build();

        assertNull(stockResponse.getId(), "ID deve ser nulo no builder");
        assertNull(stockResponse.getProdutoId(), "ProdutoId deve ser nulo no builder");
        assertNull(stockResponse.getQuantidade(), "Quantidade deve ser nula no builder");
        assertNull(stockResponse.getAtualizado(), "Atualizado deve ser nulo no builder");
        assertNull(stockResponse.getCriado(), "Criado deve ser nulo no builder");
    }

    @Test
    void testPartialBuilder() {
        StockResponse stockResponse = StockResponse.builder()
                .id(id)
                .quantidade(quantidade)
                .build();

        assertEquals(id, stockResponse.getId(), "ID deve ser igual ao fornecido no builder parcial");
        assertNull(stockResponse.getProdutoId(), "ProdutoId deve ser nulo no builder parcial");
        assertEquals(quantidade, stockResponse.getQuantidade(), "Quantidade deve ser igual ao fornecido no builder parcial");
        assertNull(stockResponse.getAtualizado(), "Atualizado deve ser nulo no builder parcial");
        assertNull(stockResponse.getCriado(), "Criado deve ser nulo no builder parcial");
    }

    @Test
    void testEqualsAndHashCode() {
        StockResponse stock1 = StockResponse.builder()
                .id(id)
                .produtoId(produtoId)
                .quantidade(quantidade)
                .atualizado(atualizado)
                .criado(criado)
                .build();

        StockResponse stock2 = StockResponse.builder()
                .id(id)
                .produtoId(produtoId)
                .quantidade(quantidade)
                .atualizado(atualizado)
                .criado(criado)
                .build();

        StockResponse stock3 = StockResponse.builder()
                .id(2L)
                .produtoId(produtoId)
                .quantidade(quantidade)
                .atualizado(atualizado)
                .criado(criado)
                .build();

        assertEquals(stock1, stock2, "Objetos com mesmos valores devem ser iguais");
        assertNotEquals(stock1, stock3, "Objetos com IDs diferentes não devem ser iguais");
        assertNotEquals(stock1, null, "Objeto não deve ser igual a null");
        assertNotEquals(stock1, new Object(), "Objeto não deve ser igual a outro tipo");

        assertEquals(stock1.hashCode(), stock2.hashCode(), "HashCodes devem ser iguais para objetos iguais");
        assertNotEquals(stock1.hashCode(), stock3.hashCode(), "HashCodes devem ser diferentes para objetos diferentes");
    }

    @Test
    void testToString() {
        StockResponse stockResponse = StockResponse.builder()
                .id(id)
                .produtoId(produtoId)
                .quantidade(quantidade)
                .atualizado(atualizado)
                .criado(criado)
                .build();

        String toString = stockResponse.toString();
        assertTrue(toString.contains("id=" + id), "toString deve conter o ID");
        assertTrue(toString.contains("produtoId=" + produtoId), "toString deve conter o ProdutoId");
        assertTrue(toString.contains("quantidade=" + quantidade), "toString deve conter a Quantidade");
        assertTrue(toString.contains("atualizado=" + atualizado), "toString deve conter o Atualizado");
        assertTrue(toString.contains("criado=" + criado), "toString deve conter o Criado");
    }
}
