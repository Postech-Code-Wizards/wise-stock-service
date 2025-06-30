package br.com.wise.jpa;

import br.com.wise.converter.StockConverter;
import br.com.wise.domain.Stock;
import br.com.wise.gateway.database.jpa.StockJpaGateway;
import br.com.wise.gateway.database.jpa.entity.StockEntity;
import br.com.wise.gateway.database.jpa.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class StockJpaGatewayTest {

    @Mock
    private StockConverter stockConverter;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockJpaGateway stockJpaGateway;

    private AutoCloseable mocks;

    @BeforeEach
    void setup() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarStockQuandoProdutoIdExistir() {
        Long produtoId = 1L;
        StockEntity entity = StockEntity.builder()
                .id(1L)
                .produtoId(produtoId)
                .quantidade(10)
                .criado(ZonedDateTime.now())
                .atualizado(ZonedDateTime.now())
                .build();

        Stock domain = Stock.builder()
                .id(1L)
                .produtoId(produtoId)
                .quantidade(10)
                .criado(ZonedDateTime.now())
                .atualizado(ZonedDateTime.now())
                .build();

        when(stockRepository.findByProdutoId(produtoId)).thenReturn(Optional.of(entity));
        when(stockConverter.toDomain(Optional.of(entity))).thenReturn(Optional.of(domain));

        Optional<Stock> result = stockJpaGateway.buscaStockPorIdProduto(produtoId);

        assertTrue(result.isPresent());
        assertEquals(produtoId, result.get().getProdutoId());
    }

    @Test
    void deveSalvarEConverterCorretamente() {
        Stock domain = Stock.builder()
                .produtoId(1L)
                .quantidade(10)
                .criado(ZonedDateTime.now())
                .atualizado(ZonedDateTime.now())
                .build();

        StockEntity entity = StockEntity.builder()
                .produtoId(1L)
                .quantidade(10)
                .criado(ZonedDateTime.now())
                .atualizado(ZonedDateTime.now())
                .build();

        when(stockConverter.toEntity(domain)).thenReturn(entity);
        when(stockRepository.save(entity)).thenReturn(entity);
        when(stockConverter.toDomain(entity)).thenReturn(domain);

        Stock result = stockJpaGateway.salvar(domain);

        assertNotNull(result);
        assertEquals(domain.getProdutoId(), result.getProdutoId());
    }
}
