package pe.gob.bcrp.controllers;


import cn.apiclub.captcha.Captcha;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.response.CaptchaResponse;
import pe.gob.bcrp.dto.response.TokenResponse;
import pe.gob.bcrp.entities.Persona;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.excepciones.SeguridadAPIException;
import pe.gob.bcrp.jwt.JwtService;
import pe.gob.bcrp.jwt.JwtValidationService;
import pe.gob.bcrp.jwt.KeycloakRestService;
import pe.gob.bcrp.services.IUsuarioService;
import pe.gob.bcrp.util.CaptchaServiceGenerate;

import java.util.*;

@Log4j2
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Oauth",description = "Operaciones de seguridad de la aplicacion - login, refresh Token, cerrar Sesion, obtener Captcha")
public class AuthController {


    private IUsuarioService usuariosService;
    private BCryptPasswordEncoder passwordEncode;
    private KeycloakRestService keycloakRestService;
    private JwtService jwtService;
    private JwtValidationService jwtValidationService;

    public AuthController(IUsuarioService usuarioService,BCryptPasswordEncoder passwordEncode, KeycloakRestService keycloakRestService, JwtService jwtService, JwtValidationService jwtValidationService ) {
        this.usuariosService = usuarioService;
        this.passwordEncode = passwordEncode;
        this.keycloakRestService = keycloakRestService;
        this.jwtService = jwtService;
        this.jwtValidationService=jwtValidationService;
    }


    @Operation(summary = "Login REST API", description = "Permite autenticar un usuario mediante sus credenciales y devuelve un token JWT con información adicional")
    //@ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")

    @ApiResponses({ @ApiResponse(responseCode = "200",description = "Autenticación exitosa, devuelve el token JWT."),
                             @ApiResponse(responseCode = "401",description = "Credenciales inválidas o token no válido." ),
                   @ApiResponse( responseCode = "422",description = "Error interno en el sistema." )
    })
    @PostMapping(value = "oauth/login")
    public ResponseEntity<?> login(@RequestBody  @Valid LoginDTO dto) throws Exception {

        log.info("INI - login | requestURL=login");

        try {

           /** String tokenUuid = (String) session.getAttribute("uuid");
            if(tokenUuid == null){
                Map<String, String> response = Map.of("mensaje", "Por favor generar un nuevo captcha");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if(!dto.getCaptcha().equals(dto.getHiddenCaptcha())){
                Map<String, String> response = Map.of("mensaje", "Captcha inválido");
                session.invalidate();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            } else if(!dto.getTokenUuid().equals(tokenUuid)){
                Map<String, String> response = Map.of("mensaje", "Token captcha inválido");
                session.invalidate();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }  **/

           UsuarioDTO usuarioDTO =this.usuariosService.buscarPorUsuarioLogin(dto.getUsername());

            if (usuarioDTO == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Las credenciales ingresadas no son válidas"));
            }




            String login = this.keycloakRestService.login(dto.getUsername(), dto.getPassword());
            JwtDTO jwt =new ObjectMapper().readValue(login, JwtDTO.class);

            // Decodificar el payload del token para obtener el nombre
            String nombre = this.keycloakRestService.extractNameFromToken(jwt.getAccess_token());


            /**boolean estadoOtp= usuariosService.regenerateOtp(usuarioDTO.getPersona().getCorreo());
            if(estadoOtp){
                log.info("se envio en codigo verificador");
            }**/

            // Validar el token
           /**if (!jwtValidationService.validateToken(jwt.getAccess_token())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "Token inválido"));
            }**/

            Map<String, String> response = new HashMap<>();
           // response.put("id", String.valueOf(usuarioDTO.getIdUsuario()));
           // response.put("nombre", usuarioDTO.getPersona().getNombres().concat(" "+usuarioDTO.getPersona().getApellidoPaterno()));
            response.put("name", nombre);
            response.put("access_token", jwt.getAccess_token());
            response.put("expires_in", String.valueOf(jwt.getExpires_in()));
            response.put("refresh_token",jwt.getRefresh_token());
            response.put("refresh_expires_in", String.valueOf(jwt.getRefresh_expires_in()));
            return ResponseEntity.ok(response);


        } catch (HttpClientErrorException e) {
            // Captura de error 401 o 400 para indicar credenciales inválidas
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("message", "Las credenciales ingresadas no son válidas"));
            } else {
                log.error("Error en la solicitud de autenticación", e);
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                        .body(Map.of("message", "Ocurrió un error en el sistema"));
            }
        } catch (Exception e) {
            log.error("Error en el login", e);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Map.of("message", "Ocurrió un error interno en el sistema"));
        }

    }

   // @Operation(summary = "Validar Token REST API", description = "Validar token de acceso")
   // @ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @Hidden
    @PostMapping("oauth/validarToken")
   public ResponseEntity<?> ValidarToken(@RequestHeader("Authorization") String authHeader) {

        log.info("INI - validarToken");
        String token = authHeader.replace("Bearer ", "").trim();
        Map<String, String> response = new HashMap<>();
        try {
            boolean isValid = jwtValidationService.validateToken(token);

            if (isValid) {
                response.put("message","Token valido");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Token Invalido o Expirado");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (SeguridadAPIException e) {
            log.error("Token Invalido o Expirado: {}", e.getMessage());
            response.put("message", "Token Invalido o Expirado");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            log.error("Error en validarToken: {}", e.getMessage());
            response.put("message", "Error al procesar el token");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }

    }

    @Operation(summary = "Refresh Token REST API", description = "Obtener nuevo token de acceso")
    //@ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refrescado correctamente, devuelve el token JWT."),
            @ApiResponse(responseCode = "403", description = "Forbidden. No se ha proporcionado un refresh_token."),
            @ApiResponse(responseCode = "422", description = "Ocurrió un error al procesar la solicitud.")
    })
    @PostMapping("oauth/refreshToken")
    public ResponseEntity<TokenResponse> refreshToken(@RequestParam("refresh_token") RefreshTokenRequest refresh_token,@RequestParam("username") String username) {

        log.info("INI - refreshToken");
        try {
            //String refreshToken = request.get("refresh_token");
            String refreshToken = refresh_token.getRefresh_token();
            if(refreshToken ==null || refreshToken.isEmpty()){
                log.error("El refresh_token no está presente o es inválido.");
                return new ResponseEntity<TokenResponse>(HttpStatus.FORBIDDEN);
            }
            ResponseEntity newTokens = jwtValidationService.refreshAccessToken(refreshToken,username);
            return newTokens;
        } catch (Exception e) {
            log.error("Error al intentar refrescar el token: ", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }

    }


    @Operation(summary = "Cerrar Sesion REST API", description = "cerrar la sesion de acceso")
    //@ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sesión cerrada correctamente."),
            @ApiResponse(responseCode = "403", description = "Forbidden. El refresh_token ha expirado o está ausente."),
            @ApiResponse(responseCode = "422", description = "Ocurrió un error al intentar cerrar sesión.")
    })
    @PostMapping("oauth/logout")
    public ResponseEntity<?> cerrarSesion(@RequestParam("refresh_token") String refreshToken,@RequestParam("username") String username) {

        log.error("INI - logout");
        try {
            if (refreshToken == null || refreshToken.isEmpty()) {
                return new ResponseEntity<>("\n" +
                        "El token de actualización expiró o faltaba. Por favor, haz una nueva solicitud de inicio de sesión.",HttpStatus.FORBIDDEN);
            }

            try {
                // Llamar a Keycloak para revocar el refresh token
                ResponseEntity<?> estado = keycloakRestService.logout(refreshToken,username);
                return estado;

            } catch (Exception e) {
                log.error("Error during logout", e.getMessage());
                return new ResponseEntity<>("Se produjo un error al intentar cerrar sesión.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (Exception e) {
            log.error("Error during logout", e.getMessage());
            throw new RuntimeException(e);
        }

    }


    //@Operation(summary = "Captcha REST API", description = "obtener captcha de acceso")
    //@ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
    @Hidden
    @GetMapping("oauth/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha(HttpSession session) {

        log.error("INI - getCaptcha");
        Captcha captcha = CaptchaServiceGenerate.createCaptcha(240, 70);
        String encodedCaptcha = CaptchaServiceGenerate.encodeCaptcha(captcha);

        // Extraer la operación del captcha
        String hiddenCaptcha = captcha.getAnswer();
        int resultadoOperacion = evaluarOperacion(hiddenCaptcha);

        String uuid = UUID.randomUUID().toString().replace("-", "");
        session.setAttribute("uuid", uuid);

        CaptchaResponse response = new CaptchaResponse();
        response.setCaptchaImage(encodedCaptcha);
        response.setHiddenCaptcha(String.valueOf(resultadoOperacion));
        response.setTokenUuid(session.getAttribute("uuid").toString());

        return ResponseEntity.ok(response);
    }

    // Método para evaluar la operación de suma
    public static int evaluarOperacion(String operacion) {
        String[] numeros = operacion.split(" \\+ "); // Divide en los dos operandos
        return Integer.parseInt(numeros[0].trim()) + Integer.parseInt(numeros[1].trim());
    }


    @Hidden
    @PutMapping("oauth/regenerate-otp")
    public ResponseEntity<Boolean> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(usuariosService.regenerateOtp(email), HttpStatus.OK);
    }

    @Hidden
    @PostMapping(value = "oauth/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody @Valid OtpVerificationDTO  dto) {
        log.info("INI - verifyOtp | requestURL=verify-otp");

        String username = dto.getUsername();
        String otp = dto.getOtp();

        boolean isOtpValid = usuariosService.validateOTP(username, otp);
        if (!isOtpValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Código inválido o expirado"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","Verificacion Correcta"));

    }

}
