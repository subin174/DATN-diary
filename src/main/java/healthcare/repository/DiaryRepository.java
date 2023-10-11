package healthcare.repository;

import healthcare.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long>, JpaSpecificationExecutor<Diary> {
    @Query(value="select * from diary a where a.status= :status", nativeQuery=true)
    List<Diary> getDiariesByStatus(String status);
//    @Query(value="select * from diary a where a.?1= :?1", nativeQuery=true)
//    List<Diary> getDiariesByStatus(Long id);
//    Diary findByCreatedByAndId (Long createdBy);
//
//    Diary findByIdAndAndCreatedBy();
}
