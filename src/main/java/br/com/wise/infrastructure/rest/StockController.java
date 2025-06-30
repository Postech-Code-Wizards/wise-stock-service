package br.com.wise.infrastructure.rest;

import br.com.wise.application.service.StockService;
import br.com.wise.infrastructure.rest.dto.request.QuantidadeRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

@Path("/estoque")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GET
    @Path("/verifica/{produtoId}")
    public Response verificaQuantidade(@PathParam("produtoId") Long produtoId) {
        return Response.ok(stockService.verificaQuantidade(produtoId)).build();
    }

    @PUT
    @Path("/repor/{produtoId}")
    public Response reporQuantidade(@PathParam("produtoId") Long produtoId, QuantidadeRequest quantidade) {
        return Response.ok(stockService.reporQuantidade(produtoId, quantidade)).build();
    }

    @PUT
    @Path("/baixa/{produtoId}")
    public Response baixaQuantidade(@PathParam("produtoId") Long produtoId, QuantidadeRequest quantidade) {
        return Response.ok(stockService.baixaQuantidade(produtoId, quantidade)).build();
    }
}
