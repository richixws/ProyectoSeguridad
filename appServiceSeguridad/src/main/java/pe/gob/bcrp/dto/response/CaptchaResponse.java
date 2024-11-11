package pe.gob.bcrp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CaptchaResponse {

    private String captchaImage;
    private String hiddenCaptcha;

    private String tokenUuid;


}
