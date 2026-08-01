package io.github.artsobol.kurkod.infrastructure.security.config;

import io.github.artsobol.kurkod.feature.iam.entity.SystemRole;
import io.github.artsobol.kurkod.infrastructure.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.infrastructure.security.handler.AccessRestrictionHandler;
import io.github.artsobol.kurkod.infrastructure.security.handler.RestAuthenticationEntryPoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final SecurityConfigProperties securityConfigProperties;

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      AccessRestrictionHandler accessRestrictionHandler,
      RestAuthenticationEntryPoint restAuthenticationEntryPoint,
      JwtRequestFilter jwtRequestFilter) {

    http.csrf(AbstractHttpConfigurer::disable);

    http.cors(Customizer.withDefaults());

    http.authorizeHttpRequests(
        auth ->
            auth.requestMatchers(HttpMethod.OPTIONS, "/**")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/auth/refresh/token")
                .permitAll()
                .requestMatchers(
                    "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/staff/**")
                .hasAnyAuthority(adminAccessSecurityRoles())
                .requestMatchers("/admin/**")
                .hasAnyAuthority(adminAccessSecurityRoles())
                .anyRequest()
                .authenticated());

    http.exceptionHandling(
        ex ->
            ex.authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(accessRestrictionHandler));

    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    var config = new CorsConfiguration();

    config.setAllowedOriginPatterns(securityConfigProperties.allowedOrigins());
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(
        List.of("Authorization", "Content-Type", "Accept", "X-Requested-With", "If-Match"));
    config.setExposedHeaders(List.of("Authorization", "Location", "ETag"));
    config.setMaxAge(securityConfigProperties.maxAge());

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    PasswordEncoder bcrypt = new BCryptPasswordEncoder();
    PasswordEncoder argon2 = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put("bcrypt", bcrypt);
    encoders.put("argon2", argon2);

    DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder("argon2", encoders);
    encoder.setDefaultPasswordEncoderForMatches(bcrypt);

    return encoder;
  }

  @Bean
  public AuthenticationProvider authenticationProvider(
      UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) {
    return authenticationConfiguration.getAuthenticationManager();
  }

  private String[] adminAccessSecurityRoles() {
    return new String[] {SystemRole.SUPER_ADMIN.name(), SystemRole.ADMIN.name()};
  }
}
