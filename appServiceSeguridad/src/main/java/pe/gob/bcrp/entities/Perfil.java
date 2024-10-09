package pe.gob.bcrp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SW_PERFIL", schema = "bd_seguridad")
public class Perfil {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perfil_seq")
    @SequenceGenerator(name = "perfil_seq", sequenceName = "seq_sw_perfil", allocationSize = 1)
    @Column(name = "id_perfil")
    private Integer idPerfil;

    @Column(name = "id_entidad", nullable = false)
    private Long idEntidad;

  //  @ManyToOne
  //  @JoinColumn(name = "id_rol", nullable = false)
   // private Rol rol;

    @Column(name = "nombre", nullable = false, length = 500)
    private String nombre;

    @ManyToMany(mappedBy = "perfiles")
    private Set<Usuario> usuarios;

}
