package healthcare.api.service;

import healthcare.entity.Mood;
import healthcare.entity.MoodSound;
import healthcare.repository.MoodSoundRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MoodSoundService extends BaseService<MoodSound>{
    @Override
    protected MoodSoundRepository getRepository() {
        return repository;
    }

    @Override
    protected Class<MoodSound> getType() {
        return MoodSound.class;
    }
    final MoodSoundRepository repository;
    final MessageResource messageResource;
    public MoodSound getEntityById(long id) {
        return getRepository().findById(id).orElseThrow(() -> new RuntimeException(messageResource.getMessage("id.notfound")));
    }

}
