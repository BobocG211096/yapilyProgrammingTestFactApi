package uk.yapily.facts.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@Order(1)
public class APISecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${application.http.authTokenHeaderName}")
    private String authTokenHeaderName;

    @Value("${application.http.apiKey}")
    private String apiKey;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        APIKeyAuthFilter filter = new APIKeyAuthFilter(authTokenHeaderName);
        filter.setAuthenticationManager(authentication -> {
            String apiKeyFromHeader = (String) authentication.getPrincipal();

            if (!apiKey.equals(apiKeyFromHeader))
            {
                throw new BadCredentialsException("The API key was not found or not the expected value.");
            }

            authentication.setAuthenticated(true);
            return authentication;
        });

        httpSecurity
                .requestMatchers()
                .antMatchers("/facts/**", "/status")
                .and()
                .csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilter(filter).authorizeRequests().anyRequest().authenticated();
    }

}
