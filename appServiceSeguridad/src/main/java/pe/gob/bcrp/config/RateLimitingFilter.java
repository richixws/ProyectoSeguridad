package pe.gob.bcrp.config;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import pe.gob.bcrp.util.Constantes;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Component
public class RateLimitingFilter implements Filter {

       private final ConcurrentMap<String, Bucket> bucketCache = new ConcurrentHashMap<>();

       @Override
       public void init(FilterConfig filterConfig) throws ServletException {
           // Inicialización si es necesaria
       }

       @Override
       public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
               throws IOException, ServletException {
           HttpServletRequest httpRequest = (HttpServletRequest) request;
           String key = generateKey(httpRequest);
           Bucket bucket = bucketCache.computeIfAbsent(key, this::createBucketForRequest);

           if (bucket.tryConsume(1)) {
               chain.doFilter(request, response);
           } else {

               HttpServletResponse httpResponse = (HttpServletResponse) response;
               httpResponse.setContentType("application/json");
               httpResponse.setCharacterEncoding("UTF-8");
               httpResponse.setStatus(429);

               // Construye el mensaje JSON manualmente
               String jsonResponse = "{ \"message\": \"" + Constantes.RATELIMIT + "\" }";
               httpResponse.getWriter().write(jsonResponse);

              // response.getWriter().write(Constantes.RATELIMIT);
              // ((jakarta.servlet.http.HttpServletResponse) response).setStatus(429);
           }
       }

       private String generateKey(HttpServletRequest request) {
           String ip = request.getRemoteAddr();
           String path = request.getRequestURI();
           String method = request.getMethod();
           String user = request.getRemoteUser();
           return ip + ":" + user + ":" + path + ":" + method;
       }

       // Límites específicos según el método HTTP
       private Bucket createBucketForRequest(String key) {
           Bandwidth limit;


           if (key.contains("GET")) {
               limit = Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(1)));
           } else if (key.contains("POST") || key.contains("PUT")) {
               limit = Bandwidth.classic(20, Refill.intervally(20, Duration.ofMinutes(1)));
           } else if (isFileUploadOrDownload(key)){
               limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1)));
           }else { // Default rate limit (opcional)
               limit = Bandwidth.classic(10, Refill.intervally(10, Duration.ofMinutes(1)));
           }

           return Bucket4j.builder()
                   .addLimit(limit)
                   .build();
       }

       @Override
       public void destroy() {
           // Limpieza si es necesaria
       }

       //lógica para detectar si es una petición de carga/descarga
       private boolean isFileUploadOrDownload(String key) {
        return key.contains("/upload") || key.contains("/download");
      }

}
