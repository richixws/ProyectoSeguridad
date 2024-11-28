package pe.gob.bcrp.dto;

import lombok.Data;
@Data
public class LoginDTO {

    private String usuario;
    private String password;

    //captcha
    /**private String captcha;
    private String hiddenCaptcha;
    private String tokenUuid;**/

}
