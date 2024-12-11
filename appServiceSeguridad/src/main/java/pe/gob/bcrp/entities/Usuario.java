package pe.gob.bcrp.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SW_USUARIO")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "seq_sw_usuario", allocationSize = 1, initialValue = 1)
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "correo_institucional", length = 50)
    private String correoInstitucional;

    @Column(name = "fecha_creacion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;

    @Column(name = "doc_sustento", nullable = false, length = 500)
    private String docSustento;

    @Column(name = "ambito", nullable = false, length = 20)
    private String ambito;

    @Column(name = "usuario", nullable = false, length = 100)
    private String usuario;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "estado")
    private  String estado;

    //mapeo realcionar con entidad persona
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;


    @Column(name = "is_deleted")
    private boolean isDeleted=false;

    //@Column(name = "otp")
    //private String otp;

    //@Column(columnDefinition = "TIMESTAMP",name = "otp_generated_time")
    //private LocalDateTime otpGeneratedTime;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "SW_PERFIL_USUARIO",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_perfil"))
    private Set<Perfil> perfilUsuarios = new HashSet<>();

    //agregacion campos auditoria
    @Column(name = "hora_creacion")
    private LocalDateTime horaCreacion;

    @Column(name = "hora_eliminacion")
    private LocalDateTime horaDeEliminacion;

    @Column(name = "hora_actualizacion")
    private LocalDateTime horaActualizacion;

    @Column(name = "usuario_creacion",length = 50)
    private String usuarioCreacion;

    @Column(name = "usuario_eliminacion", length = 50)
    private String usuarioEliminacion;

    @Column(name = "usuario_actualizacion", length = 50)
    private String usuarioActualizacion;



}
