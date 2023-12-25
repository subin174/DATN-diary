package healthcare.repository;

import healthcare.entity.MoodSound;
import healthcare.entity.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoundRepository extends JpaRepository<Sound, Long>, JpaSpecificationExecutor<Sound> {
    List<Sound> findAllByMoodSound(MoodSound moodSound);
    @Query(value = "SELECT COUNT(*) AS count, m.mood_sound " +
            "FROM `diary-app`.sound a " +
            "JOIN mood_sound m ON a.mood_sound_id = m.id  " +
            "GROUP BY a.mood_sound_id", nativeQuery = true)
    List<Object> getCountMood();

    @Query(value = " SELECT COUNT(*) AS count " +
            "FROM `diary-app`.sound a ", nativeQuery = true)
    List<Object> getCountQuantitySound();
}
