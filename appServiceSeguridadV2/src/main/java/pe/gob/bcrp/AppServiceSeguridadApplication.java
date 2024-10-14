package pe.gob.bcrp;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.gob.bcrp.entities.*;
import pe.gob.bcrp.repositories.*;

import java.util.Date;

@SpringBootApplication
public class AppServiceSeguridadApplication  implements CommandLineRunner {

	@Autowired
	private IPersonaRepository iPersonaRepository;
	@Autowired
	private IUsuarioRepository iUsuarioRepository;

	@Autowired
	private IEntidadRepository entidadRepository;

	@Autowired
	private ISistemaRepository istemaRepository;

	@Autowired
	private IRolRepository rolRepository;

	@Autowired
	private IPerfilRepository iPerfilRepository;

	@Autowired
	private  IPerfilUsuarioRepository iPerfilUsuarioRepository;

	@Autowired
	private IModuloRepository iModuloRepository;

	@Autowired
	private IOpcionRepository iOpcionRepository;



	@Autowired
	private PasswordEncoder passwordEncoder;


	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}


	public static void main(String[] args) {
		SpringApplication.run(AppServiceSeguridadApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		/**
		Persona persona1 = new Persona();
		persona1.setNombres("Juan");
		persona1.setApellidoPaterno("Gonzales");
		persona1.setApellidoMaterno("Perez");
		persona1.setDocumentoIdentidad("12345678");
		persona1.setTipoDocumento(1);
		iPersonaRepository.save(persona1);


		Usuario usuario1 = new Usuario();
		usuario1.setUsuario("jgonzales");
		usuario1.setPassword(passwordEncoder.encode("123456"));
		usuario1.setAmbito("interno");
		usuario1.setDocSustento("doc01");
		usuario1.setFechaCreacion(new Date());
		usuario1.setCorreoInstitucional("juan.gonzales@empresa.com");
		usuario1.setPersona(persona1);
		iUsuarioRepository.save(usuario1);


		Entidad entidad1 = new Entidad();
		entidad1.setNombre("Entidad A");
		entidad1.setRuc("20123456789");
		entidad1.setSigla("ENT-A");
		entidad1.setCodExterno("EXT01");
	    entidadRepository.save(entidad1);


		Sistema sistema1=new Sistema();
		sistema1.setCodigo("SYS01");
		sistema1.setNombre("Sistema A");
		sistema1.setVersion("v1.0");
		sistema1.setLogoMain("logoA.png");
		sistema1.setLogoHead("headerA.png");
		sistema1.setUrl("http://sistema-a.com");
		istemaRepository.save(sistema1);

		Modulo modulo1 = new Modulo();
		modulo1.setSistema(sistema1);
		modulo1.setOrderDate(new Date());
		iModuloRepository.save(modulo1);

		Opcion opcion1 = new Opcion();
		opcion1.setNombreOpcion("Opción 1");
		opcion1.setUrl("/opcion1");
		opcion1.setModulo(modulo1);
		iOpcionRepository.save(opcion1);


		Rol rol1 = new Rol();
		rol1.setNombre("Administrador");
		rol1.setEstado(1);
		rol1.setUltLin(100);
		rol1.setSistema(sistema1);
		rolRepository.save(rol1);

		Perfil perfil1 = new Perfil();
		perfil1.setNombre("Perfil Administrador");
		perfil1.setRol(rol1);
		perfil1.setEntidad(entidad1);
		iPerfilRepository.save(perfil1);


		PerfilUsuario perfilUsuario1 = new PerfilUsuario();
		perfilUsuario1.setPerfil(perfil1);
		perfilUsuario1.setUsuario(usuario1);
		iPerfilUsuarioRepository.save(perfilUsuario1);


   **/










	}
}
