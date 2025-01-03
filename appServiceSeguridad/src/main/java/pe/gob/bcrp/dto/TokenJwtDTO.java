package pe.gob.bcrp.dto;

import lombok.Data;

@Data
public class TokenJwtDTO {

    private String name;
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private int refresh_expires_in;



}
