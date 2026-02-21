package uce.edu.web.api.agenda.infrastructure;

import uce.edu.web.api.agenda.domain.Paciente;
import uce.edu.web.api.agenda.domain.PacienteRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PacienteRepositoryImpl implements PacienteRepository, PanacheRepositoryBase<Paciente, Integer> {

    @Override
    public void create(Paciente paciente) {
        persist(paciente);
    }

    @Override
    public void createAndFlush(Paciente paciente) {
        persistAndFlush(paciente);
    }

    @Override
    public Paciente findById(Integer id) {
        return findByIdOptional(id).orElse(null);
    }

    @Override
    public Paciente findByCedula(String cedula) {
        return find("cedula", cedula).firstResult();
    }

    @Override
    public List<Paciente> findAllPacientes() {
        return listAll();
    }
}
