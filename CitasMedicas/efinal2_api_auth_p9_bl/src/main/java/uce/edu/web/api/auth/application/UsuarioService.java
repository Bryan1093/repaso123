package uce.edu.web.api.auth.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;
import uce.edu.web.api.auth.domain.Usuario;
import uce.edu.web.api.auth.infrastructure.UsuarioRepository;

@ApplicationScoped
public class UsuarioService {

    @Inject
    UsuarioRepository usuarioRepository;

    public Usuario validarCredenciales(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario == null) {
            return null;
        }

        // Si la contraseña guardada no tiene el formato de hash de BCrypt, comparar
        // como texto plano
        if (!usuario.getPassword().startsWith("$2a$")) {
            if (usuario.getPassword().equals(password)) {
                return usuario;
            }
            return null;
        }

        if (BCrypt.checkpw(password, usuario.getPassword())) {
            return usuario;
        }

        return null;
    }

    @Transactional
    public Usuario crearUsuario(String username, String password, String rol) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Usuario usuario = new Usuario(username, hashedPassword, rol);
        usuarioRepository.persist(usuario);

        return usuario;
    }
}
