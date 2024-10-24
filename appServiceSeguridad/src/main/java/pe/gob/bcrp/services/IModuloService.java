package pe.gob.bcrp.services;

import pe.gob.bcrp.entities.Modulo;

import java.util.List;

public interface IModuloService {

    public List<Modulo> findAllModulos();
    public Modulo findModuloById(int idModulo);
    public Modulo saveModulo(Modulo modulo);

}
