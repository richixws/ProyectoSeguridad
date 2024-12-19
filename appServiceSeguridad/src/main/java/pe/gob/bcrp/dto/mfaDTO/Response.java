package pe.gob.bcrp.dto.mfaDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private int statusCode;
    private String responseMessage;
    private OtpResponse otpResponse;

}
