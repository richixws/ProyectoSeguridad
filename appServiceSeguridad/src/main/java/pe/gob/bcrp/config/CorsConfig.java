package pe.gob.bcrp.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
//@EnableWebMvc
public class CorsConfig {
    @Value("${allowed.cors.origins}")
    private String allowedOrigins;

@Bean
public CorsFilter corsFilter() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowCredentials(true);
    //configuration.addAllowedOrigin(allowedOrigins);
    configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split("\\s*,\\s*")));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));// Replace with your frontend URL
    configuration.addAllowedHeader("*");
    //configuration.addAllowedMethod("*");

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return new CorsFilter(source);
}


}
