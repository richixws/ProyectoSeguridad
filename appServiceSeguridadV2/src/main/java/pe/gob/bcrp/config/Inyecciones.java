package pe.gob.bcrp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Inyecciones {
	

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
