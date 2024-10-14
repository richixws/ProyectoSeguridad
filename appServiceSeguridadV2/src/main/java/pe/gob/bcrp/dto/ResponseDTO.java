package pe.gob.bcrp.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDTO<T> {

    private int status;
    private String message;
    private T body;

    public ResponseDTO(int status, String message, T body) {
        this.status = status;
        this.message = message;
        this.body=body;
    }

}
