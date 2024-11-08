package pe.gob.bcrp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SW_PERSONA", schema = "BCRP_MSAUTHENTICA_API")
public class Persona  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persona_seq")
    @SequenceGenerator(name = "persona_seq", sequenceName = "BCRP_MSAUTHENTICA_API.seq_sw_persona", allocationSize = 1,initialValue = 1)
    @Column(name = "id_persona", nullable = false)
    private Integer idPersona;

    //@Column(name = "tipo_documento",length = 50)
    //private Integer tipoDocumento;
    @ManyToOne
    @JoinColumn(name = "id_documento", nullable = false)
    private DocumentoIdentidad docuIdentidad;

    @Column(name = "documento_identidad", length = 8)
    private String numeroDocumento;

    @Column(name = "ap_pat", length = 50)
    private String apellidoPaterno;

    @Column(name = "ap_mat")
    private String apellidoMaterno;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "correo")
    private String correo;

    @Column(name = "is_deleted")
    private boolean isDeleted=false;


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
