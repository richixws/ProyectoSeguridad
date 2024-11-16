package pe.gob.bcrp.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "SW_DOCUMENTO_IDENTIDAD" , schema = "BCRP_MSAUTHENTICA_API")
public class DocumentoIdentidad {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documento_identidad_seq")
    @SequenceGenerator(name = "documento_identidad_seq", sequenceName = "BCRP_MSAUTHENTICA_API.seq_sw_documento_identidad", allocationSize = 1, initialValue = 1)
    @Column(name = "id_documento", nullable = false)
    private Integer idDocumentoIdentidad;

    @Column(name = "tipo_documento", nullable = false)
    private String tipoDocumentoIdentidad;

    @Column(name = "grupo_documento")
    private Integer grupoDocumento;

    @Column(name = "longitud")
    private Integer longitud;



}
