package pe.gob.bcrp.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

@Configuration
public class TemplateEngineConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimitFilter() {
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RateLimitingFilter());
        registrationBean.addUrlPatterns(
                "/api/v1/usuarios",
                "/api/v1/usuario"
        );
        registrationBean.setInitParameters(Collections.singletonMap("methods", "GET,POST PUT"));
        return registrationBean;
    }


}
