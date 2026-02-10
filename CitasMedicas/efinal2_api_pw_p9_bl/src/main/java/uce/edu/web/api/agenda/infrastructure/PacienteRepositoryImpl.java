package uce.edu.web.api.agenda.infrastructure;

import uce.edu.web.api.agenda.domain.Paciente;
import uce.edu.web.api.agenda.domain.PacienteRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PacienteRepositoryImpl implements PacienteRepository, PanacheRepositoryBase<Paciente, String> {

    @Override
    public void create(Paciente paciente) {
        persist(paciente);
    }

    @Override
    public Paciente findByCedula(String cedula) {
        return findById(cedula);
    }

    @Override
    public List<Paciente> findAllPacientes() {
        return listAll();
    }
}
