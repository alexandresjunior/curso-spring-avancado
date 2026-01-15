package br.com.procardio.api.config;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.procardio.api.model.Usuario;
import br.com.procardio.api.repository.UsuarioRepository;
import br.com.procardio.api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwtToken = recuperarToken(request);

        if (Objects.nonNull(jwtToken)) {
            String subject = tokenService.getSubject(jwtToken);

            Usuario usuario = usuarioRepository.findByEmail(subject).orElse(null);

            if (Objects.nonNull(usuario)) {
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest requisicao) {
        String cabecalhoAuthorization = requisicao.getHeader("Authorization");

        if (Objects.nonNull(cabecalhoAuthorization)) {
            return cabecalhoAuthorization.replace("Bearer ", "");
        }

        return null;
    }

}
