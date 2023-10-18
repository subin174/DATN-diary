package healthcare.entity;

import healthcare.entity.enums.MoodStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class MoodSound extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private MoodStatus status;
//    @ManyToMany(mappedBy = "moodSounds")
//    Set<Sound> sounds;
}
