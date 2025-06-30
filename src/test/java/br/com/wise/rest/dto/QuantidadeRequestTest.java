package br.com.wise.rest.dto;

import br.com.wise.infrastructure.rest.dto.request.QuantidadeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantidadeRequestTest {

    private QuantidadeRequest quantidadeRequest;
    private final Integer quantidade = 10;

    @BeforeEach
    void setUp() {
        quantidadeRequest = new QuantidadeRequest();
    }

    @Test
    void testDefaultConstructor() {
        assertNull(quantidadeRequest.getQuantidade(), "Quantidade deve ser nula ao usar o construtor padrão");
    }

    @Test
    void testSetterAndGetter() {
        quantidadeRequest.setQuantidade(quantidade);

        assertEquals(quantidade, quantidadeRequest.getQuantidade(), "Getter de quantidade deve retornar o valor correto");
    }

    @Test
    void testSetterWithNull() {
        quantidadeRequest.setQuantidade(null);

        assertNull(quantidadeRequest.getQuantidade(), "Quantidade deve ser nula após setar null");
    }

    @Test
    void testEqualsAndHashCode() {
        QuantidadeRequest request1 = new QuantidadeRequest();
        request1.setQuantidade(quantidade);

        QuantidadeRequest request2 = new QuantidadeRequest();
        request2.setQuantidade(quantidade);

        QuantidadeRequest request3 = new QuantidadeRequest();
        request3.setQuantidade(20);

        assertEquals(request1, request2, "Objetos com mesma quantidade devem ser iguais");
        assertNotEquals(request1, request3, "Objetos com quantidades diferentes não devem ser iguais");
        assertNotEquals(request1, null, "Objeto não deve ser igual a null");
        assertNotEquals(request1, new Object(), "Objeto não deve ser igual a outro tipo");

        assertEquals(request1.hashCode(), request2.hashCode(), "HashCodes devem ser iguais para objetos iguais");
        assertNotEquals(request1.hashCode(), request3.hashCode(), "HashCodes devem ser diferentes para objetos diferentes");
    }

    @Test
    void testToString() {
        quantidadeRequest.setQuantidade(quantidade);

        String toString = quantidadeRequest.toString();
        assertTrue(toString.contains("quantidade=" + quantidade), "toString deve conter a quantidade");
    }

    @Test
    void testToStringWithNull() {
        quantidadeRequest.setQuantidade(null);

        String toString = quantidadeRequest.toString();
        assertTrue(toString.contains("quantidade=null"), "toString deve conter quantidade=null");
    }
}
