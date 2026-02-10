package uce.edu.web.api.agenda.infrastructure;

import uce.edu.web.api.agenda.domain.Cita;
import uce.edu.web.api.agenda.domain.CitaRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CitaRepositoryImpl implements CitaRepository, PanacheRepository<Cita> {

    @Override
    public void create(Cita cita) {
        persist(cita);
    }

    @Override
    public List<Cita> findAllCitas() {
        return listAll();
    }

    @Override
    public Cita findById(Long id) {
        return PanacheRepository.super.findById(id);
    }
}
