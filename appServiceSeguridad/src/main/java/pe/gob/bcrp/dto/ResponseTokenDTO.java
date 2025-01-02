package pe.gob.bcrp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta del token JWT")
public class ResponseTokenDTO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonView(Views.Success.class)
    private T data;

    @JsonView({Views.Error.class})
    private String message;


    public ResponseTokenDTO() {
    }

    public ResponseTokenDTO(T data, String message) {
        this.data = data;
        this.message = message;
    }
}
