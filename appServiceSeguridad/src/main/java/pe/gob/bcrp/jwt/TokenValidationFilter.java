package pe.gob.bcrp.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisTokenService redisTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
     String requestURI = request.getRequestURI();

     List<String> excludedPaths = List.of(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api/v1/oauth/login",
                "/api/v1/oauth/validarToken",
                "/api/v1/oauth/refreshToken",
                "/api/v1/oauth/logout",
                "/api/v1/oauth/captcha",
                "/api/v1/search/**",
                "/api/media/**",
                "/api/v1/oauth/regenerate-otp",
                "/api/v1/oauth/verify-otp"
        );
        AntPathMatcher pathMatcher = new AntPathMatcher();
        // Si la URI coincide con alguna ruta excluida, pasa el filtro
        if (excludedPaths.stream().anyMatch(path ->  pathMatcher.match(path,requestURI))){
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);
        String username;
        try {
            username = extractUsernameFromToken(token);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!redisTokenService.isLatestToken(username, token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String extractUsernameFromToken(String token) throws Exception {
        String payload = new String(Base64.getDecoder().decode(token.split("\\.")[1]));
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> claims = mapper.readValue(payload, Map.class);
        return (String) claims.get("preferred_username");
    }
}
