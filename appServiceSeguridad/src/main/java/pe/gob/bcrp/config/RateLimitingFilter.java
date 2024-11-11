package pe.gob.bcrp.config;


import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Duration;


@Component
public class RateLimitingFilter implements Filter {

    private final Bucket bucket;

    public RateLimitingFilter() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(30)));
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response);
        } else {
            response.getWriter().write("Error limite de solicitud");
//			response.setStatus(429);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Se puede realizar alguna inicialización si es necesario
    }

    @Override
    public void destroy() {
        // Se puede realizar alguna limpieza si es necesario
    }
}
