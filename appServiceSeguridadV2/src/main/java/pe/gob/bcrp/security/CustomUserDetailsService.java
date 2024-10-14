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
import pe.gob.bcrp.entities.PerfilUsuario;
import pe.gob.bcrp.entities.Rol;
import pe.gob.bcrp.entities.Usuario;
import pe.gob.bcrp.repositories.IPerfilRepository;
import pe.gob.bcrp.repositories.IPerfilUsuarioRepository;
import pe.gob.bcrp.repositories.IRolRepository;
import pe.gob.bcrp.repositories.IUsuarioRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private IUsuarioRepository userRepository;

    //private IRolRepository rolRepository;
   @Autowired
   private IPerfilUsuarioRepository perfilUsuarioRepository;

    public CustomUserDetailsService(IUsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        Usuario usuario=userRepository.findByUsuario(usernameOrEmail).orElseThrow(() ->
                         new UsernameNotFoundException("User not found with username or email: "+ usernameOrEmail));


     //   Usuario usuario2 = userRepository.findByUsuarioWithPerfilesAndRoles(usernameOrEmail)
        // .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));


        /**Set<Perfil> lista=new HashSet<>();
        Perfil perfil= new Perfil();
        Perfil perfil2=new Perfil();
        perfil.setNombre("ADMIN");
        perfil2.setNombre("USER");
        lista.add(perfil);
        lista.add(perfil2);


        usuario.setPerfilUsuarios(lista);**/

        Set<GrantedAuthority> authorities = usuario.getPerfilUsuarios().stream()
                .map(Perfil::getRol)
                .filter(Objects::nonNull)
                .map(Rol::getNombre)
                .filter(nombre -> nombre != null && !nombre.isEmpty())
                .peek(roleName -> log.info("Rol añadido: " + roleName))
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .collect(Collectors.toSet());

        log.info("Authorities asignadas: " + authorities);

       /** Set<GrantedAuthority> authorities = usuario
                .getPerfilUsuarios()
                .stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toSet());**/


            return new org.springframework.security.core.userdetails.User(usuario.getUsuario(),
                usuario.getPassword(),
                authorities);
    }
}
