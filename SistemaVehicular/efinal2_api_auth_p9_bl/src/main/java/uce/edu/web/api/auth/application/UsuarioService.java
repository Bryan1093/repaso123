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

    /**
     * Valida las credenciales del usuario
     * 
     * @param username nombre de usuario
     * @param password contraseña en texto plano
     * @return Usuario si las credenciales son correctas, null en caso contrario
     */
    public Usuario validarCredenciales(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsername(username);

        if (usuario == null) {
            return null;
        }

        // Verificar la contraseña usando BCrypt
        if (BCrypt.checkpw(password, usuario.getPassword())) {
            return usuario;
        }

        return null;
    }

    /**
     * Crea un nuevo usuario con la contraseña encriptada
     * 
     * @param username nombre de usuario
     * @param password contraseña en texto plano
     * @param rol      rol del usuario (admin/user)
     * @return Usuario creado
     */
    @Transactional
    public Usuario crearUsuario(String username, String password, String rol) {
        // Encriptar la contraseña
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        Usuario usuario = new Usuario(username, hashedPassword, rol);
        usuarioRepository.persist(usuario);

        return usuario;
    }
}
