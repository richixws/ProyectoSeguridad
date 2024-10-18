package pe.gob.bcrp.jwt;

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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KeycloakRestService {
	
	@Autowired
    private RestTemplate restTemplate;


    @Value("${keycloak.token-uri}")
    private String keycloakTokenUri;

    @Value("${keycloak.user-info-uri}")
    private String keycloakUserInfo;

    @Value("${keycloak.logout}")
    private String keycloakLogout;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.authorization-grant-type}")
    private String grantType;
    
    @Value("${keycloak.authorization-grant-type-refresh}")
    private String grantTypeRefresh;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.scope}")
    private String scope;
    
    public String login(String username, String password) 
    {
    	MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    	map.add("username", username);
    	map.add("password",password);
    	map.add("client_id", this.clientId);
        map.add("grant_type", this.grantType);
        map.add("client_secret", this.clientSecret);
        map.add("scope",scope);
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

                responseMsg.put("message","Logout exitoso");
                return ResponseEntity.ok(responseMsg);
            } else {
               // responseMsg.put("message","Logout exitoso");
                return ResponseEntity.status(response.getStatusCode()).body("Error en el logout: " + response.getBody());
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error durante el logout: " + e.getMessage());
        }

      /**  try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, String> params = new HashMap<>();
            params.put("client_id", clientId);
            params.put("refresh_token", refreshToken);

            restTemplate.postForObject(keycloakLogout, params, String.class);

            return ResponseEntity.ok("Logout exitoso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error durante el logout: " + e.getMessage());
        }**/
    }


    /**
    public boolean logout(String refreshToken) {
       // String logoutUrl = keycloakLogoutEndpoint();  // URL del endpoint de logout de Keycloak
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", this.clientId);
        map.add("client_secret", this.clientSecret);
       // map.add("grant_type", this.grantType);
        map.add("refresh_token", refreshToken);


        // Crear las cabeceras HTTP (en caso de ser necesario)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Crear la entidad de la solicitud con los parámetros y las cabeceras
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(map, new HttpHeaders());

        try {
            // Realizar la petición HTTP POST para hacer logout

            this.restTemplate.postForObject(this.keycloakLogout, request, String.class);
           // restTemplate.postForEntity(this.keycloakLogout, request, String.class);
            return true;
            // Verificar si el código de respuesta es 200 OK
          //  return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            log.error("Error during Keycloak logout", e.getMessage());
            return false;
        }


     **/



    
}






















