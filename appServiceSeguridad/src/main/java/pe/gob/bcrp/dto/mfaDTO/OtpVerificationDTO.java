package pe.gob.bcrp.dto.mfaDTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OtpVerificationDTO {

    @NotNull
    @NotBlank
    @NotEmpty
    private String username;

    @NotNull(message = "codigo es obligatorio.")
    @Min(value = 0, message = "El código debe ser un número positivo.")
    @Digits(integer = 6, fraction = 0, message = "El código debe tener exactamente 6 dígitos.")
    private Integer otp;
}