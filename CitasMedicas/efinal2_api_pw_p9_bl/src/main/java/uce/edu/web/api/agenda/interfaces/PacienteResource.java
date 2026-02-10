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
        pacienteRepository.create(paciente);
        EntityResponse<Paciente> response = new EntityResponse<>(paciente);
        String self = uriInfo.getAbsolutePathBuilder().path(paciente.getCedula()).build().toString();
        response.addLink("self", self, "GET");
        response.addLink("collection", uriInfo.getAbsolutePathBuilder().build().toString(), "GET");
        return Response.created(URI.create(self)).entity(response).build();
    }

    @GET
    @Path("/{cedula}")
    public Response getById(@PathParam("cedula") String cedula) {
        Paciente paciente = pacienteRepository.findByCedula(cedula);
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
            String self = uriInfo.getAbsolutePathBuilder().path(p.getCedula()).build().toString();
            resp.addLink("self", self, "GET");
            result.add(resp);
        }
        return result;
    }
}
