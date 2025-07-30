package br.com.wise.stock_service.gateway.database.jpa.repository;

import br.com.wise.stock_service.gateway.database.jpa.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockEntity, Long> {
    Optional<StockEntity> findByProduto_Id(Long produtoId);
}
