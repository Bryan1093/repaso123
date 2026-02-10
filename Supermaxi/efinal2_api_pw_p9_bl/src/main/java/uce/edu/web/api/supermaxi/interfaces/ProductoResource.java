package uce.edu.web.api.supermaxi.interfaces;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import uce.edu.web.api.supermaxi.application.ProductoService;
import uce.edu.web.api.supermaxi.domain.Producto;
import jakarta.annotation.security.RolesAllowed;

@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoResource {

    @Inject
    ProductoService service;

    @POST
    @RolesAllowed("admin")
    public Response ingresarProducto(Producto p) {
        service.ingresarProducto(p);
        return Response.created(UriBuilder.fromResource(ProductoResource.class)
                .path(p.getCodigoBarras()).build()).build();
    }

    @GET
    @Path("/{codigo}")
    @RolesAllowed("admin")
    public Response buscarPorCodigo(@PathParam("codigo") String codigo) {
        Producto p = service.buscarPorCodigo(codigo);
        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(p)
                .link(UriBuilder.fromResource(ProductoResource.class).path(codigo).build(), "self")
                .link(UriBuilder.fromResource(VentaResource.class).build(), "ventas")
                .build();
    }
}
