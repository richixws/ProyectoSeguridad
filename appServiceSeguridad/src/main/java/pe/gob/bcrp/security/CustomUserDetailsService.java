package pe.gob.bcrp.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.gob.bcrp.entities.Perfil;
import pe.gob.bcrp.entities.Rol;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.repositories.IUsuarioRepository;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository iuserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = iuserRepository.findByUsuario(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        //obtener roles de usuario de BD
        Set<GrantedAuthority> authorities = usuario.getPerfilUsuarios().stream()
                .map(Perfil::getRol)
                .filter(Objects::nonNull)
                .map(Rol::getNombre)
                .filter(nombre -> nombre != null && !nombre.isEmpty())
                .peek(roleName -> log.info("Rol añadido: " + roleName))
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toSet());

        log.info("Authorities asignadas: " + authorities);

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsuario(),
                "", // No necesitamos contraseña ya que usamos token
                true,
                true,
                true,
                true,
                authorities
        );

        /**return new org.springframework.security.core.userdetails.User(usuario.getUsuario(),
                usuario.getPassword(),
                authorities);**/
    }
}
