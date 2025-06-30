package br.com.wise.application.service;

import br.com.wise.application.usecase.BuscaEstoquePorIdProdutoUseCase;
import br.com.wise.application.usecase.SalvarStockUseCase;
import br.com.wise.converter.StockConverter;
import br.com.wise.domain.Stock;
import br.com.wise.infrastructure.rest.StockController;
import br.com.wise.infrastructure.rest.dto.request.QuantidadeRequest;
import br.com.wise.infrastructure.rest.dto.response.StockResponse;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;

@QuarkusTest
class StockServiceTest {

    @InjectMocks
    private StockController stockResource;

    @Mock
    private BuscaEstoquePorIdProdutoUseCase buscaEstoquePorIdProdutoUseCase;

    @Mock
    private SalvarStockUseCase salvarStockUseCase;

    @Mock
    private StockConverter stockConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveVerificarQuantidade() {
        Long produtoId = 1L;

        Stock stockRetornadoUseCase = Instancio.of(Stock.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), 1204)
                .set(Select.field("id"), 987L)
                .set(Select.field("criado"), ZonedDateTime.now().minusDays(5))
                .set(Select.field("atualizado"), ZonedDateTime.now().minusHours(1))
                .create();

        StockResponse stockResponseEsperado = Instancio.of(StockResponse.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), 1204)
                .set(Select.field("id"), 987L)
                .set(Select.field("criado"), ZonedDateTime.now().minusDays(5))
                .set(Select.field("atualizado"), ZonedDateTime.now().minusHours(1))
                .create();

        when(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).thenReturn(Optional.of(stockRetornadoUseCase));
        when(stockConverter.toResponse(stockRetornadoUseCase)).thenReturn(stockResponseEsperado);


        given()
                .when()
                .get("/estoque/verifica/{id}", produtoId)
                .then()
                .statusCode(200)
                .body("produtoId", equalTo(produtoId.intValue()))
                .body("quantidade", equalTo(stockResponseEsperado.getQuantidade()))
                .body("id", notNullValue())
                .body("criado", notNullValue())
                .body("atualizado", notNullValue());
    }

    @Test
    void deveReporQuantidade() {
        Long produtoId = 1L;
        Integer quantidadeFixa = 5878;

        QuantidadeRequest request = Instancio.of(QuantidadeRequest.class)
                .set(Select.field("quantidade"), quantidadeFixa)
                .create();

        Stock stockToSave = Instancio.of(Stock.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), quantidadeFixa)
                .set(Select.field("id"), 100L)
                .set(Select.field("criado"), ZonedDateTime.now().minusHours(2))
                .set(Select.field("atualizado"), ZonedDateTime.now().minusHours(1))
                .create();

        Stock stockSaved = Instancio.of(Stock.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), quantidadeFixa)
                .set(Select.field("id"), 100L)
                .set(Select.field("criado"), stockToSave.getCriado())
                .set(Select.field("atualizado"), ZonedDateTime.now()) // Atualizado após salvar
                .create();

        StockResponse stockResponseEsperado = Instancio.of(StockResponse.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), quantidadeFixa)
                .set(Select.field("id"), 100L)
                .set(Select.field("criado"), stockToSave.getCriado())
                .set(Select.field("atualizado"), ZonedDateTime.now())
                .create();

        when(stockConverter.toDomain(produtoId, request)).thenReturn(stockToSave);
        when(salvarStockUseCase.execute(stockToSave)).thenReturn(stockSaved);
        when(stockConverter.toResponse(stockSaved)).thenReturn(stockResponseEsperado);


        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/estoque/repor/{id}", produtoId)
                .then()
                .statusCode(200)
                .body("produtoId", equalTo(produtoId.intValue()))
                .body("quantidade", equalTo(stockResponseEsperado.getQuantidade()))
                .body("id", notNullValue())
                .body("atualizado", notNullValue())
                .body("criado", notNullValue());
    }

    @Test
    void deveDarBaixaQuantidade() {
        Long produtoId = 1L;
        Integer quantidadeFixa = 9264;

        QuantidadeRequest request = Instancio.of(QuantidadeRequest.class)
                .set(Select.field("quantidade"), quantidadeFixa)
                .create();

        Stock stockToSave = Instancio.of(Stock.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), quantidadeFixa)
                .set(Select.field("id"), 200L)
                .set(Select.field("criado"), ZonedDateTime.now().minusHours(3))
                .set(Select.field("atualizado"), ZonedDateTime.now().minusHours(2))
                .create();

        Stock stockSaved = Instancio.of(Stock.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), quantidadeFixa)
                .set(Select.field("id"), 200L)
                .set(Select.field("criado"), stockToSave.getCriado())
                .set(Select.field("atualizado"), ZonedDateTime.now()) // Atualizado após salvar
                .create();

        StockResponse stockResponseEsperado = Instancio.of(StockResponse.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), quantidadeFixa)
                .set(Select.field("id"), 200L)
                .set(Select.field("criado"), stockToSave.getCriado())
                .set(Select.field("atualizado"), ZonedDateTime.now())
                .create();

        when(stockConverter.toDomain(produtoId, request)).thenReturn(stockToSave);
        when(salvarStockUseCase.execute(stockToSave)).thenReturn(stockSaved);
        when(stockConverter.toResponse(stockSaved)).thenReturn(stockResponseEsperado);


        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/estoque/baixa/{id}", produtoId)
                .then()
                .statusCode(200)
                .body("produtoId", equalTo(produtoId.intValue()))
                .body("quantidade", equalTo(stockResponseEsperado.getQuantidade()))
                .body("id", notNullValue())
                .body("atualizado", notNullValue())
                .body("criado", notNullValue());
    }

    @Test
    void deveRetornarQuantidadeCorreta() {
        Long produtoId = 1L;

        Stock stockRetornadoUseCase = Instancio.of(Stock.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), 5882)
                .set(Select.field("id"), 300L)
                .set(Select.field("criado"), ZonedDateTime.now().minusDays(1))
                .set(Select.field("atualizado"), ZonedDateTime.now().minusHours(1))
                .create();

        StockResponse stockResponseEsperado = Instancio.of(StockResponse.class)
                .set(Select.field("produtoId"), produtoId)
                .set(Select.field("quantidade"), 5882)
                .set(Select.field("id"), 300L)
                .set(Select.field("criado"), ZonedDateTime.now().minusDays(1))
                .set(Select.field("atualizado"), ZonedDateTime.now().minusHours(1))
                .create();

        when(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).thenReturn(Optional.of(stockRetornadoUseCase));
        when(stockConverter.toResponse(stockRetornadoUseCase)).thenReturn(stockResponseEsperado);

        given()
                .when()
                .get("/estoque/verifica/{id}", produtoId)
                .then()
                .statusCode(200)
                .body("produtoId", equalTo(produtoId.intValue()))
                .body("quantidade", equalTo(stockResponseEsperado.getQuantidade()))
                .body("id", notNullValue())
                .body("atualizado", notNullValue())
                .body("criado", notNullValue());
    }

    @Test
    void deveRetornarNotFoundQuandoEstoqueNaoExiste() {
        Long produtoId = 1L;
        when(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).thenReturn(Optional.empty());

        given()
                .when()
                .get("/estoque/verifica/{id}", produtoId)
                .then()
                .statusCode(404);
    }
}
