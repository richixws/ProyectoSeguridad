package pe.gob.bcrp.controllers;


import cn.apiclub.captcha.Captcha;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.mfaDTO.OtpVerificationDTO;
import pe.gob.bcrp.dto.mfaDTO.Response;
import pe.gob.bcrp.dto.response.CaptchaResponse;
import pe.gob.bcrp.dto.response.TokenResponse;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
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


    @Operation(summary = "Login REST API",description = "Permite autenticar un usuario mediante sus credenciales")
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseTokenDTO.class),
                    examples = @ExampleObject(value = """
                    {
                      "data": {
                        "name": "",
                        "access_token": "",
                        "expires_in": "",
                        "refresh_token": "",
                        "refresh_expires_in": ""
                      },
                      "message": "Autenticación exitosa"
                    }
                    """))),
                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTokenDTO.class),
                    examples = @ExampleObject(value = """
                    {
                      "message": "Las credenciales ingresadas no son válidas"
                    }"""))),
                    @ApiResponse(responseCode = "422", description = "Error interno",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseTokenDTO.class),
                    examples = @ExampleObject(value = """
                    {
                      "message": "Ocurrió un error interno en el sistema"
                    }"""))),
                   @ApiResponse(responseCode = "400", description = "Solicitud invalida",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseTokenDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "message": "Solicitud invalida de autenticación."
                    }""")))
    })

    @PostMapping(value = "oauth/login")
    public ResponseEntity<ResponseTokenDTO> login(@RequestBody  @Valid LoginDTO dto) throws Exception {

        log.info("INI - login | requestURL=login");
        ResponseTokenDTO responseToken = new ResponseTokenDTO();

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
               // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Las credenciales ingresadas no son válidas"));
                responseToken.setMessage("Las credenciales ingresadas no son válidas.");
                return new ResponseEntity<>(responseToken,HttpStatus.UNAUTHORIZED);
            }


            String login = this.keycloakRestService.login(dto.getUsername(), dto.getPassword());
            JwtDTO jwt =new ObjectMapper().readValue(login, JwtDTO.class);

            //Decodificar el payload del token para obtener el nombre
            String nombre = this.keycloakRestService.extractNameFromToken(jwt.getAccess_token());
           // String correo = this.keycloakRestService.extractEmailFromToken(jwt.getAccess_token());
           // String username = this.keycloakRestService.extractUsernameFromToken(jwt.getAccess_token());

            //Generar un nuevo codigo verificador
            /**Response estadoOtp= usuariosService.regenerateOtp(usuarioDTO.getPersona().getCorreo());  //usuarioDTO.getPersona().getCorreo()
            if(estadoOtp.getStatusCode()==200){
                log.info("se envio en codigo verificador al correo {}", usuarioDTO.getPersona().getCorreo());
            }else{
                log.info("Error - codigo verificador no enviado al correo {}", usuarioDTO.getPersona().getCorreo());
            }**/

            // Validar el token
           /**if (!jwtValidationService.validateToken(jwt.getAccess_token())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("mensaje", "Token inválido"));
            }**/

            TokenJwtDTO tokenJwtDTO = new TokenJwtDTO();
            tokenJwtDTO.setName(nombre);
            tokenJwtDTO.setAccess_token(jwt.getAccess_token());
            tokenJwtDTO.setExpires_in(jwt.getExpires_in());
            tokenJwtDTO.setRefresh_token(jwt.getRefresh_token());
            tokenJwtDTO.setRefresh_expires_in(jwt.getRefresh_expires_in());

            responseToken.setData(tokenJwtDTO);
            responseToken.setMessage("Autenticación exitosa.");
            return  ResponseEntity.ok(responseToken);


        } catch (HttpClientErrorException e) {
            // Captura de error 401 o 400 para indicar credenciales inválidas
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                log.error("credenciales válidas", e.getMessage());
                //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Las credenciales ingresadas no son válidas"));
                responseToken.setMessage("Las credenciales ingresadas no son válidas.");
                responseToken.setData(null);
                return new ResponseEntity<>(responseToken,HttpStatus.UNAUTHORIZED);
            } else {
                log.error("Error en la solicitud de autenticación.", e);
                //return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Map.of("message", "Ocurrió un error en el sistema"));
                responseToken.setMessage("Ocurrió un error en el sistema.");
                return new ResponseEntity<>(responseToken,HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }catch (ResourceNotFoundException e){
            log.error("Error Not Found", e.getMessage());
            responseToken.setMessage(e.getMessage());
            return new ResponseEntity<>(responseToken,HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            log.error("Error en el login", e);
            responseToken.setMessage("Ocurrió un error interno en el sistema");
            return new ResponseEntity<>(responseToken,HttpStatus.UNPROCESSABLE_ENTITY);
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
    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseTokenDTO.class),
                    examples = @ExampleObject(value = """
                    {
                      "data": {
                        "access_token": "",
                        "expires_in": "",
                        "refresh_token": "",
                        "refresh_expires_in": ""
                      },
                      "message": "Token refrescado correctamente."
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida refreshToken",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseTokenDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "message": "Solicitud invalida de refreshToken."
                    }"""))),
            @ApiResponse(responseCode = "422", description = "Error interno",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseTokenDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "message": "Ocurrió un error al procesar la solicitud."
                    }"""))),
            @ApiResponse(responseCode = "403", description = "Forbidden. No se ha proporcionado un refresh_token.",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseTokenDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "message": "El refresh_token no está presente o es inválido."
                    }""")))
    })


    @PostMapping("oauth/refreshToken")
    public ResponseEntity<ResponseTokenDTO> refreshToken(@RequestParam("refresh_token") RefreshTokenRequest refresh_token,@RequestParam("username") String username) {

        log.info("INI - refreshToken");
        ResponseTokenDTO responseToken = new ResponseTokenDTO();

        try {
            //String refreshToken = request.get("refresh_token");
            String refreshToken = refresh_token.getRefresh_token();
            if(refreshToken ==null || refreshToken.isEmpty()){
                responseToken.setMessage("El refreshToken no está presente o es inválido.");
                return new ResponseEntity<>(responseToken,HttpStatus.FORBIDDEN);
            }
            ResponseEntity newTokens = jwtValidationService.refreshAccessToken(refreshToken,username);

            if(newTokens.getStatusCode()==HttpStatus.UNAUTHORIZED){
                Map<String, String> errorBody = (Map<String, String>) newTokens.getBody();
                responseToken.setMessage(errorBody != null ? errorBody.getOrDefault("message", "No autorizado.") : "No autorizado.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseToken);
            }

            TokenResponse tokenResponse = (TokenResponse) newTokens.getBody();

            responseToken.setMessage("Token refrescado correctamente.");
            responseToken.setData(tokenResponse);
            return new ResponseEntity<>(responseToken,HttpStatus.OK);
        }

        catch (RuntimeException e){
            log.error("Error al refrescar el token: ", e.getMessage());
            responseToken.setMessage(e.getMessage());
            responseToken.setData(null);
            return new ResponseEntity<>(responseToken,HttpStatus.UNPROCESSABLE_ENTITY);
        }
        catch (Exception e) {
            log.error("Error al intentar refrescar el token: ", e.getMessage());
            responseToken.setMessage("Ocurrió un error al intentar refrescar el token.");
            responseToken.setData(null);
            return new ResponseEntity<>(responseToken,HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }


    @Operation(summary = "Cerrar Sesion REST API", description = "cerrar la sesion de acceso")
    //@ApiResponse( responseCode = "200", description = "HTTP Status 200 SUCCESS")
   /** @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sesión cerrada correctamente."),
            @ApiResponse(responseCode = "403", description = "Forbidden. El refresh_token ha expirado o está ausente."),
            @ApiResponse(responseCode = "422", description = "Ocurrió un error al intentar cerrar sesión.")
    })**/

    @ApiResponses({ @ApiResponse(responseCode = "200", description = "Logout exitoso",
            content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseTokenDTO.class),
                    examples = @ExampleObject(value = """
                    {
                      "message": "Logout exitoso."
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida logout.",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseTokenDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "message": "Solicitud invalida."
                    }"""))),
            @ApiResponse(responseCode = "401", description = "Token no esta activo.",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseTokenDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "message": "Token no esta activo."
                    }"""))),

            @ApiResponse(responseCode = "422", description = "Error de solicitud de sistema.",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseTokenDTO.class),
                            examples = @ExampleObject(value = """
                    {
                      "message": "Ocurrió un error al procesar la solicitud."
                    }""")))
    })
    @PostMapping("oauth/logout")
    public ResponseEntity<?> cerrarSesion(@RequestParam("refresh_token") String refreshToken,@RequestParam("username") String username) {

        log.error("INI - logout");
        ResponseTokenDTO responseToken = new ResponseTokenDTO();
        try {
            if (refreshToken == null || refreshToken.isEmpty()) {
               // return new ResponseEntity<>("\n" + "El token de actualización expiró o faltaba. Por favor, haz una nueva solicitud de inicio de sesión.",HttpStatus.FORBIDDEN);
                responseToken.setMessage("El token de actualización expiró o faltaba. Por favor, haz una nueva solicitud de inicio de sesión.");
                return new ResponseEntity<>(responseToken,HttpStatus.FORBIDDEN);

            }

            try {
                // Llamar a Keycloak para revocar el refresh token
                ResponseEntity<?> estado = keycloakRestService.logout(refreshToken,username);

                return estado;

            } catch (Exception e) {
                responseToken.setMessage("\"Se produjo un error al intentar cerrar sesión.");
                return  new ResponseEntity<>(responseToken,HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } catch (Exception e) {
            log.error("Error during logout", e.getMessage());
            responseToken.setMessage("Error durante el cierre de sesión.");
            return new ResponseEntity<>(responseToken,HttpStatus.UNPROCESSABLE_ENTITY);
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
    public ResponseEntity<?> regenerateOtp(@RequestParam  String email) {
        log.info("INI - regenerateOtp | requestURL=email");
        Response response=new Response();
        try {

            response=usuariosService.regenerateOtp(email);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e ){
            log.error("ERROR - regenerateOtp",e.getMessage());
            response.setStatusCode(403);
            response.setResponseMessage(e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);

        }
    }

    @Hidden
    @PostMapping(value = "oauth/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody @Valid OtpVerificationDTO dto) {
        log.info("INI - verifyOtp | requestURL=verify-otp");

        String username = dto.getUsername();
        Integer otp = dto.getOtp();

        //boolean isOtpValid = usuariosService.validateOTP(username, otp);
        Response mfaResponse= usuariosService.validateOTP(username,otp);


        switch (mfaResponse.getStatusCode()) {
            case 200:
                return ResponseEntity.ok(Map.of(
                        "message", mfaResponse.getResponseMessage(),
                        "isOtpValid", mfaResponse.getOtpResponse().isOtpValid()
                ));

            case 400:
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                        "message", mfaResponse.getResponseMessage()
                ));
            case 403:
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message",mfaResponse.getResponseMessage()));

            case 500:
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                        "message", mfaResponse.getResponseMessage()
                ));
        }
    }

}
