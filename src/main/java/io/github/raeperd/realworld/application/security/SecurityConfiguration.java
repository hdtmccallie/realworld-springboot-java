package io.github.raeperd.realworld.application.security;

import io.github.raeperd.realworld.domain.jwt.JWTDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@EnableConfigurationProperties(SecurityConfiguration.SecurityConfigurationProperties.class)
@Configuration
public class SecurityConfiguration implements WebMvcConfigurer {
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final SecurityConfigurationProperties properties;

    SecurityConfiguration(SecurityConfigurationProperties properties) {
        this.properties = properties;
    }


    @Bean
    JWTAuthenticationProvider jwtAuthenticationProvider(JWTDeserializer jwtDeserializer) {
        return new JWTAuthenticationProvider(jwtDeserializer);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "HEAD", "POST", "DELETE", "PUT")
                .allowedOrigins(properties.getAllowedOrigins().toArray(new String[0]))
                .allowedHeaders("*")
                .allowCredentials(true);
    }


    @ConstructorBinding
    @ConfigurationProperties("security")
    public static class SecurityConfigurationProperties {
        private final List<String> allowedOrigins;

        SecurityConfigurationProperties(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }
    }
}
