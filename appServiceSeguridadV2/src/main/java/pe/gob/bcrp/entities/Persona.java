package pe.gob.bcrp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SW_PERSONA", schema = "bd_seguridad")
public class Persona  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "persona_seq")
    @SequenceGenerator(name = "persona_seq", sequenceName = "bd_seguridad.seq_sw_persona", allocationSize = 1,initialValue = 1)
    @Column(name = "id_persona", nullable = false)
    private Integer idPersona;

    @Column(name = "tipo_documento",length = 50)
    private Integer tipoDocumento;

    @Column(name = "documento_identidad", length = 8)
    private String documentoIdentidad;

    @Column(name = "ap_pat", length = 50)
    private String apellidoPaterno;

    @Column(name = "ap_mat")
    private String apellidoMaterno;

    @Column(name = "nombres")
    private String nombres;
}
