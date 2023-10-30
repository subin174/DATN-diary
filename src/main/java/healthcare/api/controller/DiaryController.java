package healthcare.api.controller;

import healthcare.api.service.DiaryService;
import healthcare.entity.dto.req.DiaryReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("diary")
@Slf4j
public class DiaryController extends ApiController {
    @Override
    public List<String> getFilterableFields(){
        return Arrays.asList("id","status","createdAt","createdBy","thinkingMoment","moodId");
    }
    @Override
    public List<String> getSortableFields() {
        return Arrays.asList("id","createdAt");
    }
    final DiaryService service;
    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody DiaryReq Req
    ) throws Exception {
        return responseSuccess(service.create(Req));
    }
    /*@GetMapping
    public ResponseEntity<?> getPageDiaryUser(@RequestParam(required = false) String date) throws Exception{
        return responseSuccess(service.getPageUser(this.getParams()));
    }*/
    @GetMapping("/feed")
    public ResponseEntity<?> getDiaryPublic() throws Exception{
        return responseSuccess(service.getDiaryActive(this.getParams()));
    }

    @GetMapping("/calendar")
    public ResponseEntity<?> getListByUserCalendar(@RequestParam(required = false) String date) throws Exception{
        return responseSuccess(service.getListByUserCalendar(this.getParams()));
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAll() throws Exception{
        return responseSuccess(service.getListDiaryByUser(this.getParams()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id ) throws Exception {
        return responseSuccess(service.getByIdUser(id));
    }
    @GetMapping("/private/{id}")
    public ResponseEntity<?> setPrivate(@PathVariable Long id ) throws Exception {
        return responseSuccess(service.setPrivate(id));
    }
    @GetMapping("/public/{id}")
    public ResponseEntity<?> setPublic(@PathVariable Long id ) throws Exception {
        return responseSuccess(service.setPublic(id));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ){
        service.delete(id);
        return responseSuccess();
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody DiaryReq diaryReq
    ) throws Exception {
        return responseSuccess(service.update(diaryReq,id));
    }

}
