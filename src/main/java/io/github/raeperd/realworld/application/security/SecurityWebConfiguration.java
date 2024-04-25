package io.github.raeperd.realworld.application.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
@Configuration
public class SecurityWebConfiguration extends WebSecurityConfigurerAdapter {

        @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(POST, "/users", "/users/login");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors();
        http.formLogin().disable();
        http.logout().disable();
        http.addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers(GET, "/profiles/*").permitAll()
                .antMatchers(GET, "/articles/**").permitAll()
                .antMatchers(GET, "/tags/**").permitAll()
                .anyRequest().authenticated();

    }
}
