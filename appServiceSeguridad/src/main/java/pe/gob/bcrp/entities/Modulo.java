package pe.gob.bcrp.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SW_MODULO" , schema = "bd_seguridad")
public class Modulo  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "modulo_seq")
    @SequenceGenerator(name = "modulo_seq", sequenceName = "seq_sw_modulo", allocationSize = 1, initialValue = 1)
    @Column(name = "id_modulo", nullable = false)
    private Integer idModulo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sistema", nullable = false)
    private Sistema sistema;

    @Column(name = "order_date", nullable = false)
    private Date orderDate;
}
