package pe.gob.bcrp.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SW_USUARIO" , schema = "bd_seguridad")
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

    //mapeo realcionar con entidad persona
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona", nullable = false)
    private Persona persona;

    //mapeo relacional con perfil
    @ManyToMany
    @JoinTable(
            name = "SW_PERFIL_USUARIO",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_perfil")
    )
    private Set<Perfil> perfiles;



}
