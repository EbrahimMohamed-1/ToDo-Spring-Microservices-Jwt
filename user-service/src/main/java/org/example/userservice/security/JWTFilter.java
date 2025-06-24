package org.example.userservice.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.userservice.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;


@RequiredArgsConstructor
@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
          String token = jwtService.resolveToken(request);
          if (token == null) {
              filterChain.doFilter(request, response);
              return;
          }

            Claims claims = jwtService.resolveClaims(request);

            if(claims != null && !jwtService.isTokenExpired(claims.getExpiration())){

                String email = claims.getSubject();


                Authentication authentication = new UsernamePasswordAuthenticationToken(email,"",new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
