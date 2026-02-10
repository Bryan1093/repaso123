package uce.edu.web.api.agenda.domain;

import java.util.List;

public interface DoctorRepository {
    void create(Doctor doctor);

    Doctor findByCedula(String cedula);

    List<Doctor> findAllDoctors();
}
