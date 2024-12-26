package pe.gob.bcrp.dto.mfaDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private int statusCode;
    private String responseMessage;
    private OtpResponse otpResponse;

}
