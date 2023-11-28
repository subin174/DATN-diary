package healthcare.entity.dto.resp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.ElementCollection;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class SoundResp {
    private Long id;
    private String poster;
    private String track;
    private String cate;
    private String title;
    private String author;
    private String moodSound;
    @ElementCollection
    private List<String> images;
    private LocalDateTime createdAt;
    private Long createdBy;
}
