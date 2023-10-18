package healthcare.repository;

import healthcare.entity.Diary;
import healthcare.entity.DiaryComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryCommentRepository extends JpaRepository<DiaryComment,Long>, JpaSpecificationExecutor<DiaryComment> {

//    @Query(value="select * from diary a where a.status= :status", nativeQuery=true)
    List<DiaryComment> getAllByDiaryId(Long diaryId);
}
