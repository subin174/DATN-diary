package healthcare.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Sound extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String track;
    private String cate;
    private String title;
    private String author;
    private Long moodId;
    @ElementCollection
    private List<String> images;
//    @ManyToMany
//    @JoinTable(
//            name = "mood_sound_like",
//            joinColumns = @JoinColumn(name = "sound_id"),
//            inverseJoinColumns = @JoinColumn(name = "mood_id"))
//    Set<MoodSound> moodSounds;


}
