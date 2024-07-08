package meusite.configuration.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import meusite.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    AuthService authService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final var anAuthToken = request.getHeader("Authorization");

        if(anAuthToken != null){
            final var aToken = anAuthToken.replace("Bearer ", "");

            final var anUsername = authService.validateToken(aToken);

            var anUser = authService.loadUserByUsername(anUsername);

            final var anAuth = new UsernamePasswordAuthenticationToken(
                    anUser,null,
                    anUser.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(anAuth);
        }
        filterChain.doFilter(request,response);
    }
}
