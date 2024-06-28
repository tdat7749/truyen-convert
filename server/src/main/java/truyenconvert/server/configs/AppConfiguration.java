package truyenconvert.server.configs;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import truyenconvert.server.modules.users.exceptions.UserNotFoundException;
import truyenconvert.server.modules.users.repositories.UserRepository;

@Configuration
public class AppConfiguration {
    private final UserRepository userRepository;

    public AppConfiguration(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return userName -> userRepository.findByEmail(userName).orElseThrow(() -> new UserNotFoundException("Người dùng không tồn tại"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedMethod("*"); // Cấu hình cho phép tất cả các phương thức (GET, POST, PUT, DELETE, v.v.)
        config.addAllowedHeader("*"); // Cấu hình cho phép tất cả các tiêu đề
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:/service_messages/validation-messages",
                "classpath:/service_messages/auth-messages",
                "classpath:/service_messages/user-messages",
                "classpath:/service_messages/report-type-messages",
                "classpath:/service_messages/file-storage-messages"
        );
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }
}
