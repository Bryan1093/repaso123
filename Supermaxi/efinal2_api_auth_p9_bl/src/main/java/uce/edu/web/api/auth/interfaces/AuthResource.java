package uce.edu.web.api.auth.interfaces;

import java.time.Instant;
import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import uce.edu.web.api.auth.application.UsuarioService;
import uce.edu.web.api.auth.domain.Usuario;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @ConfigProperty(name = "auth.issuer")
    String issuer;

    @ConfigProperty(name = "auth.token.ttl")
    Long ttl;

    @Inject
    UsuarioService usuarioService;

    @POST
    @Path("/login")
    public TokenResponse login(AuthRequest request) {

        Usuario usuario = usuarioService.validarCredenciales(request.username, request.password);

        if (usuario == null) {
            throw new jakarta.ws.rs.WebApplicationException(
                    "Credenciales inválidas",
                    jakarta.ws.rs.core.Response.Status.UNAUTHORIZED);
        }

        Instant now = Instant.now();
        Instant exp = now.plusSeconds(ttl);

        String jwt = Jwt.issuer(issuer)
                .subject(request.username)
                .groups(Set.of(usuario.getRol()))
                .issuedAt(now)
                .expiresAt(exp)
                .sign();

        return new TokenResponse(jwt, exp.getEpochSecond(), usuario.getRol());
    }

    public static class AuthRequest {
        public String username;
        public String password;
    }

    public static class TokenResponse {

        public String accessToken;

        public long expiresAt;

        public String role;

        public TokenResponse() {
        }

        public TokenResponse(String accessToken, long expiresAt, String role) {

            this.accessToken = accessToken;

            this.expiresAt = expiresAt;

            this.role = role;

        }

    }

}
