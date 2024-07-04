package truyenconvert.server.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import truyenconvert.server.models.enums.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Value("${truyencv.api-prefix}")
    private String apiPrefix;
    private final JwtFilter jwtFilter;
    private final AuthenticationProvider authenticationProvider;

    private final CorsConfigurationSource configurationSource;

    public SecurityConfiguration(JwtFilter jwtFilter, AuthenticationProvider authenticationProvider,
                                 @Qualifier("corsConfigurationSource") CorsConfigurationSource configurationSource) {
        this.jwtFilter = jwtFilter;
        this.authenticationProvider = authenticationProvider;
        this.configurationSource = configurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(configurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth

                        //authentication
                        .requestMatchers(HttpMethod.POST,String.format("%s/auth/sign-in",apiPrefix)).permitAll()
                        .requestMatchers(HttpMethod.POST,String.format("%s/auth/sign-up",apiPrefix)).permitAll()
                        .requestMatchers(HttpMethod.POST,String.format("%s/auth/sign-out",apiPrefix)).permitAll()

                        //user


                        //report
                        .requestMatchers(HttpMethod.GET,String.format("%s/reports/admin",apiPrefix)).hasAnyRole(Role.ADMIN.name(),Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.GET,String.format("%s/reports/user",apiPrefix)).hasAnyRole(Role.USER.name(),Role.ADMIN.name(),Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.PATCH,String.format("%s/reports/{id}",apiPrefix)).hasAnyRole(Role.ADMIN.name(),Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.POST,String.format("%s/reports/",apiPrefix)).hasAnyRole(Role.USER.name(),Role.ADMIN.name(),Role.MODERATOR.name())

                        //report type
                        .requestMatchers(HttpMethod.GET,String.format("%s/rptypes/",apiPrefix)).hasAnyRole(Role.USER.name(),Role.ADMIN.name(),Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.PATCH,String.format("%s/rptypes/{id}",apiPrefix)).hasAnyRole(Role.ADMIN.name(),Role.MODERATOR.name())
                        .requestMatchers(HttpMethod.POST,String.format("%s/rptypes/",apiPrefix)).hasAnyRole(Role.ADMIN.name(),Role.MODERATOR.name())

                )
                .sessionManagement(ses -> ses.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
