package com.expeditors.trackservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestClient;

import static com.expeditors.trackservice.config.profiles.Profiles.SSL;

@Configuration
@EnableWebSecurity
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

       return new InMemoryUserDetailsManager(normaluser);
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
