package healthcare.api.service;

import com.google.api.client.util.DateTime;
import healthcare.api.data.ConditionBase;
import healthcare.api.data.FilterReq;
import healthcare.api.data.OperatorBase;
import healthcare.api.data.RequestParams;
import healthcare.entity.*;
import healthcare.entity.dto.req.DiaryReq;
import healthcare.entity.dto.resp.DiaryCommentResp;
import healthcare.entity.dto.resp.DiaryResp;
import healthcare.entity.enums.DiaryStatus;
import healthcare.entity.enums.Role;
import healthcare.repository.DiaryRepository;
import healthcare.repository.MoodRepository;
import healthcare.repository.MoodSoundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    final MoodRepository moodRepository;
    final AccountService accountService;
    final MessageResource messageResource;
    final MoodService moodService;


    public DiaryResp create(DiaryReq req) throws Exception {
        UserPrin user = accountService.getCurrentUser();
        Mood mood = moodService.getEntityById(req.getMoodId());
        Diary diary = this.reqToEntity(req,new Diary());
        diary.setCreatedBy(user.getId());
        diary.setNickname(user.getAttributes().get("nickname").toString());
        diary.setAvatar(user.getAttributes().get("avatar").toString());
        diary.setMood(mood);
        return this.entityToResp(this.save(diary),DiaryResp.class);
    }
    public DiaryResp update(DiaryReq req, Long id) throws Exception {
        UserPrin user =accountService.getCurrentUser();
        Diary diary = this.getById(id);
        if (diary.getCreatedBy().equals(user.getId()) || user.getAuthorities().stream().anyMatch(role -> role.equals(new SimpleGrantedAuthority("ADMIN")))){
            Diary diary1 = this.reqToEntity(req,diary);
            return this.entityToResp(repository.save(diary1),DiaryResp.class);
        }
        else {
            throw new Exception("not-user");
        }
    }
    public void delete(Long id){
        UserPrin user = accountService.getCurrentUser();
        Diary diary = this.getById(id);

        if (diary.getCreatedBy().equals(user.getId()) ||
                user.getAuthorities().stream().anyMatch(role -> role.equals(new SimpleGrantedAuthority("ADMIN")))){
            this.deleteById(id);
        }
    }

    public DiaryResp findById(Long id){
        Diary diary = this.getById(id);
        return this.entityToResp(diary,DiaryResp.class);
    }
    public DiaryResp getByIdUser(Long id) {
        UserPrin user = accountService.getCurrentUser();
        Diary diary = getById(id);
        if (!diary.getCreatedBy().equals(user.getId())) {
            throw new NoSuchElementException("error.diary.permission-denied");
        }
        return entityToResp(diary, DiaryResp.class);
    }

    public Page<?> getPaginated(RequestParams requestParams) throws Exception {
        accountService.checkUserPermission(Role.ADMIN.name());
        Specification<Diary> specification = this.buildSpecification(requestParams.getFilter());
        Page<Diary> diaries = this.getPaginated(specification, requestParams.getPageable());
        return diaries.map(diary -> this.entityToResp(diary, DiaryResp.class));
    }
    public  List<?> getDiaryActive(RequestParams requestParams)throws Exception{
        accountService.getCurrentUser();
        List<Diary> diaries = repository.getDiariesByStatus("PUBLIC");
        return diaries.stream().map(diary -> this.entityToResp(diary, DiaryResp.class)).collect(Collectors.toList());
    }
    public  List<?> getDiaryActiveByCreatedBy(RequestParams requestParams,Long createdBy)throws Exception{
        accountService.getCurrentUser();
        List<Diary> diaries = repository.getDiariesByStatusAndCreatedBy("PUBLIC", createdBy);
        return diaries.stream().map(diary -> this.entityToResp(diary, DiaryResp.class)).collect(Collectors.toList());
    }
    public  List<?> getDiaryByCreatedBy(Long createdBy){
        List<Diary> diaries = repository.getDiariesByCreatedBy(createdBy);
        return diaries.stream().map(diary -> this.entityToResp(diary, DiaryResp.class)).collect(Collectors.toList());
    }
    public List<?> getList(RequestParams params) throws Exception {
//        accountService.checkUserPermission(Role.ADMIN.name());
        Specification<Diary> specification = this.buildSpecification(params.getFilter());
        List<Diary> diaries = this.getAll(specification);
        Collections.sort(diaries, Comparator.comparing(Diary::getCreatedAt).reversed());
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
        List<Diary>diaries = this.getAll(specification);
        return diaries.stream().map(diary -> this.entityToResp(diary, DiaryResp.class)).collect(Collectors.toList());
    }

    public List<?> getListDiaryByUser(RequestParams params) throws Exception {
        UserPrin user = accountService.getCurrentUser();
        params.getFilter().add(getFilterByUser(user.getId()));
        Specification<Diary> specification=this.buildSpecification(params.getFilter());
        List<Diary>diaries = this.getAll(specification);
        return diaries.stream().map(diary -> this.entityToResp(diary, DiaryResp.class)).collect(Collectors.toList());
    }

    public FilterReq getFilterByUser(Long id) {
        return FilterReq.builder()
                .field("createdBy")
                .values(Collections.singletonList(id.toString()))
                .operator(OperatorBase.EQUALS)
                .condition(ConditionBase.AND)
                .build();
    }
    public void deleteByAdmin(Long id){
        accountService.checkUserPermission(Role.ADMIN.name());
        this.deleteById(id);
    }
    public Diary getEntityById(long id) {
        return getRepository().findById(id).orElseThrow(() -> new RuntimeException(messageResource.getMessage("id.notfound")));
    }
    public DiaryResp setPrivate(Long id) {
        UserPrin userPrin = accountService.getCurrentUser();
        Diary diary = this.getById(id);
        if (diary.getCreatedBy().equals(userPrin.getId())){
            diary.setStatus(DiaryStatus.PRIVATE);
        }
        return this.entityToResp(this.save(diary), DiaryResp.class);
    }
    public DiaryResp setPublic(Long id) {
        UserPrin userPrin = accountService.getCurrentUser();
        Diary diary = this.getById(id);
        if (diary.getCreatedBy().equals(userPrin.getId())){
            diary.setStatus(DiaryStatus.PUBLIC);
        }
        return this.entityToResp(this.save(diary), DiaryResp.class);
    }
    public Map<Long, Long> getChart(Long createdBy) {
        Map<Long, Long> moodCounts = new HashMap<>();
        List<Mood> mood = moodRepository.findAll();
        mood.forEach(m -> moodCounts.put(Long.valueOf(m.getMood()), repository.countByMoodIdAndCreatedBy(m.getId(), createdBy)));
        return moodCounts;
    }
    public List<Object> getCountByMoodAndCreatedBy(Long createdBy) {
        return repository.getCountByMoodAndCreatedBy(createdBy);
    }
    public List<Object> getCountByMoodAndCreatedByAndTime(Long createdBy, LocalDate start, LocalDate end) {
//
//        LocalDateTime startDate = LocalDateTime.of(start, LocalTime.of(0,0,0));
//        LocalDateTime endDate = LocalDateTime.of(end, LocalTime.of(23,59,59));
        return repository.getCountByMoodAndCreatedByAndTime(createdBy, start.toString(), end.toString());
    }


    public static void main(String[] args) {
        DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ISO_DATE_TIME;
    }

}
