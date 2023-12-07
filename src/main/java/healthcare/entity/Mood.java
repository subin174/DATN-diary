package healthcare.entity;

import healthcare.entity.enums.MoodStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "mood")
public class Mood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mood;
    private MoodStatus status;
    private String description;
}
