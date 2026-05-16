package dev.javarush.feeder.api.security;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import jakarta.servlet.http.HttpServletRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  @Value("${feeds.sync.allowed-users:}")
  List<String> feedSyncAdmins;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.cors(cors -> cors.configurationSource(apiConfigurationSource()));
    http.authorizeHttpRequests(
        authz ->
            authz
                    .requestMatchers(HttpMethod.OPTIONS).permitAll()
                    .requestMatchers("/actuator/health/*").permitAll()
                    .requestMatchers("/feeds/sync").access(
                    new AuthorizationManager<>() {
                      @Override
                      public @Nullable AuthorizationResult authorize(
                          Supplier<? extends @Nullable Authentication> authentication,
                          RequestAuthorizationContext object) {
                        System.out.println("feedSyncAdmins: " + feedSyncAdmins);
                        Authentication auth = authentication.get();
                        if (auth == null) {
                          return new AuthorizationDecision(false);
                        }
                        return new AuthorizationDecision(feedSyncAdmins.contains(auth.getName()));
                      }
                    })
                .anyRequest().authenticated()
    );
    http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }

  UrlBasedCorsConfigurationSource apiConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:5173"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
