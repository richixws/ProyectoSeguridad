package pe.gob.bcrp.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
@Data
public class LoginDTO {
    @NotEmpty(message = "Usuario no puede ser vacio.")
    private String username;
    @NotEmpty(message = "Contarseña no puede ser vacio.")
    private String password;
    //captcha
    /**@NotEmpty(message = "Captcha no puede ser vacio")
    private String captcha;
    @NotEmpty(message = "HiddenCaptcha no puede ser vacio")
    private String hiddenCaptcha;
    @NotEmpty(message = "TokenUuid no puede ser vacio")
    private String tokenUuid;**/

}
