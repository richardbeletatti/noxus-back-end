package com.alemcrm.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alemcrm.util.TokenUtil;

import java.io.IOException;

public class AuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.equals("/auth/login") || path.startsWith("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token ausente");
            return;
        }

        try {
            String decoded = TokenUtil.decodeFakeToken(authHeader);
            String[] parts = decoded.split(":");

            if (parts.length != 2) {
                throw new IllegalArgumentException("Token inválido");
            }

            String email = parts[0];
            String role = parts[1];

            if (path.startsWith("/admin") && !role.equals("admin")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Acesso negado: somente administradores");
                return;
            }

            if (path.startsWith("/user") && !role.equals("user") && !role.equals("admin")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write("Acesso negado: somente usuários comuns");
                return;
            }

            request.setAttribute("userEmail", email);
            request.setAttribute("userRole", role);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token inválido ou mal formado");
        }
    }
}
