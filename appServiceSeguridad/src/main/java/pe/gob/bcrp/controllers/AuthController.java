package pe.gob.bcrp.controllers;


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

    //@Autowired
    //private PerfilService perfilService;

   // @Autowired
   // private VariablesGlobalesService variablesGlobalesService;

    @Autowired
    private BCryptPasswordEncoder passwordEncode;

   // @Autowired
   // private Utilidades utilidades;

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
    public ResponseEntity<?> login(@RequestBody  @Valid LoginDTO dto) throws JsonMappingException, JsonProcessingException{
        log.info("INI - login | requestURL=login");
      //UsuarioModel usuario = this.usuariosService.buscarPorCorreoLogin(dto.getCorreo());
        UsuarioDTO usuarioDTO =this.usuariosService.buscarPorUsuarioLogin(dto.getUsuario());

        if (usuarioDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "Las credenciales ingresadas no son válidas"));
        }

        if (!dto.getPassword().matches(usuarioDTO.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "Las credenciales ingresadas no son válidas"));
        }

        String login = this.keycloakRestService.login(this.username, this.contrasena);
        JwtDTO jwt =new ObjectMapper().readValue(login, JwtDTO.class);


        // Validar el token
        if (!jwtValidationService.validateToken(jwt.getAccess_token())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Token inválido"));
        }

            Map<String, String> response = new HashMap<>();
            response.put("id", String.valueOf(usuarioDTO.getIdUsuario()));
            response.put("nombre", usuarioDTO.getPersona().getNombres());
            response.put("token", jwt.getAccess_token());

            return ResponseEntity.ok(response);
    }


    @PostMapping("/validar-token")
    public String ValidarToken(@RequestHeader("Authorization") String authHeader) {
        // El token viene en el formato "Bearer <token>", así que lo extraemos
        String token = authHeader.replace("Bearer ", "");

        boolean isValid = jwtValidationService.validateToken(token);
        if (isValid) {
            return "Token válido. Acceso permitido.";
        } else {
            return "Token inválido o expirado.";
        }
    }

  /**  @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }**/



   /** @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroDTO registroDTO){


          UsuarioDTO usuarioReg =this.usuariosService.buscarPorUsuarioLogin(registroDTO.getUsuario());
          if(usuarioReg!=null){

          }

    }**/



}
