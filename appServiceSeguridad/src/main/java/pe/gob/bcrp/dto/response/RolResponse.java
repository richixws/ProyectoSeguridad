package pe.gob.bcrp.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.bcrp.dto.OpcionDTO;
import pe.gob.bcrp.dto.RolDTO;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class RolResponse implements Serializable {

    private List<RolDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;


}
