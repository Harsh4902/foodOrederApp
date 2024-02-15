package com.example.foodOrderApp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private AuthenticationProvider authenticationProvider;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity
      .csrf(t -> t.disable())
      .authorizeHttpRequests(t -> {
        t
//          .requestMatchers("/login","/register").permitAll()
//          .requestMatchers("/restaurant/**").permitAll().requestMatchers("/complaints/**").permitAll()
          .anyRequest().permitAll();
      })
      .formLogin(t -> {
        t
          .loginPage("/login")
          .usernameParameter("email")
          .passwordParameter("password")
          .successHandler((request, response, authentication) -> {
              response.sendRedirect("/dashboard");
          });
      })
      .authenticationProvider(authenticationProvider)
      ;

    return httpSecurity.build();
  }

}
