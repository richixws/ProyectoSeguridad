package pe.gob.bcrp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.bcrp.dto.opcionDTO.opcionFormDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpcionResponse {

    private List<opcionFormDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
