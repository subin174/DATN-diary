package healthcare.api.service;

import healthcare.api.data.RequestParams;
import healthcare.entity.Account;
import healthcare.entity.Diary;
import healthcare.entity.Mood;
import healthcare.entity.UserPrin;
import healthcare.entity.dto.req.DiaryReq;
import healthcare.entity.dto.req.MoodReq;
import healthcare.entity.dto.resp.DiaryResp;
import healthcare.entity.dto.resp.MoodResp;
import healthcare.entity.enums.MoodStatus;
import healthcare.entity.enums.Role;
import healthcare.repository.MoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    final AccountService accountService;

    public Mood getEntityById(long id) {
        return getRepository().findById(id).orElseThrow(() -> new RuntimeException(messageResource.getMessage("id.notfound")));
    }

    public List<?> getAll(RequestParams params) throws Exception
    {
        Specification<Mood> specification =this.buildSpecification(params.getFilter());
        List<Mood>moods= this.getAll(specification);
        return moods.stream().map(mood -> this.entityToResp(mood, MoodResp.class)).collect(Collectors.toList());
    }
    public MoodResp create(MoodReq req) throws Exception {
        UserPrin user = accountService.checkUserPermission(Role.ADMIN.name());
        Mood mood = this.reqToEntity(req,new Mood());
        mood.setCreatedBy(user.getId());
        mood.setStatus(MoodStatus.ACTIVE);
        return this.entityToResp(this.save(mood),MoodResp.class);
    }
    public MoodResp update(MoodReq req, Long id) throws Exception {
        accountService.checkUserPermission(Role.ADMIN.name());
        Mood mood = this.getById(id);
        Mood mood1 = this.reqToEntity(req,mood);
        return this.entityToResp(repository.save(mood1),MoodResp.class);
    }
    public MoodResp delete(Long id) {
        accountService.checkUserPermission(Role.ADMIN.name());
        Mood mood = this.getById(id);
        mood.setStatus(MoodStatus.INACTIVE);
        return this.entityToResp(this.save(mood), MoodResp.class);
    }

}
