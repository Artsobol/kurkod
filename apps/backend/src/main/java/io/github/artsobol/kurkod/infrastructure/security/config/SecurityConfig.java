package io.github.artsobol.kurkod.infrastructure.security.config;

import io.github.artsobol.kurkod.config.security.JwtProperties;
import io.github.artsobol.kurkod.feature.auth.refreshtoken.service.RefreshTokenService;
import io.github.artsobol.kurkod.feature.user.entity.Role;
import io.github.artsobol.kurkod.feature.user.repository.UserRepository;
import io.github.artsobol.kurkod.infrastructure.localization.MessageService;
import io.github.artsobol.kurkod.infrastructure.security.jwt.JwtAccessDeniedHandler;
import io.github.artsobol.kurkod.infrastructure.security.jwt.JwtAuthenticationEntryPoint;
import io.github.artsobol.kurkod.infrastructure.security.jwt.JwtAuthenticationFilter;
import io.github.artsobol.kurkod.infrastructure.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.infrastructure.security.user.UserDetailsServiceImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final SecurityConfigProperties securityConfigProperties;

  private static final String[] PUBLIC_AUTH_POST = {
    "/auth/login", "/auth/register", "/auth/refresh", "/auth/logout"
  };

  private static final String[] PUBLIC_DOCS = {
    "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**"
  };

  private static final String[] ADMIN_AUTHORITIES = {
    Role.SUPER_ADMIN.name(), Role.ADMIN.name()
  };

  @Bean
  public SecurityFilterChain securityFilterChain(
          HttpSecurity http,
          JwtAuthenticationFilter jwtAuthenticationFilter,
          AuthenticationEntryPoint authenticationEntryPoint,
          AccessDeniedHandler accessDeniedHandler) {

    http.csrf(AbstractHttpConfigurer::disable)
        .cors(Customizer.withDefaults())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.POST, PUBLIC_AUTH_POST)
                    .permitAll()
                    .requestMatchers(PUBLIC_DOCS)
                    .permitAll()
                    .requestMatchers(HttpMethod.GET, "/staff/**")
                    .hasAnyAuthority(ADMIN_AUTHORITIES)
                    .requestMatchers("/admin/**")
                    .hasAnyAuthority(ADMIN_AUTHORITIES)
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler));

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
    config.setAllowCredentials(true);
    config.setAllowCredentials(true);
    config.setAllowCredentials(true);
    config.setMaxAge(securityConfigProperties.maxAge());

    var source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public JwtTokenProvider jwtTokenProvider(JwtProperties properties) {
    return new JwtTokenProvider(properties);
  }

  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter(
          JwtTokenProvider provider,
          RefreshTokenService refreshTokenService
  ) {
    return new JwtAuthenticationFilter(provider, refreshTokenService);
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint(MessageService messageService, ObjectMapper objectMapper) {
    return new JwtAuthenticationEntryPoint(messageService, objectMapper);
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler(MessageService messageService, ObjectMapper objectMapper) {
    return new JwtAccessDeniedHandler(messageService, objectMapper);
  }

  @Bean
  public UserDetailsService userDetailsService(UserRepository userRepository) {
    return new UserDetailsServiceImpl(userRepository);
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
}
