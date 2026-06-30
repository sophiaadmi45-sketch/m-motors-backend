package com.mmotores.m_motors_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String path = request.getRequestURI();
        String method = request.getMethod();

        
        if (path.startsWith("/vehicles") && (method.equals("POST") || method.equals("PUT")) 
            || path.equals("/api/dossiers") && method.equals("GET")
            || path.matches("/api/dossiers/\\d+/statut") && method.equals("PUT")) {

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                response.setStatus(401);
                response.getWriter().write("Erreur : Requete anonyme non autorisee.");
                return;
            }

            String token = authHeader.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey("MaCleSecreteSuperSecuriseePourMonExamenMMotors2026")
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String role = claims.get("role", String.class);
                if (!"DIRECTEUR".equals(role)) {
                    response.setStatus(403);
                    response.getWriter().write("Erreur : Acces refuse. Role Directeur requis.");
                    return;
                }
            } catch (Exception e) {
                response.setStatus(401);
                response.getWriter().write("Erreur : Token invalide ou expire.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}