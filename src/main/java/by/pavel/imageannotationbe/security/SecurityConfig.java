package by.pavel.imageannotationbe.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@SecurityEnabled
public class SecurityConfig {

    public static final int BCRYPT_STRENGTH = 10;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
    }

    @Bean
    public AuthenticationManager authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public JwtCheckerFilter authorizationFilter(UserDetailsService userDetailsService) {
        return new JwtCheckerFilter(userDetailsService);
    }

    @Bean
    public CorsConfiguration corsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern("http://localhost:80");
        corsConfiguration.addAllowedOriginPattern("http://localhost");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        return corsConfiguration;
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtCheckerFilter authorizationFilter,
            ExceptionHandlerFilter exceptionHandlerFilter,
            CustomAuthenticationEntryPoint entryPoint
    ) throws Exception {
        return http
                .authorizeHttpRequests()
                .requestMatchers("/token", "/register", "/error").permitAll()
                .requestMatchers(new ProjectImagesRequestMatcher()).permitAll()
                .anyRequest().authenticated()
                .and()
                    .addFilterAfter(authorizationFilter, BasicAuthenticationFilter.class)
                    .addFilterBefore(exceptionHandlerFilter, JwtCheckerFilter.class)
                .csrf().disable()
                .cors().configurationSource(request -> corsConfiguration())
                .and()
                    .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
                .build();
    }

    private static final class ProjectImagesRequestMatcher implements RequestMatcher {
        @Override
        public boolean matches(HttpServletRequest request) {
            String path = request.getRequestURI().substring(request.getContextPath().length());
            return path.matches("^/projects/[\\w-]+/images/[\\w-]+/download.*$");
        }
    }
}
