package pe.gob.bcrp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.bcrp.dto.moduloDTO.ModuloFormDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModuloResponse {

    private List<ModuloFormDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
