package uce.edu.web.api.agenda.infrastructure;

import uce.edu.web.api.agenda.domain.Doctor;
import uce.edu.web.api.agenda.domain.DoctorRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class DoctorRepositoryImpl implements DoctorRepository, PanacheRepositoryBase<Doctor, String> {

    @Override
    public void create(Doctor doctor) {
        persist(doctor);
    }

    @Override
    public Doctor findByCedula(String cedula) {
        return findById(cedula);
    }

    @Override
    public List<Doctor> findAllDoctors() {
        return listAll();
    }
}
