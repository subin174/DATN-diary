package healthcare.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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
    @ElementCollection
    private List<String> images;


}
