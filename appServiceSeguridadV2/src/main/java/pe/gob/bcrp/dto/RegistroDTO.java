package pe.gob.bcrp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDTO {

      private String nombres;
      private String apellidos;
      private String usuario;//correo
      private String password;

}
