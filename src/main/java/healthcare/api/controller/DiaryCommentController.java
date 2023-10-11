package healthcare.api.controller;

import healthcare.api.service.DiaryCommentService;
import healthcare.entity.DiaryComment;
import healthcare.entity.dto.req.DiaryCommentReq;
import healthcare.entity.dto.req.DiaryReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("comments")
@Slf4j
public class DiaryCommentController extends ApiController{
    final DiaryCommentService service;
    @PostMapping
    public ResponseEntity<?> save(
            @RequestBody DiaryCommentReq Req
    ) throws Exception {
        return responseSuccess(service.save(Req));
    }
    @GetMapping("/{diaryId}")
    public ResponseEntity<?> getListComment(
            @PathVariable ("diaryId") Long diaryId
    ) throws Exception {
        return responseSuccess(service.getListByDiaryId(diaryId));
    }
/*    @PostMapping(value="/{postId}")
    public DiaryComment create(@RequestBody DiaryComment resource, @PathVariable Long postId) {
        return service.create(resource,postId);
    }*/
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ){
        service.deleteByDiaryOwner(id);
        return responseSuccess();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody DiaryCommentReq req
    ) throws Exception {
        return responseSuccess(service.update(req,id));
    }
}
