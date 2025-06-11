package org.example.userservice.security;



import org.example.userservice.security.passwordencoder.PasswordEncoderConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity
@Import(
        {PasswordEncoderConfig.class,
        }


)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        configureBasicAuthentication(http);
        configureEndpointSecurity(http);
        configureCsrf(http);
        configureSessionManagement(http);
        return http.build();
    }


    private static void configureBasicAuthentication(HttpSecurity http) throws Exception{

        http.httpBasic(c -> c.realmName("task"));
    }

    private static void configureEndpointSecurity(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                        //.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()

        )
                .formLogin(withDefaults());

    }

    private static void configureCsrf(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
    }

    private static void configureSessionManagement(HttpSecurity http) throws Exception {
        http.sessionManagement(s -> s
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
    }

//    private static void configureApiKeyAuthentication(HttpSecurity http) throws Exception {
//        http.with(new ApiKeyAuthenticationConfigurer(), Customizer.withDefaults());
//    }


}
