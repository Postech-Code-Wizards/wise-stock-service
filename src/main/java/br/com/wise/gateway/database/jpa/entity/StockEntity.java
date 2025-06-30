package br.com.wise.gateway.database.jpa.entity;

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

    @Column(name = "produto_id",  nullable = false)
    private Long produtoId;

    @Column(name = "quantidade",  nullable = false)
    private Integer quantidade;

    @Column(name = "atualizado")
    private ZonedDateTime atualizado;

    @Column(name = "criado")
    private ZonedDateTime criado;
}
