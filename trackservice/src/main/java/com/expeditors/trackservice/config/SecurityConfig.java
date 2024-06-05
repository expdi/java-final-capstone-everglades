package com.expeditors.trackservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

   @Bean
   public PasswordEncoder passwordEncoder() {
      PasswordEncoder passwordEncoder =
              PasswordEncoderFactories.createDelegatingPasswordEncoder();

      return passwordEncoder;
   }

   @Bean
   public UserDetailsService userDetailsService() {
      UserDetails normaluser = User.withUsername("normaluser")
              .password("{bcrypt}$2a$10$sgyDJFnsEfqtKuGHFcfJMOcEYk69OABtAueJWIDIP9BnAwPIZtsGa")
              .roles("USER")
              .build();

      var userDetailsService = new InMemoryUserDetailsManager(normaluser);

      return userDetailsService;
   }

   @Bean
   public SecurityFilterChain priceServiceFilterChain(HttpSecurity httpSecurity) throws Exception {
      return httpSecurity.authorizeHttpRequests(auth ->
                      auth.requestMatchers("/artist/**", "/track/**").authenticated()
                              .anyRequest().denyAll())
              .httpBasic(Customizer.withDefaults())
              .csrf(AbstractHttpConfigurer::disable)
              .build();
   }

}
