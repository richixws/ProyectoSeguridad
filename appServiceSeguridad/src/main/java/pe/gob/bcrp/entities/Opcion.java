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
@Table(name = "SW_OPCION" , schema = "bd_seguridad")
public class Opcion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "opcion_seq")
    @SequenceGenerator(name = "opcion_seq", sequenceName = "seq_sw_opcion", allocationSize = 1, initialValue = 1)
    @Column(name = "id_opcion", nullable = false)
    private Integer idOpcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_modulo", nullable = false)
    private Modulo modulo;

    @Column(name = "nombre" , length = 100, nullable = false)
    private String nombreOpcion;

    @Column(name = "url", length = 300, nullable = false )
    private String url;

}
