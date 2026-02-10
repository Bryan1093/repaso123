package uce.edu.web.api.inscripcion.interfaces;

import java.util.List;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uce.edu.web.api.inscripcion.application.EstudianteService;
import uce.edu.web.api.inscripcion.domain.Estudiante;

@Path("/estudiantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EstudianteResource {

    @Inject
    EstudianteService estudianteService;

    @POST
    @RolesAllowed({ "user", "admin" })
    public Response guardarEstudiante(Estudiante estudiante) {
        try {
            estudianteService.guardar(estudiante);
            EstudianteRepresentation representation = toRepresentation(estudiante);
            return Response.status(Response.Status.CREATED).entity(representation).build();
        } catch (Exception e) {
            e.printStackTrace(); // Log error in server console
            return Response.serverError().entity("Error al guardar estudiante: " + e.getMessage()).build();
        }
    }

    @GET
    @RolesAllowed({ "user", "admin" })
    public List<EstudianteRepresentation> listarEstudiantes() {
        List<Estudiante> estudiantes = estudianteService.listarTodos();
        return estudiantes.stream()
                .map(this::toRepresentation)
                .collect(java.util.stream.Collectors.toList());
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({ "user", "admin" })
    public Response obtenerEstudiante(@PathParam("id") Long id) {
        Estudiante estudiante = estudianteService.buscarPorId(id);

        if (estudiante == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        EstudianteRepresentation representation = toRepresentation(estudiante);
        return Response.ok(representation).build();
    }

    private EstudianteRepresentation toRepresentation(Estudiante estudiante) {
        EstudianteRepresentation representation = new EstudianteRepresentation(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getApellido(),
                estudiante.getCedula());

        representation.setLinks(java.util.Arrays.asList(
                new EstudianteRepresentation.Link("self", "/matricula/api/v1.0/estudiantes/" + estudiante.getId())));

        return representation;
    }
}
