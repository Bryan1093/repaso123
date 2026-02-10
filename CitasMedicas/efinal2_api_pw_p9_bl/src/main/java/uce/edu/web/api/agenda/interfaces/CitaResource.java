package uce.edu.web.api.agenda.interfaces;

import uce.edu.web.api.agenda.domain.Cita;
import uce.edu.web.api.agenda.domain.CitaRepository;
import uce.edu.web.api.agenda.domain.Doctor;
import uce.edu.web.api.agenda.domain.DoctorRepository;
import uce.edu.web.api.agenda.domain.Paciente;
import uce.edu.web.api.agenda.domain.PacienteRepository;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;

@Path("/citas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@jakarta.annotation.security.RolesAllowed({ "user", "admin" })
public class CitaResource {

    @Inject
    CitaRepository citaRepository;

    @Inject
    DoctorRepository doctorRepository;

    @Inject
    PacienteRepository pacienteRepository;

    public static class CitaRequest {
        public String doctorCedula;
        public String pacienteCedula;
        public LocalDateTime fechaCita;
    }

    @POST
    @Transactional
    public Response create(CitaRequest request) {
        Doctor doctor = doctorRepository.findByCedula(request.doctorCedula);
        if (doctor == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Doctor no encontrado").build();
        }

        Paciente paciente = pacienteRepository.findByCedula(request.pacienteCedula);
        if (paciente == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Paciente no encontrado").build();
        }

        Cita cita = new Cita();
        cita.setDoctor(doctor);
        cita.setPaciente(paciente);
        cita.setFechaCita(request.fechaCita);

        citaRepository.create(cita);
        return Response.status(Response.Status.CREATED).entity(cita).build();
    }

    @GET
    public List<Cita> getAll() {
        return citaRepository.findAllCitas();
    }
}
