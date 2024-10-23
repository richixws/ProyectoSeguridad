package pe.gob.bcrp.enumerador;

public enum DocuIdentidad {

    DNI(1, "DNI"),
    PASA(2, "PASAPORTE"),
    CEXT(3, "CARNET DE EXTRANJERA"),
    CIDE(4,"CEDULA DE IDENTIDAD");

    private Integer idDocuIdentidad;
    private String  tipoDocuIdentidad;

    DocuIdentidad(Integer idDocuIdentidad, String tipoDocuIdentidad) {
        this.idDocuIdentidad = idDocuIdentidad;
        this.tipoDocuIdentidad = tipoDocuIdentidad;
    }


    public Integer getIdDocuIdentidad() {
        return idDocuIdentidad;
    }

    public void setIdDocuIdentidad(Integer idDocuIdentidad) {
        this.idDocuIdentidad = idDocuIdentidad;
    }

    public String getTipoDocuIdentidad() {
        return tipoDocuIdentidad;
    }

    public void setTipoDocuIdentidad(String tipoDocuIdentidad) {
        this.tipoDocuIdentidad = tipoDocuIdentidad;
    }
}
