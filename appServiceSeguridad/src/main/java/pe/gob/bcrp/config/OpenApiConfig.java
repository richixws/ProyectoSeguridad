package pe.gob.bcrp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI openAPI() {
        // Datos de los servidores
        //http://localhost:8081/v3/api-docs
        //http://localhost:8081/swagger-ui/index.html
        Server devServer = new Server();
        devServer.setUrl("http://172.30.107.212:1025");
        devServer.setDescription("URL de pruebas desarrollo");

        Server prodServer = new Server();
        prodServer.setUrl("http://172.30.107.212:1025");
        prodServer.setDescription("URL del servidor de producción");

        Server localServer = new Server();
        localServer.setUrl("http://localhost:8081");
        localServer.setDescription("URL de prueba local");

        // Datos de contacto
        Contact contact = new Contact();
        contact.setEmail("bcrp@gob.com");
        contact.setName("BCRP");
        contact.setUrl("https://bcrp.com");

        // Datos de la API
        License mitLicense = new License()
                .name("Licencia MIT")
                .url("https://bcrp.gob.pe/license");

        Info info = new Info()
                .title("Documentación de App Seguridad BCRP")
                .version("1.0")
                .contact(contact)
                .description("Esta API expone los endpoints para usar la App Seguridad BCRP.")
                .termsOfService("https://bcrp.gob.pe")
                .license(mitLicense);

        // Configuración de la seguridad
        SecurityRequirement securityRequirement = new SecurityRequirement().
                addList("Bearer Authentication");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");

        Components components = new Components().addSecuritySchemes("Bearer Authentication", securityScheme);

        // Integración de la configuración
        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer,localServer))
                .addSecurityItem(securityRequirement)
                .components(components);
    }

}
