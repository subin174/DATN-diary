package healthcare.entity.dto.req;

import com.fasterxml.jackson.annotation.JsonBackReference;
import healthcare.entity.Mood;
import healthcare.entity.enums.DiaryStatus;
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
    private String date;
    private String time;
    private String place;
    //checkit
    private String level;
    //changeit
    private String thinkingFelt;
    private String other;
    private DiaryStatus status;
}
