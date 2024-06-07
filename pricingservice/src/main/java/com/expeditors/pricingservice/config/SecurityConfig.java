package com.expeditors.pricingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails administrator = User.withUsername("administrator")
                .password("{bcrypt}$2a$10$KYGEZfin.QeVbMsx3G/o7OlxqK7EWTi.zSiszEAvLbQ5zHng9Zazy")
                .roles("USER", "ADMIN").
                build();

        UserDetails normalUser = User.withUsername("normalUser")
                .password("{bcrypt}$2a$10$4NcjVhKScEm6/YEu.jGMqu5iaG0kNbnSP/y2Ryvofb108qHQavQwu")
                .roles("USER").
                build();

        return new InMemoryUserDetailsManager(administrator, normalUser);
    }

    @Bean
    public SecurityFilterChain priceServiceFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(auth ->
                auth.requestMatchers(HttpMethod.GET, "/pricing/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/pricing/**").hasRole("ADMIN")
                        .anyRequest().denyAll())
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}
