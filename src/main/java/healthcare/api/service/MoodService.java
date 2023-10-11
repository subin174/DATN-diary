package healthcare.api.service;

import healthcare.entity.Account;
import healthcare.entity.Mood;
import healthcare.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MoodService extends BaseService<Mood>{
    @Override
    protected MoodRepository getRepository() {
        return repository;
    }

    @Override
    protected Class getType() {
        return Mood.class;
    }
    final MoodRepository repository;
    private final MessageResource messageResource;
    private RuntimeException notFoundException(long id) {
        return new RuntimeException(String.valueOf(id));
    }
    public Mood getEntityById(long id) {
        return getRepository().findById(id).orElseThrow(() -> new RuntimeException(messageResource.getMessage("id.notfound")));
    }

}
