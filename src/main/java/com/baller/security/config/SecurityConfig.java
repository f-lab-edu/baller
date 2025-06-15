package com.baller.security.config;

import com.baller.security.filter.JwtAuthFilter;
import com.baller.security.filter.JwtExceptionFilter;
import com.baller.security.filter.LoginFilter;
import com.baller.security.handler.JwtAuthenticationEntryPoint;
import com.baller.security.handler.JwtAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final LoginFilter    loginFilter;
    private final JwtAuthFilter  jwtAuthFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    private static final String[] WHITE_LIST_POST_URL = {
            "/api/members",
            "/api/members/login"
    };

    private static final String[] WHITE_LIST_GET_URL = {
            "/api/clubs",
            "/api/clubs/*",
            "/actuator/prometheus",
            "/api/games",
            "/api/games/*",
            "/api/games/*/records",
            "/game-record.html"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, WHITE_LIST_POST_URL).permitAll()
                        .requestMatchers(HttpMethod.GET, WHITE_LIST_GET_URL).permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .addFilterBefore(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class);

        return http.build();
    }
}
