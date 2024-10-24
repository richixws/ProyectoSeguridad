package pe.gob.bcrp.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;

import jakarta.ws.rs.ForbiddenException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.gob.bcrp.dto.*;
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

   // @Value("${keycloak.client-user}")
   // private String username;

  //  @Value("${keycloak.client-password}")
  //  private String contrasena;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtValidationService jwtValidationService;



    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody  @Valid LoginDTO dto) throws Exception {

        log.info("INI - login | requestURL=login");

        try {

            /**if (jwtValidationService.verify(dto.getCaptchaToken()).isSuccess()) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED)
                        .body(Map.of("mensaje", "Captcha inválido"));
            }**/

             /**  final boolean isValidCaptcha = jwtValidationService.validateCaptcha(dto.getCaptchaResponse());
            if (!isValidCaptcha) {
                log.info("Throwing forbidden exception as the captcha is invalid.");
                throw new ForbiddenException("INVALID_CAPTCHA");
            }**/

            UsuarioDTO usuarioDTO =this.usuariosService.buscarPorUsuarioLogin(dto.getUsuario());

            if (usuarioDTO == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Las credenciales ingresadas no son válidas"));
            }

            if(!this.passwordEncode.matches(dto.getPassword(), usuarioDTO.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "Las credenciales ingresadas no son válidas"));
            }

            //String login = this.keycloakRestService.login(this.username, this.contrasena);
            String login = this.keycloakRestService.login(dto.getUsuario(), dto.getPassword());
            JwtDTO jwt =new ObjectMapper().readValue(login, JwtDTO.class);


            // Validar el token
            if (!jwtValidationService.validateToken(jwt.getAccess_token())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "Token inválido"));
            }

            Map<String, String> response = new HashMap<>();
            response.put("id", String.valueOf(usuarioDTO.getIdUsuario()));
            response.put("nombre", usuarioDTO.getPersona().getNombres().concat(" "+usuarioDTO.getPersona().getApellidoPaterno()));
            response.put("token", jwt.getAccess_token());
            response.put("refreshToken",jwt.getRefresh_token());
            return ResponseEntity.ok(response);


        } catch (Exception e) {
            log.error("Error en el login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("mensaje", "Ocurrió un error interno en el sistema"));
        }

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
         if(refreshToken ==null){
            return new ResponseEntity<TokenResponse>(HttpStatus.FORBIDDEN);
         }
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
