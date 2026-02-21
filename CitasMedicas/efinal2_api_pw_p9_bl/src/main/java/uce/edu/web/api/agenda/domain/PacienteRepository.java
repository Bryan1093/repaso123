package uce.edu.web.api.agenda.domain;

import java.util.List;

public interface PacienteRepository {
    void create(Paciente paciente);

    void createAndFlush(Paciente paciente);

    Paciente findById(Integer id);

    Paciente findByCedula(String cedula);

    List<Paciente> findAllPacientes();
}
