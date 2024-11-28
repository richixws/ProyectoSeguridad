package pe.gob.bcrp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.gob.bcrp.dto.personaDTO.PersonaFormDTO;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonaResponse {

    private List<PersonaFormDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;

}
