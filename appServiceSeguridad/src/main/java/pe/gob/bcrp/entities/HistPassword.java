package pe.gob.bcrp.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SW_HIST_PASSWORD" , schema = "bd_seguridad")
public class HistPassword {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "histpassword_seq")
    @SequenceGenerator(name = "histpassword_seq", sequenceName = "seq_sw_histpassword", allocationSize = 1, initialValue = 1)
    @Column(name = "id_hist_password", nullable = false)
    private Integer idHistPassword;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "fecha_hora")
    private Date fecha_hora;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;



}
