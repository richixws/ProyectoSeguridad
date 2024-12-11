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
@Table(name = "SW_ROL")
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rol_seq")
    @SequenceGenerator(name = "rol_seq", sequenceName = "seq_sw_rol", allocationSize = 1, initialValue = 1)
    @Column(name = "id_rol", nullable = false)
    private Integer idRol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sistema", nullable = false)
    private Sistema sistema;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "estado", nullable = false)
    private Integer estado;

   // @Column(name = "ultlin", nullable = false)
   // private Integer ultLin;

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
