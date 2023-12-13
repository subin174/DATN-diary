package healthcare.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import healthcare.entity.enums.DiaryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "diary")
public class Diary  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    //catchit
    @ManyToOne
    @JoinColumn(name = "mood_id")
    @JsonBackReference
    private Mood mood;
    private String happened;
    private String date;
    private String time;
    private String place;
    //checkit
    private String level;
    //changeit
    private String thinkingFelt;
    private String thinkingMoment;
    private String other;
    private LocalDateTime createdAt = LocalDateTime.now();
    private Long createdBy;
    private String nickname;
    private String avatar;
    @Enumerated(EnumType.STRING)
    private DiaryStatus status;

}
