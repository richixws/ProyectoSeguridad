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
@Table(name = "SW_FILES", schema = "bd_seguridad")
public class Files implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "files_seq")
    @SequenceGenerator(name = "files_seq", sequenceName = "bd_seguridad.seq_sw_files", allocationSize = 1,initialValue = 1)
    @Column(name = "id_files", nullable = false)
    private Long idFiles;

    //@Column(name = "id_sistema", nullable = false)
    //private Integer idSistema;

    @Column(name = "filename", nullable = false, length = 255)
    private String filename;

    @Column(name = "path", nullable = false, length = 1000)
    private String path;

    @Column(name = "extension", nullable = false, length = 50)
    private String extension;

    @Column(name = "mime", nullable = false, length = 50)
    private String mime;

    @Column(name = "sizes", nullable = false, precision = 25)
    private Integer sizes;

    @Column(name = "id_identity", nullable = false, precision = 10)
    private Integer idIdentidad;

    @Column(name = "id_modulo", nullable = false, length = 50)
    private String idModulo;

    @Column(name = "id_usuario", nullable = false, length = 50)
    private String idUsuario;

    @Column(name = "fecha_hora")
    private LocalDateTime horaFechaFile;

}
