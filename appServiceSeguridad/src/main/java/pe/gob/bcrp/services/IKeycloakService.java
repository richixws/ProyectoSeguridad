package pe.gob.bcrp.services;


import org.keycloak.representations.idm.UserRepresentation;
import pe.gob.bcrp.dto.UserDTO;

import java.util.List;

public interface IKeycloakService {

    List<UserRepresentation>findAllUsers();
    List<UserRepresentation>searchUser(String username);
    String createUser(UserDTO userDTO);
    void deleteUser(String userId);
    void updateUser(String userId, UserDTO userDTO);

}
