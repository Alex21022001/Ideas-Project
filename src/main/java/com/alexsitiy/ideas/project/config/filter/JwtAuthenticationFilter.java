package com.alexsitiy.ideas.project.config.filter;

import com.alexsitiy.ideas.project.util.JwtTokenUtil;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtTokenUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization != null && authorization.startsWith("Bearer ")){
            String token = authorization.replace("Bearer ", "");
            log.debug("JWT token was found");

            try{
                String username = jwtUtil.validateAndExtractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                addUserToSecurityContext(request, userDetails);

                filterChain.doFilter(request,response);
            }catch (JWTVerificationException | UsernameNotFoundException jwtException){
                log.debug("JWT token is Invalid {}",jwtException.getLocalizedMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"JWT toke is invalid");
            }

        }else {
            filterChain.doFilter(request,response);
        }
    }

    private void addUserToSecurityContext(HttpServletRequest request, UserDetails userDetails) {
        if (SecurityContextHolder.getContext().getAuthentication() == null){
            var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            log.debug("User {} is added to SecurityContext",userDetails);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
}
