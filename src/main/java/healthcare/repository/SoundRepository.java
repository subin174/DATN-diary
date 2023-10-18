package healthcare.repository;

import healthcare.entity.Sound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoundRepository extends JpaRepository<Sound, Long>, JpaSpecificationExecutor<Sound> {
    List<Sound> getSoundByMoodId(Long moodId);
}
