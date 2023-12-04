package healthcare.api.service;

import healthcare.api.data.RequestParams;
import healthcare.entity.Diary;
import healthcare.entity.Mood;
import healthcare.entity.MoodSound;
import healthcare.entity.dto.resp.DiaryResp;
import healthcare.entity.dto.resp.MoodSoundResp;
import healthcare.entity.enums.Role;
import healthcare.repository.MoodSoundRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    final AccountService accountService;
    public MoodSound getEntityById(long id) {
        return getRepository().findById(id).orElseThrow(() -> new RuntimeException(messageResource.getMessage("id.notfound")));
    }
    public List<MoodSound> getList(){
       return repository.findAll();
    }

}
