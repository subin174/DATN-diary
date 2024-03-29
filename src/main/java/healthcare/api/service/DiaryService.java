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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
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

    public void deleteById(Long id) {
        getRepository().deleteById(id);
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
        Collections.sort(diaries, Comparator.comparing(Diary::getCreatedAt).reversed());
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
        return repository.getCountByMoodAndCreatedByAndTime(createdBy, start.toString(), end.toString());
    }

    @Getter
    @Setter
    private String month;
    @Getter
    @Setter
    private List<Map<String, Object>> data;

    @Getter
    @Setter
    public static class MoodCount {
        private int count;
        private String mood;

    }
    private List<MoodCount> convertToMoodCountList(List<Object> monthlyResult) {
        List<MoodCount> moodCounts = new ArrayList<>();
        for (Object result : monthlyResult) {
            Object[] resultArray = (Object[]) result;
            MoodCount moodCount = new MoodCount();
            moodCount.setCount(((BigInteger) resultArray[0]).intValue()); // Convert BigInteger to int
            moodCount.setMood((String) resultArray[1]);
            moodCounts.add(moodCount);
        }
        return moodCounts;
    }

    public Map<String, List<MoodCount>> getCountByMoodAndCreatedByYear(Integer year) {
        UserPrin userPrin = accountService.getCurrentUser();
        Long createdBy = userPrin.getId();
        Map<String, List<MoodCount>> resultMap = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] monthNames = new DateFormatSymbols().getShortMonths();

        if (Objects.nonNull(year)) {
            calendar.set(Calendar.YEAR, year);
        }

        for (int i = 1; i <= 12; i++) {
            calendar.set(Calendar.MONTH, i - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String startDate = dateFormat.format(calendar.getTime());

            calendar.set(Calendar.MONTH, i);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String endDate = dateFormat.format(calendar.getTime());

            List<Object> monthlyResult = repository.getCountByMoodAndCreatedByAndTime(createdBy, startDate, endDate);
            resultMap.put(monthNames[i - 1], convertToMoodCountList(monthlyResult));
        }

        return resultMap;
    }
    public Map<String, List<MoodCount>> getCountByMoodAndCreatedByMonth(Integer month,Integer year) {
        UserPrin userPrin = accountService.getCurrentUser();
        Long createdBy = userPrin.getId();
        Map<String, List<MoodCount>> resultMap = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] monthNames = new DateFormatSymbols().getShortMonths();
        if (Objects.nonNull(year)) {
            calendar.set(Calendar.YEAR, year);
        }
        if (Objects.nonNull(month)) {
            calendar.set(Calendar.MONTH, month);
        } else {
            month = 1;
        }
        calendar.set(Calendar.MONTH,month -1);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        String startDate = dateFormat.format(calendar.getTime());

        calendar.set(Calendar.MONTH,month );
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        String endDate = dateFormat.format(calendar.getTime());

        List<Object> monthlyResult = repository.getCountByMoodAndCreatedByAndTime(createdBy, startDate, endDate);
        resultMap.put(monthNames[month - 1],convertToMoodCountList(monthlyResult) );

        return resultMap;
    }



//    public Map<String, List<MoodCount>> getCountByMoodAndCreatedByMonth(Integer i) {
//        UserPrin userPrin = accountService.getCurrentUser();
//        Long createdBy = userPrin.getId();
//        Map<String, List<MoodCount>> resultMap = new LinkedHashMap<>();
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String[] monthNames = new DateFormatSymbols().getShortMonths();
//        calendar.set(Calendar.MONTH,i -1);
//        calendar.set(Calendar.DAY_OF_MONTH,1);
//        String startDate = dateFormat.format(calendar.getTime());
//
//        calendar.set(Calendar.MONTH,i );
//        calendar.add(Calendar.DAY_OF_MONTH,-1);
//        String endDate = dateFormat.format(calendar.getTime());
//
//        List<Object> monthlyResult = repository.getCountByMoodAndCreatedByAndTime(createdBy, startDate, endDate);
//        resultMap.put(monthNames[i - 1],convertToMoodCountList(monthlyResult) );
//
//        return resultMap;
//    }

    public Map<String, List<Object>> getCountMoodByYear() {
        Map<String, List<Object>> resultMap = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] monthNames = new DateFormatSymbols().getShortMonths();

        for (int i = 1; i <= 12; i++) {
            calendar.set(Calendar.MONTH, i - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String startDate = dateFormat.format(calendar.getTime());


            calendar.set(Calendar.MONTH, i);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String endDate = dateFormat.format(calendar.getTime());


            List<Object> monthlyResult = repository.getCountMoodByYear(startDate, endDate);
            resultMap.put(monthNames[i - 1], monthlyResult);
        }

        return resultMap;
    }
    public Map<String, List<Object>> getCountDiaryMoodPublicByYear() {
        Map<String, List<Object>> resultMap = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] monthNames = new DateFormatSymbols().getShortMonths();

        for (int i = 1; i <= 12; i++) {
            calendar.set(Calendar.MONTH, i - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String startDate = dateFormat.format(calendar.getTime());


            calendar.set(Calendar.MONTH, i);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String endDate = dateFormat.format(calendar.getTime());


            List<Object> monthlyResult = repository.getCountDiaryMoodPublicByYear(startDate, endDate);
            resultMap.put(monthNames[i - 1], monthlyResult);
        }

        return resultMap;
    }

    public Map<String, List<Object>> getCountDiaryByYear() {
        Map<String, List<Object>> resultMap = new LinkedHashMap<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String[] monthNames = new DateFormatSymbols().getShortMonths();
        for (int i = 1; i <= 12; i++) {
            calendar.set(Calendar.MONTH, i - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            String startDate = dateFormat.format(calendar.getTime());


            calendar.set(Calendar.MONTH, i);
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            String endDate = dateFormat.format(calendar.getTime());


            List<Object> monthlyResult = repository.getCountDiaryByYear( startDate, endDate);
            resultMap.put(monthNames[i - 1], monthlyResult);
        }
        return resultMap;
    }
    public Object getCountQuantityDiary() {
        return repository.getCountQuantityDiary();
    }











    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int i = 7;


            calendar.set(Calendar.MONTH,i -1);
            calendar.set(Calendar.DAY_OF_MONTH,1);
//            String b = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1 )+"-" + calendar.get(Calendar.DAY_OF_MONTH);
            String startDate = dateFormat.format(calendar.getTime());
            System.out.println( startDate);

            calendar.set(Calendar.MONTH,i );
            calendar.add(Calendar.DAY_OF_MONTH,-1);
//            String a = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1 )+"-" + calendar.get(Calendar.DAY_OF_MONTH);
            String endDate = dateFormat.format(calendar.getTime());
            System.out.println(endDate);

    }

}
