package pe.gob.bcrp.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SW_DOCUMENTO_IDENTIDAD" , schema = "bd_seguridad")
public class DocumentoIdentidad {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documento_identidad_seq")
    @SequenceGenerator(name = "documento_identidad_seq", sequenceName = "bd_seguridad.seq_sw_documento_identidad", allocationSize = 1, initialValue = 1)
    @Column(name = "id_documento", nullable = false)
    private Integer idDocumentoIdentidad;

    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumentoIdentidad;



}
