package pe.gob.bcrp.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import pe.gob.bcrp.dto.response.TokenResponse;
import pe.gob.bcrp.excepciones.SeguridadAPIException;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Service
@Slf4j
public class JwtValidationService {

    @Value("${keycloak.issuer}")
    private String keycloakIssuerUrl;

    @Value("${keycloak.token-uri}")
    private  String urlToken;

    @Value("${keycloak.jwk-set-uri}")
    private String urlCerts;

    @Value("${keycloak.client-id}")
    private String clientId;

    //@Value("${keycloak.client-secret}")
    //private String clientSecret;

    private RestTemplate restTemplate;

    private RSAPublicKey publicKey;

    private RSAPrivateKey privateKey;


    //private static final String TOKEN_ENDPOINT = "/protocol/openid-connect/token";
    public JwtValidationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        try {
            publicKey = null; // Inicialización diferida al validar el token
        } catch (Exception e) {
            log.error("Error initializing JWT validation service", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            // Decodifica el token para extraer el `kid`
            DecodedJWT jwt = JWT.decode(token);
            String kid = jwt.getKeyId();

            // Cargar y obtener la clave pública correcta del `JWKSet` usando el `kid`
            publicKey = getKeycloakPublicKey(kid);

            // Crear el algoritmo RSA256 con la llave pública
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);

            // Crear el verificador del token
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.keycloakIssuerUrl)
                    .build();

            // Verificar y decodificar el token
            verifier.verify(token);
            String subject = jwt.getSubject();
            log.info("Token válido para el usuario: {}", subject);
            return true;

        } catch (JWTVerificationException ex) {
            throw new SeguridadAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (SeguridadAPIException ex) {
            throw new SeguridadAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (IllegalArgumentException ex) {
            throw new SeguridadAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private RSAPublicKey getKeycloakPublicKey(String kid) throws Exception {
        // Cargar el conjunto de claves (JWKSet) de Keycloak
        JWKSet jwkSet = JWKSet.load(new URL(urlCerts));

        // Buscar la clave que coincide con el `kid`
        JWK jwk = jwkSet.getKeys().stream()
                .filter(key -> key.getKeyID().equals(kid) && key instanceof RSAKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No matching RSA key found in JWKSet"));

        return ((RSAKey) jwk).toRSAPublicKey();
    }


    public TokenResponse refreshAccessToken(String refreshToken) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);
        body.add("client_id", clientId);
        //body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        //String url = keycloakServerUrl + "/realms/" + realm + TOKEN_ENDPOINT;

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(urlToken, request, TokenResponse.class);
        return response.getBody();
    }



}
