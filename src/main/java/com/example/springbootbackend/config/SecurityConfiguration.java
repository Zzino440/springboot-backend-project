package com.example.springbootbackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.springbootbackend.user.enums.Permission.*;
import static com.example.springbootbackend.user.enums.Role.ADMIN;
import static com.example.springbootbackend.user.enums.Role.USER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll()

/*                        .requestMatchers("api/v1/users/**").hasRole(ADMIN.name())

                        .requestMatchers(GET,"api/v1/users/**").hasAuthority(ADMIN_READ.name())
                        .requestMatchers(POST,"api/v1/users/**").hasAuthority(ADMIN_POST.name())
                        .requestMatchers(PUT,"api/v1/users/**").hasAuthority(ADMIN_UPDATE.name())
                        .requestMatchers(DELETE,"api/v1/users/**").hasAuthority(ADMIN_DELETE.name())*/

                        .requestMatchers("api/v1/demo-controller/**").hasRole(USER.name())
                        .requestMatchers(GET,"api/v1/demo-controller").hasAuthority(USER_READ.name())

                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }
}
