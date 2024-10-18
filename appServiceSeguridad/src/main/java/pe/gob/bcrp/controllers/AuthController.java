package pe.gob.bcrp.controllers;


import com.auth0.jwk.Jwk;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.JwtDTO;
import pe.gob.bcrp.dto.LoginDTO;
import pe.gob.bcrp.dto.TokenResponse;
import pe.gob.bcrp.dto.UsuarioDTO;
import pe.gob.bcrp.jwt.JwtService;
import pe.gob.bcrp.jwt.JwtValidationService;
import pe.gob.bcrp.jwt.KeycloakRestService;
import pe.gob.bcrp.services.IUsuarioService;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Autowired
    private IUsuarioService usuariosService;

    @Autowired
    private BCryptPasswordEncoder passwordEncode;

    @Autowired
    private KeycloakRestService keycloakRestService;

    @Value("${keycloak.client-user}")
    private String username;

    @Value("${keycloak.client-password}")
    private String contrasena;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtValidationService jwtValidationService;


    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody  @Valid LoginDTO dto) throws Exception {

        log.info("INI - login | requestURL=login");
        UsuarioDTO usuarioDTO =this.usuariosService.buscarPorUsuarioLogin(dto.getUsuario());

        if (usuarioDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "Las credenciales ingresadas no son válidas"));
        }


        if(this.passwordEncode.matches(usuarioDTO.getPassword(), dto.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "Las credenciales ingresadas no son válidas"));
        }

        String login = this.keycloakRestService.login(this.username, this.contrasena);
        JwtDTO jwt =new ObjectMapper().readValue(login, JwtDTO.class);



        //Jwk jwk = this.jwtService.getJwk();
        //String username = jwk.getClaimAsString("preferred_username");
        // Validar el token
        if (!jwtValidationService.validateToken(jwt.getAccess_token())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Token inválido"));
        }

            Map<String, String> response = new HashMap<>();
            response.put("id", String.valueOf(usuarioDTO.getIdUsuario()));
            response.put("nombre", usuarioDTO.getPersona().getNombres().concat(usuarioDTO.getPersona().getApellidoPaterno()));
            response.put("token", jwt.getAccess_token());
            response.put("refreshToken",jwt.getRefresh_token());
            return ResponseEntity.ok(response);
    }


    @PostMapping("oauth/validarToken")
   public ResponseEntity<?> ValidarToken(@RequestHeader("Authorization") String authHeader) {
   // public String ValidarToken(@RequestParam("Authorization") String authHeader) {
        // El token viene en el formato "Bearer <token>", así que lo extraemos
        String token = authHeader.replace("Bearer ", "");

        boolean isValid = jwtValidationService.validateToken(token);

        Map<String, String> response = new HashMap<>();
        if (isValid) {
            response.put("message","Token valido");
            return ResponseEntity.ok(response);
        } else {
            response.put("message","Token Invalido o Expirado");
            return ResponseEntity.badRequest().body(response);
        }
    }


    @PostMapping("oauth/refreshToken")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refresh_token");
        TokenResponse newTokens = jwtValidationService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(newTokens);
    }



    @PostMapping("oauth/logout")
    public ResponseEntity<?> cerrarSesion(@RequestParam("refreshToken") String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            return new ResponseEntity<>("Refresh token was expired or missing. Please make a new signin request",HttpStatus.FORBIDDEN);
        }

        try {
            // Llamar a Keycloak para revocar el refresh token
            ResponseEntity<?> estado = keycloakRestService.logout(refreshToken);
            return estado;

        } catch (Exception e) {
            log.error("Error during logout", e);
            return new ResponseEntity<>("An error occurred while trying to logout", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


   /** @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroDTO registroDTO){
          UsuarioDTO usuarioReg =this.usuariosService.buscarPorUsuarioLogin(registroDTO.getUsuario());
          if(usuarioReg!=null){

          }

    }**/



}
