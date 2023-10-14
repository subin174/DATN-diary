package healthcare.entity;

import healthcare.entity.enums.DiaryCommentStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="diary_comment")
public class DiaryComment extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private Long diaryId;
    private String avatar;
    private String nickName;
    @Enumerated(EnumType.STRING)
    private DiaryCommentStatus status;

}
