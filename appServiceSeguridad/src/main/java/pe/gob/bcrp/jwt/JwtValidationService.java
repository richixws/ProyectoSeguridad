package pe.gob.bcrp.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.JsonNode;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@Slf4j
public class JwtValidationService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

   // @Value("${keycloak.realm}")
    @Value("${keycloak.realm.name}")
    private String realm;

    private RestTemplate restTemplate;

    private RSAPublicKey publicKey;

    private RSAPrivateKey privateKey;

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
                                      .withIssuer(keycloakServerUrl + "/realms/" + realm)
                                      .build();

            // Verificar y decodificar el token
            DecodedJWT jwt = verifier.verify(token);

            //información adicional del token
            String subject = jwt.getSubject();
            log.info("Token valido para el usuario: ", subject);
            return true;

        } catch (JWTVerificationException e) {
            log.error("Token validation failed", e);
            return false;
        }
    }

    private RSAPublicKey getKeycloakPublicKey() throws Exception {
        // URL del JWKSet de Keycloak
        String jwkSetUrl = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/certs";

        // Obtener el JWKSet desde Keycloak
        JWKSet jwkSet = JWKSet.load(new URL(jwkSetUrl));

        // Obtener el primer JWK y convertirlo a RSAPublicKey
        JWK jwk = jwkSet.getKeys().get(0);

        // Asegúrate de que es una clave RSA
        if (jwk instanceof RSAKey) {
            return ((RSAKey) jwk).toRSAPublicKey();
        } else {
            throw new IllegalArgumentException("No RSA key found in JWKSet");
        }
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




}
