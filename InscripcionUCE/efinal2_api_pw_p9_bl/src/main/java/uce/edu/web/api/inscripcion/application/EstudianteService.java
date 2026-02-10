package uce.edu.web.api.inscripcion.application;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.web.api.inscripcion.domain.Estudiante;
import uce.edu.web.api.inscripcion.infrastructure.EstudianteRepository;

@ApplicationScoped
public class EstudianteService {

    @Inject
    EstudianteRepository estudianteRepository;

    @Transactional
    public void guardar(Estudiante estudiante) {
        estudianteRepository.persist(estudiante);
    }

    public List<Estudiante> listarTodos() {
        return estudianteRepository.listAll();
    }

    public Estudiante buscarPorId(Long id) {
        return estudianteRepository.findById(id);
    }
}
