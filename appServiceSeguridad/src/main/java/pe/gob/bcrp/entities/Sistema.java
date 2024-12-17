package pe.gob.bcrp.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "SW_SISTEMA")
public class Sistema implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sistema_seq")
    @SequenceGenerator(name = "sistema_seq", sequenceName = "seq_sw_sistema", allocationSize = 1, initialValue = 1)
    @Column(name = "id_sistema", nullable = false)
    private Integer idSystem;

    @Column(name = "codigo",nullable = false, length = 50)
    private String codigo;

    @Column(name = "nombre",nullable = false, length = 100)
    private String name;

    @Column(name = "version", nullable = false, length = 50)
    public String version;

    @Column(name = "logo_main",nullable = false, length = 50)
    private String logoMain;

    @Column(name = "logo_head",nullable = false, length = 50)
    private String logoHead;

    @Column(name = "url",nullable = false, length = 500)
    private String url;

    @Column(name = "is_deleted")
    private boolean isDeleted=false;

    @Column(name = "user_responsable")
    private String userResponsible;

    @Column(name = "id_user_responsable")
    private Integer idUserResponsible;

    @Column(name = "user_responsable_alterno")
    private String userResponsibleAlternate;

    @Column(name = "id_user_responsable_alterno")
    private Integer idUserResponsibleAlternate;

    @Column(name = "url_externo")
    private String urlExternal;

    @Column(name = "estado_critico")
    private String stateCritical;

    @Column(name = "unidad_organizacional")
    private String unitOrganizational;

    @Column(name = "estado", length = 1)
    private Integer estate;


    //agregacion campos auditoria
    @Column(name = "hora_creacion")
    private LocalDateTime horaCreacion;

    @Column(name = "hora_eliminacion")
    private LocalDateTime horaDeEliminacion;

    @Column(name = "hora_actualizacion")
    private LocalDateTime horaActualizacion;

    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    @Column(name = "usuario_eliminacion")
    private String usuarioEliminacion;

    @Column(name = "usuario_actualizacion")
    private String usuarioActualizacion;

}
