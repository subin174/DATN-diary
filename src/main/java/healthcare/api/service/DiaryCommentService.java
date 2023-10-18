package healthcare.api.service;

import healthcare.api.data.RequestParams;
import healthcare.entity.Diary;
import healthcare.entity.DiaryComment;
import healthcare.entity.UserPrin;
import healthcare.entity.dto.req.DiaryCommentReq;
import healthcare.entity.dto.resp.DiaryCommentResp;
import healthcare.entity.dto.resp.DiaryResp;
import healthcare.entity.enums.DiaryStatus;
import healthcare.repository.DiaryCommentRepository;
import healthcare.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryCommentService extends BaseService<DiaryComment>{
    private final DiaryRepository diaryRepository;


    @Override
    protected DiaryCommentRepository getRepository() {
        return repository;
    }

    @Override
    protected Class<DiaryComment> getType() {
        return DiaryComment.class;
    }
    final DiaryCommentRepository repository;
    final AccountService accountService;
    final DiaryService diaryService;
    private static final String ENTITY = "DiaryComment";
    private final Validator validator;

    public DiaryCommentResp save(DiaryCommentReq req) throws Exception {
        UserPrin user = accountService.getCurrentUser();
        Diary diary = diaryService.getById(req.getDiaryId());
//        Diary diary = diaryService.getEntityById(req.getDiaryId());
        if (!diary.getStatus().equals(DiaryStatus.PUBLIC)) {
            throw new Exception("diary.not.active");
        }
        DiaryComment comment = this.reqToEntity(req,new DiaryComment());
        comment.setCreatedBy(user.getId());
        comment.setAvatar(user.getAttributes().get("avatar").toString());
        comment.setNickName(user.getAttributes().get("nickname").toString());
        return this.entityToResp(this.save(comment),DiaryCommentResp.class);
    }

    public List<?> getList(RequestParams params) throws Exception {
        Specification<DiaryComment> specification = this.buildSpecification(params.getFilter());
        List<DiaryComment> comments = this.getAll(specification);
        return comments.stream().map(comment -> this.entityToResp(comment, DiaryResp.class)).collect(Collectors.toList());
    }
    public List<?> getListByDiaryId(Long diaryId) throws Exception {
        accountService.getCurrentUser();
        List<DiaryComment> comments = repository.getAllByDiaryId(diaryId);
        return comments.stream().map(comment -> this.entityToResp(comment, DiaryCommentResp.class)).collect(Collectors.toList());
    }
    public DiaryCommentResp update(DiaryCommentReq req, Long id) throws Exception {
        accountService.getCurrentUser();
        DiaryComment comment = this.getById(id);
        DiaryComment diaryComment = this.reqToEntity(req,comment);
        return this.entityToResp(repository.save(diaryComment),DiaryCommentResp.class);
    }

    public void deleteByDiaryOwner(Long id){
        UserPrin user = accountService.getCurrentUser();
        DiaryComment comment = this.getById(id);
        Diary diary = diaryService.getEntityById(comment.getDiaryId());
        if (comment.getCreatedBy().equals(user.getId()) || diary.getCreatedBy().equals(user.getId())){
            this.deleteById(id);
        }
    }

/*    @Transactional
    public DiaryComment create( DiaryComment comment,Long postId) {
        UserPrin user = accountService.getCurrentUser();
        Diary diary = diaryRepository.getReferenceById(postId);
        Set<ConstraintViolation<DiaryComment>> violations = validator.validate(comment);
        if (!violations.isEmpty())
            throw new ResourceValidationException(ENTITY, violations);
        comment.setDiary(diary);
        comment.setCreatedBy(user.getId());
        return repository.save(comment);
    }*/

    @RestController
    @RequiredArgsConstructor
    @RequestMapping("sound")
    public static class SoundService {
    }
}
