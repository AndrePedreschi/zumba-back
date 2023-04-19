package com.example.zumbasquad.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
                //.cors(cors -> cors.disable())
                .cors().and()
                //disabling csrf
                .csrf().disable()
                //implementing allowlist
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET,"/auth/**", "/categorias/**", "/cidades/**", "/caracteristicas/**", "/produtos/**", "/reservas/por_produto/**").permitAll()
                .requestMatchers(HttpMethod.PUT,"/auth/**", "/categorias/**", "/cidades/**", "/caracteristicas/**").permitAll()
                .requestMatchers(HttpMethod.DELETE,"/auth/**", "/categorias/**", "/cidades/**", "/caracteristicas/**", "/produtos/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/auth/**", "/categorias/**", "/cidades/**", "/caracteristicas/**").permitAll()
                //authenticated requests
                .anyRequest().authenticated()
                //creating session requirements
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //adding filters to requests
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

     @Bean
     public WebMvcConfigurer corsConfigurer(){
         return new WebMvcConfigurer() {
             @Override
             public void addCorsMappings(CorsRegistry registry) {
                 registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST", "DELETE", "PUT", "OPTIONS").allowedHeaders("*");
             }
         };
     }
}
