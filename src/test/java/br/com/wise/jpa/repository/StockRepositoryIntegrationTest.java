package br.com.wise.jpa.repository;

import br.com.wise.gateway.database.jpa.entity.StockEntity;
import br.com.wise.gateway.database.jpa.repository.StockRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class StockRepositoryIntegrationTest {

    @Inject
    StockRepository stockRepository;

    @Test
    void devePersistirEBuscarEstoque() {
        StockEntity entity = StockEntity.builder()
                .produtoId(5L)
                .quantidade(10)
                .atualizado(ZonedDateTime.now())
                .criado(ZonedDateTime.now())
                .build();

        StockEntity salvo = stockRepository.save(entity);

        Optional<StockEntity> encontrado = stockRepository.findByProdutoId(5L);

        assertTrue(encontrado.isPresent());
        assertEquals(5L, encontrado.get().getProdutoId());
    }
}
