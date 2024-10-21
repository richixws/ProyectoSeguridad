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
@Table(name = "SW_ENTIDAD" , schema = "bd_seguridad")
public class Entidad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entidad_seq")
    @SequenceGenerator(name = "entidad_seq", sequenceName = "bd_seguridad.seq_sw_entidad", allocationSize = 1, initialValue = 1)
    @Column(name = "id_entidad", nullable = false)
    private Integer idEntidad;

    //@Column(name = "tipo_documento", length = 25,nullable = false)
    //private String tipoDocumento;
    @ManyToOne
    @JoinColumn(name = "id_documento", nullable = false)
    private DocumentoIdentidad documentoIdentidad;

    @Column(name = "numero_documento", length = 25,nullable = false )
    private String numeroDocumento;

    @Column(name = "nombre", length = 100,nullable = false)
    private String nombre;

    @Column(name = "sigla", length = 50,nullable = false)
    private String sigla;

    @Column(name = "cod_externo", length = 50,nullable = false)
    private String codExterno;

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
