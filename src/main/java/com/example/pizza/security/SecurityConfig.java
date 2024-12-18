package com.example.pizza.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .failureUrl("/login?error=true") // URL xử lý đăng nhập
                        .successHandler((request, response, authentication) -> {
                            // Lấy vai trò của người dùng sau khi đăng nhập
                            boolean isAdmin = authentication.getAuthorities().stream()
                                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

                            // Chuyển hướng tùy theo vai trò
                            if (isAdmin) {
                                response.sendRedirect("/admin/dashboard");
                            } else {
                                // Nếu là User, chuyển hướng đến trang thanh toán (checkout)
                                response.sendRedirect("/menu");
                            }
                        })
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll());

        // Cấu hình CSRF rõ ràng nếu cần thiết (ví dụ, vô hiệu hóa CSRF cho các API
        // RESTful)
        http.csrf(csrf -> csrf.disable()); // hoặc csrf().ignoringRequestMatchers("/api/**") tùy vào yêu cầu

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    // Bean mã hóa mật khẩu
    // @Bean
    // public PasswordEncoder passwordEncoder() {
    // return new BCryptPasswordEncoder();
    // }

}
