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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pe.gob.bcrp.dto.CaptchaResponse;
import pe.gob.bcrp.dto.TokenResponse;
import pe.gob.bcrp.excepciones.SeguridadAPIException;
import java.net.URL;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;

@Service
@Slf4j
public class JwtValidationService {

    @Value("${kecloak.issuer}")
    private String keycloakIssuerUrl;

    @Value("${keycloak.token-uri}")
    private  String urlToken;

    @Value("${keycloak.jwk-set-uri}")
    private String urlCerts;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private RestTemplate restTemplate;

    private RSAPublicKey publicKey;

    private RSAPrivateKey privateKey;


    //recaptcha valores
    @Value("${google.recaptcha.verification.endpoint}")
    String recaptchaEndpoint;

    @Value("${google.recaptcha.secret}")
    String recaptchaSecret;

    //private static final String TOKEN_ENDPOINT = "/protocol/openid-connect/token";
    public JwtValidationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        try {
            // Obtener la clave pública de Keycloak al iniciar el servicio
           // String publicKeyPEM = getKeycloakPublicKey();
           // publicKey = parsePublicKey(publicKeyPEM);
            publicKey = getKeycloakPublicKey();
        } catch (Exception e) {
            log.error("Error initializing JWT validation service", e);
        }
    }

    public boolean validateToken(String token) {

        try {
            // Crear el algoritmo RSA256 con la llave pública
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            // Crear el verificador del token
            JWTVerifier verifier = JWT.require(algorithm)
                                      .withIssuer(this.keycloakIssuerUrl)
                                      .build();
            // Verificar y decodificar el token
            DecodedJWT jwt = verifier.verify(token);
            //información adicional del token
            String subject = jwt.getSubject();
            log.info("Token valido para el usuario: ", subject);
            return true;

        } catch (JWTVerificationException ex) { // Esto captura el resto de errores de verificación
            throw new SeguridadAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (SeguridadAPIException ex) {
            throw new SeguridadAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (IllegalArgumentException ex) {
            throw new SeguridadAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }

    private RSAPublicKey getKeycloakPublicKey() throws Exception {
        // URL del JWKSet de Keycloak
        //String jwkSetUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/certs";
        // Obtener el JWKSet desde Keycloak
        JWKSet jwkSet = JWKSet.load(new URL(urlCerts));
        // Obtener el primer JWK y convertirlo a RSAPublicKey
        JWK jwk = jwkSet.getKeys().get(0);
        // Asegúrate de que es una clave RSA
        if (jwk instanceof RSAKey) {
            return ((RSAKey) jwk).toRSAPublicKey();
        } else {
            throw new IllegalArgumentException("No RSA key found in JWKSet");
        }
    }


    public TokenResponse refreshAccessToken(String refreshToken) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("refresh_token", refreshToken);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        //String url = keycloakServerUrl + "/realms/" + realm + TOKEN_ENDPOINT;

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(urlToken, request, TokenResponse.class);
        return response.getBody();
    }



/**
    private String getKeycloakPublicKey() {
        String url = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/certs";
        JsonNode response = restTemplate.getForObject(url, JsonNode.class);

        // Obtener la clave pública del response
        return response.get("keys").get(0).get("n").asText();
    }

    private RSAPublicKey parsePublicKey(String publicKeyPEM) throws Exception {
        // Implementación de la conversión de PEM a RSAPublicKey
        // Este es un ejemplo simplificado, deberías usar una biblioteca como Bouncy Castle
       // byte[] publicKeyDER = Base64.getDecoder().decode(publicKeyPEM);
        byte[] publicKeyDER = Base64.getUrlDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyDER));
    }

  **/

// method validate the captcha response coming from the client
// and return either true or false after the validation.
// reference url - https://developers.google.com/recaptcha/docs/verify
public boolean validateCaptcha(final String captchaResponse) {
    log.info("Going to validate the captcha response = {}", captchaResponse);
    final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    // "secret" is a required param and it represents the shared key between your site
    // and the recaptcha.
    params.add("secret", recaptchaSecret);
    // "response" is a required param and it represents the user token provided
    // by the recaptcha client-side integration on your site.
    params.add("response", captchaResponse);

    CaptchaResponse apiResponse = null;
    try {
        apiResponse = restTemplate.postForObject(recaptchaEndpoint, params, CaptchaResponse.class);
    } catch (final RestClientException e) {
        log.error("Some exception occurred while binding to the recaptcha endpoint.", e);
    }

    if (Objects.nonNull(apiResponse) && apiResponse.isSuccess()) {
        log.info("Captcha API response = {}", apiResponse.toString());
        return true;
    } else {
        return false;
    }
}




}
