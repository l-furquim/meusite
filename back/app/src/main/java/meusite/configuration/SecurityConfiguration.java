package meusite.configuration;


import meusite.configuration.filter.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    SecurityFilter securityFilter;


    @Bean
    public SecurityFilterChain setSecurtityFilterChain(final HttpSecurity httpSecurity)throws Exception{
        return httpSecurity.
                csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.POST, "user/recoveryEmail").permitAll())
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.POST, "user/register").permitAll())
                .authorizeHttpRequests(authorize -> authorize.requestMatchers(HttpMethod.POST, "user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "user/login/validate").permitAll()
                        .anyRequest()
                        .authenticated())
                .addFilterBefore(this.securityFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
