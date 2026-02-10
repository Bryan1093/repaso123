package uce.edu.web.api.agenda.domain;

import java.util.List;

public interface CitaRepository {
    void create(Cita cita);

    Cita findById(Long id);

    List<Cita> findAllCitas();
}
