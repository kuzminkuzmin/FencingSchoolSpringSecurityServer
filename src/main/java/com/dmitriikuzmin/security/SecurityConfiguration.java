package com.dmitriikuzmin.security;

import com.dmitriikuzmin.dto.ResponseResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private UserDetailsService userDetailsService;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/user").permitAll()
                .antMatchers(HttpMethod.DELETE, "/user").hasRole("ADMIN")
                .antMatchers("/admin").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/apprentice").permitAll()
                .antMatchers(HttpMethod.GET, "/apprentice").hasAnyRole("ADMIN", "TRAINER")
                .antMatchers(HttpMethod.PUT, "/apprentice").hasAnyRole("ADMIN", "APPRENTICE")
                .antMatchers(HttpMethod.DELETE, "/apprentice").hasAnyRole("ADMIN", "APPRENTICE")
                .antMatchers(HttpMethod.POST, "/trainer").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/trainer").hasAnyRole("ADMIN", "APPRENTICE","TRAINER")
                .antMatchers(HttpMethod.PUT, "/trainer").hasAnyRole("ADMIN", "TRAINER")
                .antMatchers(HttpMethod.DELETE, "/trainer").hasAnyRole("ADMIN", "TRAINER")
                .antMatchers(HttpMethod.POST, "/training").hasAnyRole("ADMIN", "TRAINER", "APPRENTICE")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json; charset=utf-8");
                    String text = "Нет доступа";
                    new ObjectMapper().writeValue(response.getOutputStream(), new ResponseResult<>(text, null));
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setCharacterEncoding("utf-8");
                    response.setContentType("application/json;charset=utf-8");
                    String authenticateHeader = response.getHeader("WWW-Authenticate");
                    String text = authenticateHeader != null ? "Неправильный логин или пароль" : "Ошибка авторизации";
                    new ObjectMapper().writeValue(response.getOutputStream(), new ResponseResult<>(text, null));
                })
                .and()
                .httpBasic();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
