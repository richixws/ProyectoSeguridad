package pe.gob.bcrp.enumerador;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public enum EstadoCritico {

    BAJ(1, "BAJO"),
    ALT(2, "ALTO"),
    MALT(3, "MUY ALTO"),
    MED(4,"MEDIO");

    private int codigo;
    private String descripcion;

    EstadoCritico(Integer codigo, String estCritico) {
        this.codigo = codigo;
        this.descripcion = estCritico;

    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
