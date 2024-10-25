package pe.gob.bcrp.services;

import org.bouncycastle.math.raw.Mod;
import pe.gob.bcrp.dto.EntidadResponse;
import pe.gob.bcrp.dto.ModuloDTO;
import pe.gob.bcrp.dto.ModuloResponse;
import pe.gob.bcrp.entities.Modulo;

import java.util.List;

public interface IModuloService {

    public ModuloResponse getAllModulos(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Integer idSistema);//, String nombre
    public ModuloDTO saveModulo(ModuloDTO modulo);
    public ModuloDTO updateModulo(ModuloDTO modulo, Integer idModulo);
    public boolean deleteModulo(Integer idModulo);

}
