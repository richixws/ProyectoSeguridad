package pe.gob.bcrp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
    @Value("${allowed.cors.origins}")
    String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE")
                .allowedOrigins(allowedOrigins);

        /*registry
                .addMapping("/api/v1/oauth/captcha/**")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowedOrigins(allowedOrigins)
                .allowCredentials(true);*/
    }
}
