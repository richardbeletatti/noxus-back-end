package com.alemcrm.config;

import com.alemcrm.util.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        if (path.equals("/auth/login") || path.startsWith("/h2-console") || method.equalsIgnoreCase("OPTIONS")) {
            if (method.equalsIgnoreCase("OPTIONS")) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token ausente ou mal formado");
            return;
        }

        String token = authHeader.substring(7); // Remove "Bearer "

        try {
            Claims claims = TokenUtil.decodeToken(token);
            String email = claims.getSubject();
            String role = claims.get("role", String.class);

            // Check permission for role
            if (path.startsWith("/admin") && !"admin".equals(role)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Acesso negado: somente administradores");
                return;
            }

            if (path.startsWith("/user") && !(role.equals("user") || role.equals("admin"))) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Acesso negado: somente usuários comuns");
                return;
            }

            request.setAttribute("userEmail", email);
            request.setAttribute("userRole", role);

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expirado");
        } catch (SignatureException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Assinatura do token inválida");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido ou mal formado");
        }
    }
}