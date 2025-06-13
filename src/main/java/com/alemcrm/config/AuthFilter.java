package com.alemcrm.config;

import com.alemcrm.util.TokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;

    public AuthFilter(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        System.out.println("🚨 ROTA INTERCEPTADA: " + request.getRequestURI());

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        final String path = request.getRequestURI();
        final String method = request.getMethod();

        System.out.println("\n🚨 [AuthFilter] Iniciando filtro para: " + path + 
        		" | Método: " + method);

        if (isPublicRoute(path, method)) {
            System.out.println("🟢 Rota pública - acesso liberado");
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("🔴 Erro: Header Authorization ausente ou inválido");
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token ausente ou mal formado");
            return;
        }

        final String token = authHeader.substring(7); // Remove "Bearer "

        try {
            System.out.println("🔍 Token recebido: " + token);

            final Claims claims = tokenUtil.decodeToken(token);

            final String email = claims.getSubject();
            final String role = claims.get("role", String.class);
            final Long id = claims.get("id", Long.class);

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());

            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, null, List.of(authority));

            SecurityContextHolder.getContext().setAuthentication(authToken);

            request.setAttribute("userId", id);
            request.setAttribute("userEmail", email);
            request.setAttribute("userRole", role);

            System.out.println("✅ Acesso permitido para " + path);
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            System.out.println("🔴 Token expirado: " + e.getMessage());
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
        } catch (SignatureException e) {
            System.out.println("🔴 Assinatura inválida: " + e.getMessage());
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Assinatura do token inválida");
        } catch (Exception e) {
            System.out.println("🔴 Erro inesperado: " + e.getMessage());
            e.printStackTrace();
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou mal formado");
        }
    }

    private boolean isPublicRoute(String path, String method) {
        return path.equals("/auth/login") || 
               path.startsWith("/h2-console") || 
               method.equalsIgnoreCase("OPTIONS");
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
