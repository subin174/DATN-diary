package healthcare.entity.dto.resp;

import healthcare.entity.enums.MoodStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoodResp {
    private Long id;
    private String mood;
    private MoodStatus status;
    private String description;

}
