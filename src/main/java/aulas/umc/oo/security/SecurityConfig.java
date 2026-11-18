package aulas.umc.oo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JsonAuthenticationEntryPoint authenticationEntryPoint,
            JsonAccessDeniedHandler accessDeniedHandler,
            JwtAuthenticationConverter jwtAuthenticationConverter)
            throws Exception {

        http.csrf(csrf -> csrf.disable())
                .requestCache(cache -> cache.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/health")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .httpBasic(basic -> basic.authenticationEntryPoint(authenticationEntryPoint))
                .oauth2ResourceServer(
                        oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                                .authenticationEntryPoint(authenticationEntryPoint)
                                .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }

    // Conta de laboratorio opcional: senha BCrypt mantida exclusivamente no Render.
    // Sem estas variaveis, nao existe usuario de teste habilitado.
    @Bean
    @ConditionalOnProperty(name = "MAGAZINE_TEST_USERNAME")
    UserDetailsService testUserDetailsService(
            @Value("${MAGAZINE_TEST_USERNAME}") String username,
            @Value("${MAGAZINE_TEST_PASSWORD_HASH}") String passwordHash) {
        if (username.isBlank() || !passwordHash.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$")) {
            throw new IllegalStateException("Credenciais de teste invalidas; configure o hash BCrypt no Render");
        }
        return new InMemoryUserDetailsManager(User.withUsername(username)
                .password("{bcrypt}" + passwordHash)
                .authorities(
                        Permission.PRODUTO_LER, Permission.CLIENTE_LER,
                        Permission.CARRINHO_LER, Permission.PEDIDO_LER,
                        Permission.PAGAMENTO_LER, Permission.ESTOQUE_LER)
                .build());
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authorities = new JwtGrantedAuthoritiesConverter();
        authorities.setAuthoritiesClaimName("permissions");
        authorities.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authorities);
        return converter;
    }
}
