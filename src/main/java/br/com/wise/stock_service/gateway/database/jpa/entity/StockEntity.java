package br.com.wise.stock_service.gateway.database.jpa.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "stock")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class StockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "produto_id",  nullable = false)
    private ProductEntity produto;

    @Column(name = "quantidade",  nullable = false)
    private Integer quantidade;

    @Column(name = "atualizado")
    private ZonedDateTime atualizado;

    @Column(name = "criado")
    private ZonedDateTime criado;
}
