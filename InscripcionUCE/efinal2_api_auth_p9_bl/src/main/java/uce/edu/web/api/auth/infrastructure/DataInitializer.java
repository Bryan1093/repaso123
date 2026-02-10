package uce.edu.web.api.auth.infrastructure;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import uce.edu.web.api.auth.application.UsuarioService;

@ApplicationScoped
public class DataInitializer {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    UsuarioService usuarioService;

    @Transactional
    public void init(@Observes StartupEvent event) {
        long count = usuarioRepository.count();

        if (count == 0) {
            System.out.println("Inicializando usuarios de prueba...");
            usuarioService.crearUsuario("admin", "admin", "admin");
            System.out.println("Usuario creado: admin / admin (rol: admin)");
            usuarioService.crearUsuario("user", "user", "user");
            System.out.println("Usuario creado: user / user (rol: user)");
            System.out.println("Usuarios de prueba creados exitosamente.");
        } else {
            System.out.println("Los usuarios ya existen en la base de datos.");
        }
    }
}
