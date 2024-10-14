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
@Table(name = "SW_ENTIDAD" , schema = "bd_seguridad")
public class Entidad implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entidad_seq")
    @SequenceGenerator(name = "entidad_seq", sequenceName = "bd_seguridad.seq_sw_entidad", allocationSize = 1, initialValue = 1)
    @Column(name = "id_entidad", nullable = false)
    private Integer idEntidad;

    @Column(name = "ruc", length = 25,nullable = false)
    private String ruc;

    @Column(name = "nombre", length = 100,nullable = false)
    private String nombre;

    @Column(name = "sigla", length = 50,nullable = false)
    private String sigla;

    @Column(name = "cod_externo", length = 50,nullable = false)
    private String codExterno;
}
