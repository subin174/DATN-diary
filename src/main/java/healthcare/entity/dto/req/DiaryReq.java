package healthcare.entity.dto.req;

import com.fasterxml.jackson.annotation.JsonBackReference;
import healthcare.entity.Mood;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Setter
public class DiaryReq {
    private String title;
    //catchit

    private Long moodId;
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
}
