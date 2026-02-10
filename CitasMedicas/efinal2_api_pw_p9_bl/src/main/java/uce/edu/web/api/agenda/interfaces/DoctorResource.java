package uce.edu.web.api.agenda.interfaces;

import uce.edu.web.api.agenda.domain.Doctor;
import uce.edu.web.api.agenda.domain.DoctorRepository;
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

@Path("/doctores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@jakarta.annotation.security.RolesAllowed("admin")
public class DoctorResource {

    @Inject
    DoctorRepository doctorRepository;

    @Context
    UriInfo uriInfo;

    @POST
    @Transactional
    public Response create(Doctor doctor) {
        doctorRepository.create(doctor);
        EntityResponse<Doctor> response = new EntityResponse<>(doctor);
        String self = uriInfo.getAbsolutePathBuilder().path(doctor.getCedula()).build().toString();
        response.addLink("self", self, "GET");
        response.addLink("collection", uriInfo.getAbsolutePathBuilder().build().toString(), "GET");
        return Response.created(URI.create(self)).entity(response).build();
    }

    @GET
    @Path("/{cedula}")
    public Response getById(@PathParam("cedula") String cedula) {
        Doctor doctor = doctorRepository.findByCedula(cedula);
        if (doctor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        EntityResponse<Doctor> response = new EntityResponse<>(doctor);
        response.addLink("self", uriInfo.getAbsolutePath().toString(), "GET");
        response.addLink("collection", uriInfo.getBaseUriBuilder().path(DoctorResource.class).build().toString(),
                "GET");
        return Response.ok(response).build();
    }

    @GET
    public List<EntityResponse<Doctor>> getAll() {
        List<Doctor> doctors = doctorRepository.findAllDoctors();
        List<EntityResponse<Doctor>> result = new ArrayList<>();
        for (Doctor d : doctors) {
            EntityResponse<Doctor> resp = new EntityResponse<>(d);
            String self = uriInfo.getAbsolutePathBuilder().path(d.getCedula()).build().toString();
            resp.addLink("self", self, "GET");
            result.add(resp);
        }
        return result;
    }
}
