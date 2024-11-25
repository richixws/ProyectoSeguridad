package pe.gob.bcrp.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KeycloakRestService {
	
	@Autowired
    private RestTemplate restTemplate;


    @Value("${keycloak.token-uri}")
    private String keycloakTokenUri;

    @Value("${keycloak.logout}")
    private String keycloakLogout;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.authorization-grant-type}")
    private String grantType;
    
    @Value("${keycloak.authorization-grant-type-refresh}")
    private String grantTypeRefresh;

   // @Value("${keycloak.client-secret}")
   // private String clientSecret;

      @Value("${keycloak.scope}")
      private String scope;
    
    public String login(String username, String password) {
    	MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    	map.add("username", username);
    	map.add("password",password);
    	map.add("client_id", this.clientId);
        map.add("grant_type", this.grantType);
        map.add("scope",scope);
      //  map.add("client_secret", this.clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(map, new HttpHeaders());
        return this.restTemplate.postForObject(this.keycloakTokenUri, request, String.class);
    }


    public ResponseEntity<?> logout(String refreshToken) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("client_id", clientId);
            map.add("refresh_token", refreshToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(keycloakLogout, request, String.class);

            Map<String, String> responseMsg = new HashMap<>();
            if (response.getStatusCode() == HttpStatus.NO_CONTENT) {

                responseMsg.put("message", "Logout exitoso");
                return ResponseEntity.ok(responseMsg);
            } else {
                // responseMsg.put("message","Logout exitoso");
                return ResponseEntity.status(response.getStatusCode()).body("Error en el logout: " + response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error durante el logout: " + e.getMessage());
        }
    }

    /**
     * Decodificar el nombre del usuario del payload del token JWT.
     *
     * @param token JWT
     * @return Nombre del usuario o null si no se encuentra
     */
    public String extractNameFromToken(String token) throws Exception {
        try {
            // Dividir el token en sus partes
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                throw new IllegalArgumentException("JWT no tiene el formato adecuado");
            }

            // Decodificar el payload (segunda parte)
            String payload = new String(Base64.getDecoder().decode(parts[1]));

            // Convertir el payload JSON a un mapa
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> claims = objectMapper.readValue(payload, Map.class);

            // Retornar el valor del claim "name" (o el campo correspondiente)
            return (String) claims.get("name");
        } catch (Exception e) {
            log.error("Error al decodificar el token JWT", e);
            throw e;
        }
    }

}






















