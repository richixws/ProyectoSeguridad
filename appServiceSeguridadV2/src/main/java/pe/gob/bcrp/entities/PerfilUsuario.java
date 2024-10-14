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
@Table(name = "SW_PERFIL_USUARIO" , schema = "bd_seguridad")
public class PerfilUsuario  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perfil_usuario_seq")
    @SequenceGenerator(name = "perfil_usuario_seq", sequenceName = "bd_seguridad.seq_sw_perfil_usuario", allocationSize = 1)
    @Column(name = "id_perfil_usuario" , nullable = false)
    private Long idPerfilUsuario;

    @ManyToOne
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}
