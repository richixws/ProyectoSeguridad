package pe.gob.bcrp.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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



    
}






















