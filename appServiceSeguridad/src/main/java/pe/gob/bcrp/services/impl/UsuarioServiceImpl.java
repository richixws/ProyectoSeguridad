package pe.gob.bcrp.services.impl;

import jakarta.mail.MessagingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
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
import org.springframework.web.multipart.MultipartFile;
import pe.gob.bcrp.dto.*;
import pe.gob.bcrp.dto.mfaDTO.OtpResponse;
import pe.gob.bcrp.dto.mfaDTO.Response;
import pe.gob.bcrp.dto.personaDTO.ValidateDni;
import pe.gob.bcrp.dto.personaDTO.ValidatePasaporte;
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
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


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
    public List<DocumentoIdentidadDTO> getAllDocumentosUsuarios() {
        try {
            log.info("INI - getAllDocumentoUsuarios");
            List<DocumentoIdentidad> listDocumentos=documentoIdentidadRepository.findByGrupoDocumento(1);
            return listDocumentos.stream()
                    .map(documento -> modelMapper.map(documento, DocumentoIdentidadDTO.class))
                    .collect(Collectors.toList());

        }catch (Exception e){
            log.error("ERROR - getAllDocumentoUsuarios() {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

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
                String nombreLowerCase = nombres != null ? nombres.toLowerCase() : null;
                String ambitoLowerCase = ambito != null ? ambito.toLowerCase() : null;
                pageUsuarios = usuarioRepository.findByFilters(nombreLowerCase, tipoDocumento, numeroDocumento,ambitoLowerCase, pageDetails);//,
            }else if(idSistema != null){
                pageUsuarios = usuarioRepository.findBySistemaId(idSistema, pageDetails);
            }else{
                // pageUsuarios = usuarioRepository.findByIsDeletedFalse(pageDetails);
                pageUsuarios = usuarioRepository.findAll(pageDetails);
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
            log.error("ERROR - getAllEntidades() {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
     public List<UsuarioFormDTO> uploadUserCsv(MultipartFile file) throws BadRequestException {
        log.info("INI Service() - uploadUserCsv");
        try {

            if(file.isEmpty()){
                throw new BadRequestException("Por favor seleccione un archivo para cargar.");
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

      //  try {
         DocumentoIdentidad docu = documentoIdentidadRepository.findByIdDocumentoIdentidadAndGrupoDocumento(tipoDocumento, 1)
                    .orElseThrow(() -> new ResourceNotFoundException("Tipo de documento de identidad no existe"));

        RegistroCreateUsuarioDTO registroDTO = RegistroCreateUsuarioDTO.builder()
                .documentType(tipoDocumento)
                .documentNumber(numeroDocumento)
                .names(nombres)
                .fatherSurname(apePaterno)
                .motherSurname(apeMaterno)
                .email(correoElectronico)
                .scope(ambito)
                .build();

        Set<ConstraintViolation<RegistroCreateUsuarioDTO>> violations;
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();
            if(docu != null && docu.getGrupoDocumento().equals(1)){
                if(Objects.equals(docu.getIdDocumentoIdentidad(), 1)) {
                    violations = validator.validate(registroDTO, ValidateDni.class);
                } else {
                    violations = validator.validate(registroDTO, ValidatePasaporte.class);
                }
            } else {
                if(Objects.equals(registroDTO.getDocumentType(), 1)) {
                    violations = validator.validate(registroDTO, ValidateDni.class);
                } else {
                    violations = validator.validate(registroDTO, ValidatePasaporte.class);
                }
            }

            if(!violations.isEmpty()) {
                var obj = violations.stream().findFirst().get();
                throw new IllegalArgumentException(obj.getMessage());
            }

            // Verificar si el número de documento ya existe
            if (personaRepository.existsByNumeroDocumento(numeroDocumento)) {
                throw new IllegalArgumentException("El número de documento del usuario se encuentra en uso, por favor ingrese uno nuevo.");
            }

            // Verificar si el correo electrónico ya existe
            if (personaRepository.existsByCorreo(correoElectronico)) {
                throw new IllegalArgumentException("El correo electrónico del usuario se encuentra en uso, por favor ingrese un nuevo.");
            }


            Usuario usuarioSistema=util.getUsuario();

            DocumentoIdentidad doc=documentoIdentidadRepository.findById(tipoDocumento).orElseThrow(()-> new ResourceNotFoundException("El tipo documento del usuario no existe."));

            Usuario usuario=new Usuario();
            Persona persona=new Persona();
            persona.setTipoDocumento(doc);
            persona.setNumeroDocumento(numeroDocumento);
            persona.setNombres(nombres);
            persona.setApellidoPaterno(apePaterno);
            persona.setApellidoMaterno(apeMaterno);
            persona.setCorreo(correoElectronico);
            persona.setEstado(1);
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
            usuario.setUsuarioCreacion(usuarioSistema.getUsuario());
            usuario.setHoraCreacion(usuario.getHoraCreacion());
            if(usuario.getEstado()==null){
                usuario.setEstado(1);
            }




            Persona personaNew=personaRepository.save(persona);
            usuario.setPersona(personaNew);
            //usuarioNew.setEstado("Activo");
            Usuario usuarioNew=usuarioRepository.save(usuario);

            uploadFileService.almacenarDatosFile(sustento,usuarioNew.getIdUsuario(),"Modulo Usuario");

            RegistroCreateUsuarioDTO regUsuarioNew=modelMapper.map(usuarioNew, RegistroCreateUsuarioDTO.class);
            return regUsuarioNew;

        } catch (IllegalArgumentException e) {
            log.error("ERROR - guardarUsuario {}", e.getMessage());
          throw new IllegalArgumentException(e.getMessage());
        }catch (ResourceNotFoundException e) {
            log.error("ERROR  guardarUsuario{}", e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        }catch (Exception e) {
            log.error("ERROR guardarUsuario{}", e.getMessage());
          throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "usuarios", allEntries = true)
    public RegistroUsuarioDTO updateUsuario(Integer idUsuario, RegistroUsuarioDTO registroUsuarioDTO) {

        log.info("INI Service() - updateUsuario");
      //  try {
        DocumentoIdentidad doc = documentoIdentidadRepository.findByIdDocumentoIdentidadAndGrupoDocumento(registroUsuarioDTO.getDocumentType(), 1)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de documento de identidad no existe"));

            Set<ConstraintViolation<RegistroUsuarioDTO>> violations;
            try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
                Validator validator = factory.getValidator();
                if(doc != null && doc.getGrupoDocumento().equals(1)){
                    if(Objects.equals(doc.getIdDocumentoIdentidad(), 1)) {
                        violations = validator.validate(registroUsuarioDTO, ValidateDni.class);
                    } else {
                        violations = validator.validate(registroUsuarioDTO, ValidatePasaporte.class);
                    }
                } else {
                    if(Objects.equals(registroUsuarioDTO.getDocumentType(), 1)) {
                        violations = validator.validate(registroUsuarioDTO, ValidateDni.class);
                    } else {
                        violations = validator.validate(registroUsuarioDTO, ValidatePasaporte.class);
                    }
                }

                if(!violations.isEmpty()) {
                    var obj = violations.stream().findFirst().get();
                    throw new IllegalArgumentException(obj.getMessage());
                }


            // Verificar si el número de documento ya existe
            Usuario u=usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Usuario a actualizar no existe."));
            if (personaRepository.existsByNumeroDocumentoAndIdPersonaNot(registroUsuarioDTO.getDocumentNumber(), u.getPersona().getIdPersona())) {
                throw new IllegalArgumentException("El número de documento del usuario ya esta registrado en otro usuario.");
            }

            // Verificar si el correo electrónico ya existe
            if (personaRepository.existsByCorreoAndIdPersonaNot(registroUsuarioDTO.getEmail(),u.getPersona().getIdPersona())) {
                throw new IllegalArgumentException("El correo electrónico del usuario ya esta registrado en otro usuario.");
            }

            Usuario usuarioReg = util.getUsuario();

            Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Usuario a actualizar no existe."));
            Persona persona = personaRepository.findById(usuario.getPersona().getIdPersona()).orElseThrow(() -> new IllegalArgumentException("Persona a actualizar no existe"));
            DocumentoIdentidad docuIde = documentoIdentidadRepository.findById(registroUsuarioDTO.getDocumentType()).orElseThrow(() -> new IllegalArgumentException("Documento de identidad a actualizar no existe."));

            persona.setTipoDocumento(docuIde);
            persona.setNumeroDocumento(registroUsuarioDTO.getDocumentNumber());
            persona.setNombres(registroUsuarioDTO.getNames());
            persona.setApellidoPaterno(registroUsuarioDTO.getFatherSurname());
            persona.setApellidoMaterno(registroUsuarioDTO.getMotherSurname());
            persona.setCorreo(registroUsuarioDTO.getEmail());

            persona.setUsuarioActualizacion(usuarioReg.getUsuario());
            persona.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));


            // usuarioUpdate.setCorreoInstitucional(registroUsuarioDTO.getCorreo());
            usuario.setAmbito(registroUsuarioDTO.getScope());

            usuario.setHoraActualizacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
            usuario.setUsuarioActualizacion(usuario.getUsuario());
            usuario.setDocSustento(usuario.getDocSustento());
            usuario.setEstado(registroUsuarioDTO.getState());
            //   usuarioUpdate.setPersona(usuarioDTO.getIdPersona());**/

            Persona personaUpd=personaRepository.save(persona);
            usuario.setPersona(personaUpd);
            Usuario usuarioUpd=usuarioRepository.save(usuario);

            RegistroUsuarioDTO newUsuario = modelMapper.map(usuarioUpd, RegistroUsuarioDTO.class);
            return newUsuario;

        } catch (IllegalArgumentException e) {
            log.error("ERROR updateUsuario() {}", e.getMessage());
           throw new IllegalArgumentException(e.getMessage());
        }  catch (ResourceNotFoundException e){
            log.error("ERROR - updateUsuario() {}", e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("ERROR - updateUsuario e() {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = "usuarios", allEntries = true)
    public boolean deleteUsuario(Integer idUsuario) {

        log.info("INI - InhabilitarUsuario()");
        boolean estado=false;
        try {
            Usuario usuarioReg=util.getUsuario();

            Usuario usuario=usuarioRepository.findById(idUsuario).orElseThrow(() -> new ResourceNotFoundException("Usuario a inhabilitar no existe."));
            if(usuario!=null){
                /*if(usuario.getEstado().equalsIgnoreCase("Inactivo")){
                    throw  new ResourceNotFoundException("Usuario ya ha sido inhabilitado");
                }*/
                if(usuario.isDeleted()){
                    throw new ResourceNotFoundException("El usuario no existe, ya se encuentra eliminado.");
                }
                usuario.setHoraDeEliminacion(LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()));
                usuario.setUsuarioEliminacion(usuarioReg.getUsuario());
                usuario.setDeleted(true);

                usuarioRepository.save(usuario);
                estado=true;
            }


        }catch (ResourceNotFoundException e){
            log.error("ERROR - deleteEntidad() {}", e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
            //estado=false;
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
        log.info("Otp: {}", otp);
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
    public Response validateOTP(String username, Integer otp) {

        try{
        // get OTP from cache
        Integer cacheOTP = generateOTP.getOPTByKey(username);


        if (cacheOTP == null) {
            return Response.builder()
                    .statusCode(400)
                    .responseMessage("No has enviado una OTP o ha caducado.")
                    .build();
        }

        Integer failedAttempts = generateOTP.getFailedAttempts(username);

        if (failedAttempts == null) {
            failedAttempts = 0; // Si no existe, inicializar en 0
        }


        // Validar que el OTP coincida
        if (!cacheOTP.equals(otp)) {
            failedAttempts++;

            generateOTP.updateFailedAttempts(username, failedAttempts);

            if (failedAttempts > 3) {

                generateOTP.clearFailedAttempts(username);
                generateOTP.clearOTPFromCache(username);

                return Response.builder()
                        .statusCode(403)
                        .responseMessage("La cuenta está bloqueada debido a demasiados intentos fallidos.")
                        .build();
            }
            return Response.builder()
                    .statusCode(400)
                    .responseMessage(" codigo invalido OTP")
                    .build();
        }


        // Limpiar el OTP del caché después de validar
        generateOTP.clearFailedAttempts(username);
        generateOTP.clearOTPFromCache(username);

        return Response.builder()
                .statusCode(200)
                .responseMessage("SUCCESS")
                .otpResponse(OtpResponse.builder().isOtpValid(true).build())
                .build();

    } catch (Exception e) {
        log.error("Error during OTP validation", e);
        return Response.builder()
                .statusCode(500)
                .responseMessage("Internal server error")
                .build();
    }


    }
}
