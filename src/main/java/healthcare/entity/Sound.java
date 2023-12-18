package healthcare.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    private String poster;
    private String track;
    private String cate;
    private String title;
    private String author;

    @ManyToOne
    @JoinColumn(name = "mood_sound_id")
    @JsonBackReference
    private MoodSound moodSound ;

//    @ElementCollection
//    private List<String> images;



}
