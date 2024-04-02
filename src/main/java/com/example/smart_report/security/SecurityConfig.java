package com.example.smart_report.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    customSuccessHandler customSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/signup/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/healthcare_professional", "/healthTests/**", "/containers/**",
                                "/machines/**", "/healthParameters/**",
                                "/reagents/**", "/reagentActions/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_HEALTHCARE_PROFESSIONAL", "ROLE_INVENTORY_MANAGER")
                        .requestMatchers("/lab_technician", "/testLabMappings/**", "/labFaults/**", "/labProcesses/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_LAB_TECHNICIAN", "ROLE_INVENTORY_MANAGER")
                        .requestMatchers("/inventory_manager", "/inventories/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_INVENTORY_MANAGER")
                        .requestMatchers("/admin/**")
                        .hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customSuccessHandler)
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .permitAll())

                .logout(obj -> obj.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/?logout")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID"));

        http.exceptionHandling((ex) -> ex.accessDeniedPage("/unAuthorized"));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/", "/images/**",
                "/unAuthorized");
    }

}

@Component
class customSuccessHandler implements AuthenticationSuccessHandler {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        System.out.println("authority " + authentication.getAuthorities());
        handleRedirect(request, response, authentication.getAuthorities());
    }

    private void handleRedirect(HttpServletRequest request, HttpServletResponse response,
            Collection<? extends GrantedAuthority> authorities) throws IOException {
        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        boolean isHealthcareProfessional = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_HEALTHCARE_PROFESSIONAL"));

        boolean isInventoryManager = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_INVENTORY_MANAGER"));

        boolean isLabTechnician = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_LAB_TECHNICIAN"));

        if (isAdmin) {
            redirectStrategy.sendRedirect(request, response, "/admin");
        } else if (isHealthcareProfessional) {
            redirectStrategy.sendRedirect(request, response, "/healthcare_professional");
        } else if (isInventoryManager) {
            redirectStrategy.sendRedirect(request, response, "/inventory_manager");
        } else if (isLabTechnician) {
            redirectStrategy.sendRedirect(request, response, "/lab_technician");
        } else {
            redirectStrategy.sendRedirect(request, response, "/user");
        }
    }

}
