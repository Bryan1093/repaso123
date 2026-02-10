package uce.edu.web.api.agenda.domain;

import java.util.List;

public interface PacienteRepository {
    void create(Paciente paciente);

    Paciente findByCedula(String cedula);

    List<Paciente> findAllPacientes();
}
