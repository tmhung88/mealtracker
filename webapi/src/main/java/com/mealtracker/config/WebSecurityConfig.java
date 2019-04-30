package com.mealtracker.config;

import com.mealtracker.config.properties.JwtProperties;
import com.mealtracker.security.RestAccessDeniedHandler;
import com.mealtracker.security.RestAuthenticationEntryPoint;
import com.mealtracker.security.jwt.JwtAuthenticationFilter;
import com.mealtracker.security.jwt.JwtTokenProvider;
import com.mealtracker.services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

// TODO: Need review
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(JwtProperties jwtProperties) {
        return new JwtTokenProvider(jwtProperties.getSecretKey(), jwtProperties.getExpirationInMs());
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        var authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userService);
//        authProvider.setPasswordEncoder(passwordEncoder());
//        return authProvider;
//    }

    @Configuration
    public static class AppWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        private final UserService userService;
        private final PasswordEncoder passwordEncoder;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final HandlerExceptionResolver exceptionResolver;

        public AppWebSecurityConfigurerAdapter(UserService userService,
                                               PasswordEncoder passwordEncoder,
                                               JwtAuthenticationFilter jwtAuthenticationFilter,
                                               @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
            this.userService = userService;
            this.passwordEncoder = passwordEncoder;
            this.jwtAuthenticationFilter = jwtAuthenticationFilter;
            this.exceptionResolver = exceptionResolver;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .cors().and().csrf().disable()
                    .exceptionHandling()
                        .accessDeniedHandler(new RestAccessDeniedHandler(exceptionResolver))
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint(exceptionResolver)).and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeRequests()
                        .regexMatchers(GET, "\\/v1\\/users\\/?\\?email=.*").permitAll()
                        .antMatchers(POST, "/v1/users").permitAll() // sign-up
                        .antMatchers(POST, "/v1/sessions").permitAll() // sign-in
                    .anyRequest().authenticated();
            // Add our custom JWT security filter
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }


        @Override
        protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userService)
                    .passwordEncoder(passwordEncoder);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }
}
