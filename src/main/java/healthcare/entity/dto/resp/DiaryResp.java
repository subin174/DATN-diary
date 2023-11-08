package healthcare.entity.dto.resp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import healthcare.entity.DiaryComment;
import healthcare.entity.Mood;
import healthcare.entity.enums.DiaryStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DiaryResp {
    private Long id;
    private String title;
    //catchit
    private String mood;
    private String happened;
    private String thinkingMoment;
    private LocalDateTime time;
    private String place;
    //checkit
    private String thinkingNow;
    //changeit
    private String thinkingFelt;
    private String change;
    private String other;
    private DiaryStatus status;
    private String createdBy;
    private String createdAt;
    private String nickname;
    private String avatar;
}
