package dev.javarush.feeder.api.security;

import java.util.List;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
public class SecurityConfig {

  @Value("${feeds.sync.allowed-users:}")
  List<String> feedSyncAdmins;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable);
    http.authorizeHttpRequests(
        authz ->
            authz
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
    http.httpBasic(Customizer.withDefaults());
    return http.build();
  }
}
