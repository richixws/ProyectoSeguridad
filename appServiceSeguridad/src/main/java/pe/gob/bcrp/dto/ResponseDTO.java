package pe.gob.bcrp.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDTO<T> {

    @JsonView({Views.Create.class, Views.Update.class})
    private int status;
    @JsonView({Views.Create.class, Views.Update.class})
    private String message;
    //@JsonView({Views.Create.class, Views.Update.class})
    //private T body;

    public ResponseDTO(int status, String message, T body) {
        this.status = status;
        this.message = message;
        //this.body=body;
    }

}
