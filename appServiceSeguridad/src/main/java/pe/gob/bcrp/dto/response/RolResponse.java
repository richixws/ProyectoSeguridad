package pe.gob.bcrp.dto.response;

import lombok.Data;
import pe.gob.bcrp.dto.OpcionDTO;
import pe.gob.bcrp.dto.RolDTO;

import java.util.List;

@Data
public class RolResponse {

    private List<RolDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;


}
