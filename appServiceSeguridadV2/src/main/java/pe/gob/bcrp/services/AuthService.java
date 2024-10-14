package pe.gob.bcrp.services;

import pe.gob.bcrp.dto.LoginDTO;

public interface AuthService {

   public String login(LoginDTO loginDto);

}
