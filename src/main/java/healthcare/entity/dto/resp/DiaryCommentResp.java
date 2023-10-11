package healthcare.entity.dto.resp;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class DiaryCommentResp {
    private Long id;
    private String comment;
    private Long diaryId;
    private LocalDateTime createdAt;
    private String createdBy;
}
