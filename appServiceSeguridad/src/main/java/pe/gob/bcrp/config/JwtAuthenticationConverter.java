package pe.gob.bcrp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {


    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.attribute-name}")
    private String principleAttribute;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities = Stream.concat(jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                        extractResourceRoles(jwt).stream())
                .toList();
        return new JwtAuthenticationToken(jwt, authorities, getPrincipleName(jwt));
    }


    // This method is used to extract the roles from the JWT token
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> realmAccess;
        Collection<String> realmRoles;

        // Verifica si el claim "realm_access" existe
        if (jwt.getClaim("realm_access") == null) {
            return List.of(); // Si no hay roles en el token, devuelve vacío
        }

        // Obtiene los accesos del realm desde el token
        realmAccess = jwt.getClaim("realm_access");

        if (realmAccess.get("roles") == null) {
            return List.of(); // Si no hay roles asociados
        }

        // Obtiene la lista de roles desde "roles"
        realmRoles = (Collection<String>) realmAccess.get("roles");

        // Verifica si contiene "role_admin"
        if (realmRoles.contains("role_admin")) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN")); // Mapea "role_admin" como "ROLE_ADMIN"
        }

        // Si no tiene "role_admin", devuelve vacío
        return List.of();
    }

    // This method is used to extract the principle name from the JWT token
    private String getPrincipleName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;

        if(principleAttribute!=null){
            claimName = principleAttribute;
        }
        return jwt.getClaim(claimName);

    }


}
