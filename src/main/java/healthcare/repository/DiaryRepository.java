package healthcare.repository;

import healthcare.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long>, JpaSpecificationExecutor<Diary> {
    @Query(value="select * from diary a where a.status= :status", nativeQuery=true)
    List<Diary> getDiariesByStatus(String status);
    @Query(value="select * from diary a where a.created_by= ?1", nativeQuery=true)
    List<Diary> getDiariesByCreatedBy(Long CreatedBy);

    @Query(value="select * from diary a where a.status= :status and a.created_by = :createdBy", nativeQuery=true)
    List<Diary> getDiariesByStatusAndCreatedBy(String status, Long createdBy);
    long countByMoodIdAndCreatedBy(Long moodId, Long createdBy);

    @Query(value = "SELECT COUNT(*) AS count, m.mood " +
            "FROM `diary_dev`.diary a " +
            "JOIN mood m ON a.mood_id = m.id " +
            "WHERE a.created_by = ?1 " +
            "GROUP BY a.mood_id", nativeQuery = true)
    List<Object> getCountByMoodAndCreatedBy( Long createdBy);
    @Query(value = " SELECT COUNT(*) AS count, m.mood " +
            "FROM `diary_dev`.diary a " +
            "JOIN mood m ON a.mood_id = m.id " +
            "WHERE a.created_by = ?1 " +
            "and a.created_at between ?2  AND ?3 " +
            "GROUP BY a.mood_id", nativeQuery = true)
    List<Object> getCountByMoodAndCreatedByAndTime(Long createdBy, String startDate, String endDate);
    @Query(value = "SELECT COUNT(*) AS count, m.mood " +
            "FROM `diary_dev`.diary a " +
            "JOIN mood m ON a.mood_id = m.id " +
            "WHERE a.created_at BETWEEN ?1 AND ?2 " +
            "AND a.status = 'PUBLIC' " +
            "GROUP BY a.mood_id", nativeQuery = true)
    List<Object> getCountDiaryMoodPublicByYear(String startDate, String endDate);

    @Query(value = "SELECT COUNT(*) AS count, m.mood " +
            "FROM `diary_dev`.diary a " +
            "JOIN mood m ON a.mood_id = m.id " +
            "WHERE a.created_at BETWEEN ?1 AND ?2 " +
            "GROUP BY a.mood_id", nativeQuery = true)
    List<Object> getCountMoodByYear(String startDate, String endDate);


    @Query(value = " SELECT COUNT(*) AS count " +
            "FROM `diary_dev`.diary a " +
            "WHERE a.created_at BETWEEN ?1 AND ?2" , nativeQuery = true)
    List<Object> getCountDiaryByYear( String startDate, String endDate);
    @Query(value = " SELECT COUNT(*) AS count " +
            "FROM `diary_dev`.diary a ", nativeQuery = true)
    List<Object> getCountQuantityDiary();

//    select count(*),a.mood_id,m.mood FROM onstora_dev.diary a join mood m on a.mood_id=m.id group by mood_id;
//
//    select count(*),m.mood FROM onstora_dev.diary a join mood m on a.mood_id=m.id group by mood_id;
//
//    select count(*) as num,m.mood FROM onstora_dev.diary a join mood m on a.mood_id=m.id group by mood_id;
}


//`diary-app`


