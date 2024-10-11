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
@Table(name = "SW_SISTEMA" , schema = "bd_seguridad")
public class Sistema implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sistema_seq")
    @SequenceGenerator(name = "sistema_seq", sequenceName = "seq_sw_sistema", allocationSize = 1, initialValue = 1)
    @Column(name = "id_sistema", nullable = false)
    private Integer idSistema;

    @Column(name = "codigo",nullable = false, length = 50)
    private String codigo;

    @Column(name = "nombre",nullable = false, length = 100)
    private String nombre;

    @Column(name = "version", nullable = false, length = 50)
    public String version;

    @Column(name = "logo_main",nullable = false, length = 50)
    private String logoMain;

    @Column(name = "logo_head",nullable = false, length = 50)
    private String logoHead;

    @Column(name = "url",nullable = false, length = 500)
    private String url;


}
