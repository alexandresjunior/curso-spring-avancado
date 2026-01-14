package br.com.procardio.api.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.procardio.api.model.Usuario;
import br.com.procardio.api.service.GoogleAuthService;
import br.com.procardio.api.service.TokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private GoogleAuthService googleAuthService;

    @Autowired
    private TokenService tokenService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll();
                    // Opcional: Liberar Swagger ou outros endpoints públicos aqui
                    // O endpoint de callback do Google precisa ser público,
                    // mas o Spring Security gerencia isso internamente na cadeia oauth2Login
                    req.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> oauth2
                        // Define o que fazer quando o login no Google der certo
                        .successHandler(googleLoginSuccessHandler()))
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Handler personalizado para Login com Google
     * 1. Recupera o usuário do Google
     * 2. Salva ou atualiza no banco
     * 3. Gera o Token JWT da nossa API
     * 4. Redireciona ou retorna o token (Para fins didáticos, escrevemos no corpo)
     */
    @Bean
    public AuthenticationSuccessHandler googleLoginSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {

                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

                Usuario usuario = googleAuthService.processarUsuarioGoogle(oAuth2User);

                String tokenJWT = tokenService.gerarToken(usuario);

                response.setContentType("application/json");
                response.getWriter().write("{\"token\": \"" + tokenJWT + "\"}");
                response.getWriter().flush();
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}