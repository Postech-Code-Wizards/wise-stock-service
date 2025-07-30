package br.com.wise.stock_service.infrastructure.rest;

import br.com.wise.stock_service.application.usecase.BaixarEstoqueUseCase;
import br.com.wise.stock_service.application.usecase.BuscaEstoquePorIdProdutoUseCase;
import br.com.wise.stock_service.application.usecase.ReporEstoqueUseCase;
import br.com.wise.stock_service.converter.StockConverter;
import br.com.wise.stock_service.domain.Product;
import br.com.wise.stock_service.domain.Stock;
import br.com.wise.stock_service.infrastructure.rest.dto.request.QuantidadeRequest;
import br.com.wise.stock_service.infrastructure.rest.dto.response.StockResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZonedDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StockConverter stockConverter;

    @MockitoBean
    private ReporEstoqueUseCase reporEstoqueUseCase;

    @MockitoBean
    private BaixarEstoqueUseCase baixarEstoqueUseCase;

    @MockitoBean
    private BuscaEstoquePorIdProdutoUseCase buscaEstoquePorIdProdutoUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class EndpointsContext {

        @Test
        @DisplayName("Should retrieve stock by ID successfully")
        void shouldVerificaQuantidadeSuccessfully() throws Exception {
            Long produtoId = 1L;

            Stock stock = new Stock(1L, new Product(produtoId), 10, ZonedDateTime.now(), ZonedDateTime.now());
            StockResponse stockResponse = Instancio.create(StockResponse.class);

            given(buscaEstoquePorIdProdutoUseCase.execute(produtoId)).willReturn(stock);
            given(stockConverter.toResponse(stock)).willReturn(stockResponse);

            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/estoque/verifica/{produtoId}", produtoId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(stockResponse.getId()));

            verify(buscaEstoquePorIdProdutoUseCase, times(1)).execute(produtoId);
            verify(stockConverter, times(1)).toResponse(stock);
        }

        @Test
        @DisplayName("Should repor stock successfully")
        void shouldReporQuantidadeSuccessfully() throws Exception {
            Long produtoId = 2L;
            QuantidadeRequest request = new QuantidadeRequest(10);

            Stock stock = new Stock(1L, new Product(produtoId), 20, ZonedDateTime.now(), ZonedDateTime.now());
            StockResponse stockResponse = Instancio.create(StockResponse.class);

            given(stockConverter.toDomain(produtoId, request.getQuantidade())).willReturn(stock);
            given(reporEstoqueUseCase.execute(stock)).willReturn(stock);
            given(stockConverter.toResponse(stock)).willReturn(stockResponse);

            mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/estoque/repor/{produtoId}", produtoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(stockResponse.getId()));

            verify(reporEstoqueUseCase, times(1)).execute(stock);
            verify(stockConverter, times(1)).toResponse(stock);
        }

        @Test
        @DisplayName("Should baixa stock successfully")
        void shouldBaixaQuantidadeSuccessfully() throws Exception {
            Long produtoId = 3L;
            QuantidadeRequest request = new QuantidadeRequest(5);

            Stock stock = new Stock(1L, new Product(produtoId), 5, ZonedDateTime.now(), ZonedDateTime.now());
            StockResponse stockResponse = Instancio.create(StockResponse.class);

            given(stockConverter.toDomain(produtoId, request.getQuantidade())).willReturn(stock);
            given(baixarEstoqueUseCase.execute(stock)).willReturn(stock);
            given(stockConverter.toResponse(stock)).willReturn(stockResponse);

            mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/estoque/baixa/{produtoId}", produtoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(stockResponse.getId()));

            verify(baixarEstoqueUseCase, times(1)).execute(stock);
            verify(stockConverter, times(1)).toResponse(stock);
        }
    }
}
