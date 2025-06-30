package br.com.wise.gateway.database.jpa.repository;

import br.com.wise.gateway.database.jpa.entity.StockEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class StockRepository implements PanacheRepository<StockEntity> {

    public Optional<StockEntity> findByProdutoId(Long produtoId) {
        return find("produtoId = ?1", produtoId).firstResultOptional();
    }

    @Transactional
    public StockEntity save(StockEntity stockEntity) {
        if (stockEntity.getId() != null) {
            return getEntityManager().merge(stockEntity);
        } else {
            persist(stockEntity);
            return stockEntity;
        }
    }
}
