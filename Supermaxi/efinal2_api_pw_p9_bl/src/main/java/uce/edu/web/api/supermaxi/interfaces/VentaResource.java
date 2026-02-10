package uce.edu.web.api.supermaxi.interfaces;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.GET;
import java.util.List;
import uce.edu.web.api.supermaxi.domain.Venta;
import uce.edu.web.api.supermaxi.application.VentaService;
import jakarta.annotation.security.RolesAllowed;

@Path("/ventas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VentaResource {

    @Inject
    VentaService service;

    @POST
    @RolesAllowed({ "admin", "user" })
    public Response realizarVenta(VentaDTO dto) {
        try {
            service.realizarVenta(dto);
            return Response.created(jakarta.ws.rs.core.UriBuilder.fromResource(VentaResource.class)
                    .path(dto.getNumeroVenta()).build()).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @RolesAllowed("admin")
    public Response listarVentas() {
        List<Venta> ventas = service.buscarTodas();
        return Response.ok(ventas)
                .link(jakarta.ws.rs.core.UriBuilder.fromResource(VentaResource.class).build(), "self")
                .build();
    }
}
