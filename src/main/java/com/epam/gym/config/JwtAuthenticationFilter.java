package com.epam.gym.config;

import com.epam.gym.securityService.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final static String TOKEN_PREFIX = "Bearer ";
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(
            JwtService jwtService,
            @Lazy UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        parseToken(request)
                .ifPresent(this::addUserToContext);
        filterChain.doFilter(request, response);
    }

    private void addUserToContext(String jwtToken) {
        jwtService.validateToken(jwtToken);
        String userLogin = jwtService.extractUsername(jwtToken);
        updateSecurityContext(getUserDetails(userLogin));
    }

    private Optional<String> parseToken(HttpServletRequest request) {
        return Optional.ofNullable(request)
                .map(r -> r.getHeader("Authorization"))
                .filter(authHeader -> authHeader.startsWith(TOKEN_PREFIX))
                .map(authHeader -> authHeader.substring(TOKEN_PREFIX.length()));
    }

    private void updateSecurityContext(UserDetails userDetails) {
        SecurityContext context = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken authToken = getAuthToken(userDetails);
        context.setAuthentication(authToken);
    }

    private UsernamePasswordAuthenticationToken getAuthToken(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private UserDetails getUserDetails(String userLogin) {
        return userDetailsService.loadUserByUsername(userLogin);
    }

}