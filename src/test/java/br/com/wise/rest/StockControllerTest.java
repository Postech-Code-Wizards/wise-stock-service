package br.com.wise.rest;

import br.com.wise.application.service.StockService;
import br.com.wise.infrastructure.rest.dto.request.QuantidadeRequest;
import br.com.wise.infrastructure.rest.dto.response.StockResponse;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class StockControllerTest {

    @InjectMock
    StockService stockService;

    @BeforeEach
    void setUp() {
        RestAssured.defaultParser = io.restassured.parsing.Parser.JSON;
    }

    @Test
    void deveVerificarQuantidade() {
        Long produtoId = 1L;

        StockResponse stockResponse = Instancio.of(StockResponse.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), 1204)
                .set(Select.field("id"), 987L)
                .set(Select.field("criado"), ZonedDateTime.now().minusDays(5))
                .set(Select.field("atualizado"), ZonedDateTime.now().minusHours(1))
                .create();

        when(stockService.verificaQuantidade(produtoId)).thenReturn(stockResponse);

        given()
                .when()
                .get("/estoque/verifica/{id}", produtoId)
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("produtoId", equalTo(produtoId.intValue()))
                .body("quantidade", equalTo(stockResponse.getQuantidade()))
                .body("id", notNullValue())
                .body("criado", notNullValue())
                .body("atualizado", notNullValue());

        verify(stockService, times(1)).verificaQuantidade(produtoId);
    }

    @Test
    void deveReporQuantidade() {
        Long produtoId = 1L;
        Integer quantidadeParaRepor = 5878;

        QuantidadeRequest request = Instancio.of(QuantidadeRequest.class)
                .set(Select.field("quantidade"), quantidadeParaRepor)
                .create();

        StockResponse stockAtualizadoResponse = Instancio.of(StockResponse.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), quantidadeParaRepor)
                .set(Select.field("id"), 100L)
                .set(Select.field("criado"), ZonedDateTime.now().minusHours(2))
                .set(Select.field("atualizado"), ZonedDateTime.now())
                .create();

        when(stockService.reporQuantidade(eq(produtoId), any(QuantidadeRequest.class))).thenReturn(stockAtualizadoResponse);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/estoque/repor/{id}", produtoId)
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("produtoId", equalTo(produtoId.intValue()))
                .body("quantidade", equalTo(stockAtualizadoResponse.getQuantidade()))
                .body("id", notNullValue())
                .body("atualizado", notNullValue())
                .body("criado", notNullValue());

        verify(stockService, times(1)).reporQuantidade(eq(produtoId), any(QuantidadeRequest.class));
    }

    @Test
    void deveDarBaixaQuantidade() {
        Long produtoId = 1L;
        Integer quantidadeParaBaixa = 9264;

        QuantidadeRequest request = Instancio.of(QuantidadeRequest.class)
                .set(Select.field("quantidade"), quantidadeParaBaixa)
                .create();

        StockResponse stockAtualizadoResponse = Instancio.of(StockResponse.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), quantidadeParaBaixa)
                .set(Select.field("id"), 200L)
                .set(Select.field("criado"), ZonedDateTime.now().minusHours(3))
                .set(Select.field("atualizado"), ZonedDateTime.now())
                .create();

        when(stockService.baixaQuantidade(eq(produtoId), any(QuantidadeRequest.class))).thenReturn(stockAtualizadoResponse);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/estoque/baixa/{id}", produtoId)
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("produtoId", equalTo(produtoId.intValue()))
                .body("quantidade", equalTo(stockAtualizadoResponse.getQuantidade()))
                .body("id", notNullValue())
                .body("atualizado", notNullValue())
                .body("criado", notNullValue());

        verify(stockService, times(1)).baixaQuantidade(eq(produtoId), any(QuantidadeRequest.class));
    }

    @Test
    void deveRetornarQuantidadeCorreta() {
        Long produtoId = 1L;

        StockResponse stockResponse = Instancio.of(StockResponse.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), 5882)
                .set(Select.field("id"), 300L)
                .set(Select.field("criado"), ZonedDateTime.now().minusDays(1))
                .set(Select.field("atualizado"), ZonedDateTime.now().minusHours(1))
                .create();

        when(stockService.verificaQuantidade(produtoId)).thenReturn(stockResponse);

        given()
                .when()
                .get("/estoque/verifica/{id}", produtoId)
                .then()
                .log().all()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("produtoId", equalTo(produtoId.intValue()))
                .body("quantidade", equalTo(stockResponse.getQuantidade()))
                .body("id", notNullValue())
                .body("atualizado", notNullValue())
                .body("criado", notNullValue());

        verify(stockService, times(1)).verificaQuantidade(produtoId);
    }

}
