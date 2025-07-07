package br.com.wise.stock_service.infrastructure.rest;

import br.com.wise.stock_service.application.service.StockService;
import br.com.wise.stock_service.converter.StockConverter;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StockService stockService;

    @MockitoBean
    private StockConverter stockConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class EndpointsContext {

        @Test
        @DisplayName("Should retrieve stock by ID successfully")
        void shouldVerificaQuantidadeSuccessfully() throws Exception {
            Long produtoId = 1L;
            StockResponse stockResponse = Instancio.create(StockResponse.class);

            given(stockService.verificaQuantidade(produtoId)).willReturn(stockResponse);
            given(stockConverter.toResponse(any(Stock.class))).willReturn(stockResponse);

            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/estoque/verifica/{produtoId}", produtoId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                    .value(stockResponse.getId()));

            verify(stockService, times(1)).verificaQuantidade(any());
        }

        @Test
        @DisplayName("Should repor stock successfully")
        void shouldReporQuantidadeSuccessfully() throws Exception {
            Long produtoId = 2L;
            QuantidadeRequest request = new QuantidadeRequest(10);
            StockResponse stockResponse = Instancio.create(StockResponse.class);

            given(stockService.reporQuantidade(any(), any())).willReturn(stockResponse);

            mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/estoque/repor/{produtoId}", produtoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                            .value(stockResponse.getId()));

            verify(stockService, times(1)).reporQuantidade(produtoId, request);
        }

        @Test
        @DisplayName("Should baixa stock successfully")
        void shouldBaixaQuantidadeSuccessfully() throws Exception {
            Long produtoId = 3L;
            QuantidadeRequest request = new QuantidadeRequest(5);
            StockResponse stockResponse = Instancio.create(StockResponse.class);

            given(stockService.baixaQuantidade(any(), any())).willReturn(stockResponse);

            mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/estoque/baixa/{produtoId}", produtoId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                            .value(stockResponse.getId()));

            verify(stockService, times(1)).baixaQuantidade(produtoId, request);
        }
    }
}
