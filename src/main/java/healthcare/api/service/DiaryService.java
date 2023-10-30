package healthcare.api.service;

import healthcare.api.data.ConditionBase;
import healthcare.api.data.FilterReq;
import healthcare.api.data.OperatorBase;
import healthcare.api.data.RequestParams;
import healthcare.entity.Diary;
import healthcare.entity.Mood;
import healthcare.entity.UserPrin;
import healthcare.entity.dto.req.DiaryReq;
import healthcare.entity.dto.resp.DiaryResp;
import healthcare.entity.enums.DiaryStatus;
import healthcare.entity.enums.Role;
import healthcare.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiaryService extends BaseService<Diary> {


    @Override
    protected DiaryRepository getRepository() {
        return repository;
    }

    @Override
    protected Class<Diary> getType() {
        return Diary.class;
    }
    final DiaryRepository repository;
    final AccountService accountService;
    final MessageResource messageResource;
    final MoodService moodService;


    public DiaryResp create(DiaryReq req) throws Exception {
        UserPrin user = accountService.getCurrentUser();
        Mood mood = moodService.getEntityById(req.getMoodId());
        Diary diary = this.reqToEntity(req,new Diary());
        diary.setCreatedBy(user.getId());
        diary.setMood(mood);
        return this.entityToResp(this.save(diary),DiaryResp.class);
    }
    public DiaryResp update(DiaryReq req, Long id) throws Exception {
        accountService.getCurrentUser();
        Diary diary = this.getById(id);
        Diary diary1 = this.reqToEntity(req,diary);
        return this.entityToResp(repository.save(diary1),DiaryResp.class);
    }
    public void delete(Long id){
        this.deleteById(id);
    }

    public  List<?> getDiaryActive(RequestParams params)throws Exception{
        List<Diary> diaries = repository.getDiariesByStatus("PUBLIC");
        return diaries.stream().map(diary -> this.entityToResp(diary, DiaryResp.class)).collect(Collectors.toList());
    }
    public DiaryResp findById(Long id){
        Diary diary = this.getById(id);
        return this.entityToResp(diary,DiaryResp.class);
    }
    public DiaryResp getByIdUser(Long id) {
        UserPrin user = accountService.getCurrentUser();
        Diary diary = getById(id);
        if (!diary.getCreatedBy().equals(user.getId())) {
            throw new NoSuchElementException("error.job.permission-denied");
        }
        return entityToResp(diary, DiaryResp.class);
    }

    public Page<?> getPaginated(RequestParams requestParams) throws Exception {
        Specification<Diary> specification = this.buildSpecification(requestParams.getFilter());
        Page<Diary> diaries = this.getPaginated(specification, requestParams.getPageable());
        return diaries.map(diary -> this.entityToResp(diary, DiaryResp.class));
    }
    public List<?> getList(RequestParams params) throws Exception {
        Specification<Diary> specification = this.buildSpecification(params.getFilter());
        List<Diary> diaries = this.getAll(specification);
        return diaries.stream().map(diary -> this.entityToResp(diary, DiaryResp.class)).collect(Collectors.toList());
    }
    public List<?> getListByUserCalendar(RequestParams params) throws Exception {
        UserPrin user = accountService.getCurrentUser();
        params.getFilter().add(getFilterByUser(user.getId()));
        if ( params.getAdditions().get("date") != null ){
            params.getFilter().add(FilterReq.builder()
                    .field("createdAt")
                    .values(Arrays.asList(
                            (params.getAdditions().get("date")[0] + "T00.00.00"),
                            (params.getAdditions().get("date")[0] + "T23.59.59")
                    ))
                    .condition(ConditionBase.AND)
                    .operator(OperatorBase.BETWEEN)
                    .build());

        }
        Specification<Diary> specification = this.buildSpecification( params.getFilter());
        return getAll(specification);
    }

    public List<?> getListDiaryByUser(RequestParams params) throws Exception {
        UserPrin user = accountService.getCurrentUser();
        List<FilterReq> filters = Collections.singletonList(getFilterByUser(user.getId()));
        Specification<Diary> specification=this.buildSpecification(params.getFilter());
        return getAll(specification);

    }
    public FilterReq getFilterByUser(Long id) {
        return FilterReq.builder()
                .field("createdBy")
                .values(Collections.singletonList(id.toString()))
                .operator(OperatorBase.EQUALS)
                .condition(ConditionBase.AND)
                .build();
    }
    public DiaryResp createByAdmin(DiaryReq req) throws Exception {
        UserPrin user = accountService.checkUserPermission(Role.ADMIN.name());
        Mood mood = moodService.getEntityById(req.getMoodId());
        Diary diary = this.reqToEntity(req,new Diary());
        diary.setCreatedBy(user.getId());
        diary.setMood(mood);
        return this.entityToResp(this.save(diary),DiaryResp.class);
    }
    public DiaryResp updateByAdmin(DiaryReq req, Long id) throws Exception {
        accountService.checkUserPermission(Role.ADMIN.name());
        Diary diary = this.getById(id);
        Diary diary1 = this.reqToEntity(req,diary);
        return this.entityToResp(repository.save(diary1),DiaryResp.class);
    }
    public void deleteByAdmin(Long id){
        accountService.checkUserPermission(Role.ADMIN.name());
        this.deleteById(id);
    }
    public Diary getEntityById(long id) {
        return getRepository().findById(id).orElseThrow(() -> new RuntimeException(messageResource.getMessage("id.notfound")));
    }
    public DiaryResp setPrivate(Long id) {
        accountService.getCurrentUser();
        Diary diary = this.getById(id);
        diary.setStatus(DiaryStatus.PRIVATE);
        return this.entityToResp(this.save(diary), DiaryResp.class);
    }
    public DiaryResp setPublic(Long id) {
        accountService.getCurrentUser();
        Diary diary = this.getById(id);
        diary.setStatus(DiaryStatus.PUBLIC);
        return this.entityToResp(this.save(diary), DiaryResp.class);
    }
}
