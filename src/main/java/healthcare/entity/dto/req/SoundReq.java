package healthcare.entity.dto.req;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.ElementCollection;
import java.util.List;

@Getter
@Setter
public class SoundReq {
    private String track;
    private String cate;
    private String title;
    private String author;
    private Long moodId;
    @ElementCollection
    private List<String> images;
}
