package healthcare.repository;

import healthcare.entity.MoodSound;
import healthcare.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoodSoundRepository extends JpaRepository<MoodSound, Long>, JpaSpecificationExecutor<MoodSound> {
    Optional<MoodSound> findByMoodSound(String moodSound);
    MoodSound getById(Long id);
    List<MoodSound> getMoodSoundById(Long id);
}
