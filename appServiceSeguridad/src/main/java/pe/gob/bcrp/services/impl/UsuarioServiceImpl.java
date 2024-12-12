package pe.gob.bcrp.services.impl;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.response.UsuarioResponse;
import pe.gob.bcrp.dto.usuarioDTO.RegistroCreateUsuarioDTO;
import pe.gob.bcrp.dto.usuarioDTO.RegistroUsuarioDTO;
import pe.gob.bcrp.dto.usuarioDTO.UsuarioFormDTO;
import pe.gob.bcrp.entities.*;
import pe.gob.bcrp.excepciones.ResourceNotFoundException;
import pe.gob.bcrp.repositories.IDocumentoIdentidadRepository;
import pe.gob.bcrp.repositories.IPersonaRepository;
import pe.gob.bcrp.repositories.IUsuarioRepository;
import pe.gob.bcrp.services.IEmailService;
import pe.gob.bcrp.repositories.*;
import pe.gob.bcrp.services.IUploadFileService;
import pe.gob.bcrp.services.IUsuarioService;
import pe.gob.bcrp.util.TotpUtils;
import pe.gob.bcrp.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Log4j2
@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements IUsuarioService {


   //@Autowired
    private IUsuarioRepository usuarioRepository;

    private IPersonaRepository personaRepository;

    private IDocumentoIdentidadRepository documentoIdentidadRepository;

    private ModelMapper modelMapper;

    private Util util;

    private IUploadFileService uploadFileService;

    private TotpUtils totpUtils;

    private IEmailService emailService;

    private OtpGenerator generateOTP;

    private IPerfilRepository perfilRepository;
    private IPerfilUsuarioRepository perfilUsuarioRepository;

    @Override
    @Cacheable(value = "usuarios", key = "{#pageNumber, #pageSize, #sortBy, #sortOrder, #nombres,#tipoDocumento,#numeroDocumento,#idSistema, #ambito}")
    public UsuarioResponse getAllUsuarios(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, String nombres, Integer tipoDocumento, String numeroDocumento, Integer idSistema, String ambito) {
        log.info("INI Service() - getAllUsuarios");

        try {

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

            Page<Usuario> pageUsuarios = null;

            //String nombres = StringUtils.hasText(nombresFiltro) ? nombresFiltro : null;
            //String numeroDocumento = StringUtils.hasText(numeroDocumentos) ? numeroDocumentos : null;
            //String ambito = StringUtils.hasText(ambitos) ? ambitos : null;


            if (nombres != null  || tipoDocumento != null || numeroDocumento != null || ambito!= null) {
                pageUsuarios = usuarioRepository.findByFilters(nombres, tipoDocumento, numeroDocumento,ambito, pageDetails);//,
            }else if(idSistema != null){
                pageUsuarios = usuarioRepository.findBySistemaId(idSistema, pageDetails);
            }else{
                 pageUsuarios = usuarioRepository.findByIsDeletedFalse(pageDetails);
            }

            List<Usuario> usuarios = pageUsuarios.getContent();

            List<UsuarioFormDTO> usuariosDTOS = usuarios.stream().map(user -> {
                UsuarioFormDTO usuarioDTO = modelMapper.map(user, UsuarioFormDTO.class);
                  if(user.getPersona() !=null){
                      usuarioDTO.setIdDocumento(user.getPersona().getTipoDocumento().getIdDocumentoIdentidad());
                      usuarioDTO.setTipoDocumento(user.getPersona().getTipoDocumento().getTipoDocumentoIdentidad());
                      usuarioDTO.setNumeroDocumento(user.getPersona().getNumeroDocumento());
                      usuarioDTO.setNombres(user.getPersona().getNombres());
                      usuarioDTO.setApellidoPaterno(user.getPersona().getApellidoPaterno());
                      usuarioDTO.setApellidoMaterno(user.getPersona().getApellidoMaterno());
                      usuarioDTO.setCorreoElectronico(user.getPersona().getCorreo());
                  }
                return usuarioDTO;
            }).toList();

            UsuarioResponse usuarioResponse = new UsuarioResponse();
            usuarioResponse.setContent(usuariosDTOS);
            usuarioResponse.setPageNumber(pageUsuarios.getNumber());
            usuarioResponse.setPageSize(pageUsuarios.getSize());
            usuarioResponse.setTotalElements(pageUsuarios.getTotalElements());
            usuarioResponse.setTotalPages(pageUsuarios.getTotalPages());
            usuarioResponse.setLastPage(pageUsuarios.isLast());
            return usuarioResponse;

        } catch (Exception e) {
            log.error( "ERROR - getAllEntidades() "+e.getMessage() );
            throw new RuntimeException(e);
        }
    }

    @Override
     public List<UsuarioFormDTO> uploadUserCsv(MultipartFile file) throws BadRequestException {
        log.info("INI Service() - uploadUserCsv");
        try {

            if(file.isEmpty()){
                throw new BadRequestException("Por favor seleccione un archivo para cargar");
            }
            if(!file.getContentType().equals("text/csv")){
                throw new BadRequestException("Por favor carge un archivo CSV valido");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            List<UsuarioFormDTO> users = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                UsuarioFormDTO user = new UsuarioFormDTO();
                user.setAmbito(record.get("Ambito"));               // "Ambito" en lugar de "ambito"
                user.setTipoDocumento(record.get("Tipo_documento"));      // "Tip. doc" en lugar de "tipo"
                user.setNumeroDocumento(record.get("Numero_documento")); // "Nro. documento" en lugar de "numeroDocumento"
                user.setEstado(record.get("Estado"));               // "Estado" en lugar de "estado"
                user.setNombres(record.get("Nombre"));              // "Nombre" en lugar de "nombres"
                user.setApellidoPaterno(record.get("Apellido_paterno")); // "Ape. paterno" en lugar de "apellidoPaterno"
                user.setApellidoMaterno(record.get("Apellido_materno")); // "Ape. materno" en lugar de "apellidoMaterno"
                user.setCorreoElectronico(record.get("Correo_electronico")); // "Correo electrónico" en lugar de "correoElectronico"
                users.add(user);

            }
            return users;

        } catch (BadRequestException e) {
            log.error("ERROR - al cargar archivo"+e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException("Error al procesar el archivo CSV: " + e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "usuarios", allEntries = true)
    public RegistroCreateUsuarioDTO guardarUsuario(Integer tipoDocumento,
                                                   String numeroDocumento,
                                                   String nombres,
                                                   String apePaterno,
                                                   String apeMaterno,
                                                   String correoElectronico,
                                                   String ambito, MultipartFile sustento) {

        try {
            // Verificar si el número de documento ya existe
            if (personaRepository.existsByNumeroDocumento(numeroDocumento)) {
                throw new IllegalArgumentException("El número de documento ya existe.");
            }

            // Verificar si el correo electrónico ya existe
            if (personaRepository.existsByCorreo(correoElectronico)) {
                throw new IllegalArgumentException("El correo electrónico ya existe.");
            }


            Usuario usuarioSistema=util.getUsuario();

            DocumentoIdentidad doc=documentoIdentidadRepository.findById(tipoDocumento).orElseThrow(()-> new ResourceNotFoundException("documento no encontrado"));

            Usuario usuario=new Usuario();
            Persona persona=new Persona();
           // usuarioNew.set
            persona.setTipoDocumento(doc);
            persona.setNumeroDocumento(numeroDocumento);
            persona.setNombres(nombres);
            persona.setApellidoPaterno(apePaterno);
            persona.setApellidoMaterno(apeMaterno);
            persona.setCorreo(correoElectronico);

            persona.setUsuarioCreacion(usuarioSistema.getUsuario());
            persona.setHoraCreacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));


            //if(ambito.equalsIgnoreCase("interno")){
            usuario.setAmbito(ambito);
            if(sustento !=null){
                //usuario.setDocSustento(sustento.getOriginalFilename());
                String nombrefileSustento=uploadFileService.upload(sustento);
                usuario.setDocSustento(nombrefileSustento);
            }

            usuario.setFechaCreacion(new Date());
            usuario.setEstado("ACTIVO");

            usuario.setUsuarioCreacion(usuarioSistema.getUsuario());
            usuario.setHoraCreacion(usuario.getHoraCreacion());
           // }


            Persona personaNew=personaRepository.save(persona);
            usuario.setPersona(personaNew);
            //usuarioNew.setEstado("Activo");
            Usuario usuarioNew=usuarioRepository.save(usuario);

            uploadFileService.almacenarDatosFile(sustento,usuarioNew.getIdUsuario(),"Modulo Usuario");

            RegistroCreateUsuarioDTO regUsuarioNew=modelMapper.map(usuarioNew, RegistroCreateUsuarioDTO.class);
            return regUsuarioNew;

        } catch (IllegalArgumentException e) {
          log.error(e.getMessage());
          throw new IllegalArgumentException(e.getMessage());
        }
        catch (Exception e) {
          log.error(e.getMessage());
          throw new RuntimeException(e);
        }
    }

    @Override
    @CacheEvict(value = "usuarios", allEntries = true)
    public RegistroUsuarioDTO updateUsuario(Integer idUsuario, RegistroUsuarioDTO registroUsuarioDTO) {

        log.info("INI Service() - updateUsuario");
        try {
            // Verificar si el número de documento ya existe
            Usuario u=usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            if (personaRepository.existsByNumeroDocumentoAndIdPersonaNot(registroUsuarioDTO.getNumeroDocumento(), u.getPersona().getIdPersona())) {
                throw new IllegalArgumentException("El número de documento ya existe.");
            }

            // Verificar si el correo electrónico ya existe
            if (personaRepository.existsByCorreoAndIdPersonaNot(registroUsuarioDTO.getCorreoElectronico(),u.getPersona().getIdPersona())) {
                throw new IllegalArgumentException("El correo electrónico ya existe.");
            }


            Usuario usuarioReg = util.getUsuario();

            Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            Persona persona = personaRepository.findById(usuario.getPersona().getIdPersona()).orElseThrow(() -> new RuntimeException("Persona no encontrado"));
            DocumentoIdentidad docuIde = documentoIdentidadRepository.findById(registroUsuarioDTO.getTipoDocumento()).orElseThrow(() -> new ResourceNotFoundException("documento no encontrado"));

            persona.setTipoDocumento(docuIde);
            persona.setNumeroDocumento(registroUsuarioDTO.getNumeroDocumento());
            persona.setNombres(registroUsuarioDTO.getNombres());
            persona.setApellidoPaterno(registroUsuarioDTO.getApePaterno());
            persona.setApellidoMaterno(registroUsuarioDTO.getApeMaterno());
            persona.setCorreo(registroUsuarioDTO.getCorreoElectronico());

            persona.setUsuarioActualizacion(usuarioReg.getUsuario());
            persona.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));


            // usuarioUpdate.setCorreoInstitucional(registroUsuarioDTO.getCorreo());
            usuario.setAmbito(registroUsuarioDTO.getAmbito());

            usuario.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            usuario.setUsuarioActualizacion(usuario.getUsuario());
            usuario.setDocSustento(usuario.getDocSustento());
            //   usuarioUpdate.setPersona(usuarioDTO.getIdPersona());**/

            Persona personaUpd=personaRepository.save(persona);
            usuario.setPersona(personaUpd);
            Usuario usuarioUpd=usuarioRepository.save(usuario);

            RegistroUsuarioDTO newUsuario = modelMapper.map(usuarioUpd, RegistroUsuarioDTO.class);
            return newUsuario;

        } catch (IllegalArgumentException e) {
           log.error(e.getMessage());
           throw new IllegalArgumentException(e.getMessage());
        }  catch (ResourceNotFoundException e){
            log.error("ERROR - updateUsuario() "+e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    @CacheEvict(value = "usuarios", allEntries = true)
    public boolean deleteUsuario(Integer idUsuario) {

        log.info("INI - InhabilitarUsuario()");
        boolean estado=false;
        try {
            Usuario usuarioReg=util.getUsuario();

            Usuario usuario=usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            if(usuario!=null){
                //entidadRepository.deleteById(id);
                //usuario.setDeleted(true);
                if(usuario.getEstado().equalsIgnoreCase("Inactivo")){
                    throw  new ResourceNotFoundException("Usuario ya ha sido inhabilitado");
                }
                usuario.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                usuario.setUsuarioEliminacion(usuarioReg.getUsuario());
                usuario.setEstado("Inactivo");

                usuarioRepository.save(usuario);
                estado=true;
            }


        }catch (ResourceNotFoundException e){
            log.error("ERROR - deleteEntidad() "+e.getMessage());
            e.printStackTrace();
            estado=false;
        }
        return estado;
    }

    @Override
    @CacheEvict(value = "usuarios", allEntries = true)
    public boolean AddProfilesToUsuario(Integer idUsuario, Integer idRol) {
        log.info("INI - Asignar perfiles a Usuario()");
        boolean estado = false;
        try {

            Usuario usuario=usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            if(usuario != null){
                List<Perfil> perfiles = perfilRepository.findByRolAndDeletedFalseCustom(idRol);
                List<PerfilUsuario> perfilUsuarios = new ArrayList<>();
                if(perfiles != null) {
                    for (Perfil perfil: perfiles) {
                        PerfilUsuario obj = new PerfilUsuario();
                        obj.setPerfil(perfil);
                        obj.setUsuario(usuario);
                        perfilUsuarios.add(obj);
                    }

                    perfilUsuarioRepository.saveAll(perfilUsuarios);
                    estado=true;
                } else {
                    throw new ResourceNotFoundException("Perfiles no encontrados");
                }
            }
        }catch (ResourceNotFoundException e){
            log.error("ERROR - AddProfilesToUsuario() "+e.getMessage());
            e.printStackTrace();
        }
        return estado;
    }

    @Override
    @CacheEvict(value = "usuarios", allEntries = true)
    public boolean AddProfileToUsuario(Integer idUsuario, Integer idPerfil) {
        log.info("INI - Asignar perfiles a Usuario()");
        boolean estado = false;
        try {

            Usuario usuario=usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
            if(usuario != null){
                Optional<Perfil> perfil = perfilRepository.findById(idPerfil);
                if(perfil.isPresent()) {
                    PerfilUsuario obj = new PerfilUsuario();
                    obj.setPerfil(perfil.get());
                    obj.setUsuario(usuario);

                    perfilUsuarioRepository.save(obj);
                    estado=true;
                } else {
                    throw new ResourceNotFoundException("Perfil no encontrado");
                }
            }
        }catch (ResourceNotFoundException e){
            log.error("ERROR - AddProfileToUsuario() "+e.getMessage());
            e.printStackTrace();
        }
        return estado;
    }



    @Override
    public UsuarioDTO buscarPorUsuarioLogin(String usuario) {
       Optional<Usuario> usuarioLogin=usuarioRepository.findByUsuario(usuario);
       // Optional<Usuario> usuarioLogin=usuarioRepository.findByUsuarioAndEstadoAndIsDeletedFalse(usuario,estado);
        UsuarioDTO dto = null;
        if(usuarioLogin.isPresent()){
            dto= mapToDTO(usuarioLogin.get());
        }
        return dto;
    }



    // convert Entity into DTO
    private UsuarioDTO mapToDTO(Usuario usuario){
        UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
        return usuarioDTO;
    }


    @Override
    public Boolean regenerateOtp(String email) {

        Persona persona = personaRepository.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("No user found with this email: " + email));

        Usuario user = usuarioRepository.findByPersona(persona)
                .orElseThrow(() -> new RuntimeException("User not found for the given email"));


        //Usuario user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
       // String otp = totpUtils.generateOtp();
        String otp = generateOTP.generateOTP(user.getUsuario());
        if (otp == null)
        {
            log.error("OTP generator is not working...");
            return false;
        }

        log.info("Generated OTP: {}", otp);

        try {
            emailService.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
       //user.setOtp(otp);
       // user.setOtpGeneratedTime(LocalDateTime.now());
       // Usuario userResp=usuarioRepository.save(user);

        return true;
        //return "Email sent... please verify account within 1 minute";
    }

    @Override
    public Boolean validateOTP(String username, String otp) {

        // get OTP from cache
        Integer cacheOTP = generateOTP.getOPTByKey(username);
        if (cacheOTP!=null && cacheOTP.equals(Integer.parseInt(otp)))
        {
            generateOTP.clearOTPFromCache(username);
            return true;
        }
        return false;


    }


}
