package br.com.wise.stock_service.converter;

import br.com.wise.stock_service.infrastructure.rest.dto.request.StockItens;
import br.com.wise.stock_service.infrastructure.rest.dto.request.StockRequestMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StockItensConverter {

    public StockItens toDomain(StockRequestMessage source) {
        return new StockItens(
                source.getPedidoId(),
                source.getProdutoId(),
                source.getQuantidade()
        );
    }

    public List<StockItens> toDomainList(List<StockRequestMessage> messages) {
        return messages.stream().map(this::toDomain).toList();
    }
}
