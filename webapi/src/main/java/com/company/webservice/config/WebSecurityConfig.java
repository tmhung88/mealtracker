package com.company.webservice.config;

import com.company.webservice.config.properties.JwtProperties;
import com.company.webservice.security.RestAccessDeniedHandler;
import com.company.webservice.security.RestAuthenticationEntryPoint;
import com.company.webservice.security.jwt.JwtAuthenticationFilter;
import com.company.webservice.security.jwt.JwtAuthenticationHandler;
import com.company.webservice.security.jwt.JwtTokenProvider;
import com.company.webservice.security.jwt.JwtTokenValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class WebSecurityConfig {

    @Bean
    @ConfigurationProperties("app.jwt")
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(JwtProperties jwtProperties) {
        return new JwtTokenProvider(jwtProperties.getSecretKey());
    }

    @Bean
    public JwtTokenValidator jwtTokenValidator(JwtProperties jwtProperties) {
        return new JwtTokenValidator(jwtProperties.getSecretKey());
    }

    @Bean
    public JwtAuthenticationHandler jwtAuthenticationHandler(JwtTokenProvider jwtTokenProvider, JwtTokenValidator jwtTokenValidator) {
        return new JwtAuthenticationHandler(jwtTokenProvider, jwtTokenValidator);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtAuthenticationHandler authenticationHandler) {
        return new JwtAuthenticationFilter(authenticationHandler);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        var emptyRoleVoterPrefix = "";
        return  new GrantedAuthorityDefaults(emptyRoleVoterPrefix);
    }

    @Configuration
    public static class AppWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final HandlerExceptionResolver exceptionResolver;

        public AppWebSecurityConfigurerAdapter(JwtAuthenticationFilter jwtAuthenticationFilter,
                                               @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
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
                    .antMatchers(POST, "/v1/users").permitAll()
                    .antMatchers(POST, "/v1/sessions").permitAll()

                    .anyRequest().authenticated();


            // Add our custom JWT security filter
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }


    }
}
