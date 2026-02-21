package uce.edu.web.api.agenda.interfaces;

import uce.edu.web.api.agenda.domain.Paciente;
import uce.edu.web.api.agenda.domain.PacienteRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@jakarta.annotation.security.RolesAllowed("admin")
public class PacienteResource {

    @Inject
    PacienteRepository pacienteRepository;

    @Context
    UriInfo uriInfo;

    @POST
    @Transactional
    public Response create(Paciente paciente) {
        paciente.setId(null); // Asegurar que sea nuevo
        pacienteRepository.createAndFlush(paciente);
        // Forzar flush para obtener el ID generado
        // Aunque Panache persist() suele hacerlo, esto es más seguro para el ID
        EntityResponse<Paciente> response = new EntityResponse<>(paciente);

        String idStr = paciente.getId() != null ? String.valueOf(paciente.getId()) : "unknown";
        String self = uriInfo.getAbsolutePathBuilder().path(idStr).build().toString();

        response.addLink("self", self, "GET");
        response.addLink("collection", uriInfo.getAbsolutePathBuilder().build().toString(), "GET");
        return Response.created(URI.create(self)).entity(response).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Integer id) {
        Paciente paciente = pacienteRepository.findById(id);
        if (paciente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        EntityResponse<Paciente> response = new EntityResponse<>(paciente);
        response.addLink("self", uriInfo.getAbsolutePath().toString(), "GET");
        response.addLink("collection", uriInfo.getBaseUriBuilder().path(PacienteResource.class).build().toString(),
                "GET");
        return Response.ok(response).build();
    }

    @GET
    public List<EntityResponse<Paciente>> getAll() {
        List<Paciente> pacientes = pacienteRepository.findAllPacientes();
        List<EntityResponse<Paciente>> result = new ArrayList<>();
        for (Paciente p : pacientes) {
            EntityResponse<Paciente> resp = new EntityResponse<>(p);
            if (p.getId() != null) {
                String self = uriInfo.getAbsolutePathBuilder().path(String.valueOf(p.getId())).build().toString();
                resp.addLink("self", self, "GET");
            }
            result.add(resp);
        }
        return result;
    }
}
