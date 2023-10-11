package healthcare.entity.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiaryCommentReq {
    private String comment;
    private Long diaryId;
}
