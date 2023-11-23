package healthcare.entity.dto.req;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.ElementCollection;
import java.util.List;

@Getter
@Setter
public class SoundReq {
    private String track;
    private MultipartFile poster;
    private String cate;
    private String title;
    private String author;
    private Long moodId;
    @ElementCollection
    private List<String> images;
}
