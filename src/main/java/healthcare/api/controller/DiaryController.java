package healthcare.api.controller;

import healthcare.api.service.DiaryService;
import healthcare.entity.dto.req.DiaryReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("diary")
@Slf4j
public class DiaryController extends ApiController {
    @Override
    public List<String> getFilterableFields() {
        return Arrays.asList("id", "status", "createdAt", "createdBy", "thinkingMoment", "moodId");
    }

    @Override
    public List<String> getSortableFields() {
        return Arrays.asList("id", "createdAt");
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
    public ResponseEntity<?> getDiaryPublic() throws Exception {
        return responseSuccess(service.getDiaryActive(this.getParams()));
    }

    @GetMapping("/user-detail")
    public ResponseEntity<?> getDiaryByCreatedBy(@RequestParam Long createdBy) {
        return responseSuccess(service.getDiaryByCreatedBy(createdBy));
    }

    @GetMapping("/feed/user")
    public ResponseEntity<?> getDiaryPublicUser(@RequestParam Long createdBy) throws Exception {
        return responseSuccess(service.getDiaryActiveByCreatedBy(this.getParams(), createdBy));
    }

    @GetMapping("/calendar")
    public ResponseEntity<?> getListByUserCalendar(@RequestParam(required = false) String date) throws Exception {
        return responseSuccess(service.getListByUserCalendar(this.getParams()));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() throws Exception {
        return responseSuccess(service.getListDiaryByUser(this.getParams()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) throws Exception {
        return responseSuccess(service.getByIdUser(id));
    }

    @GetMapping("/private/{id}")
    public ResponseEntity<?> setPrivate(@PathVariable Long id) throws Exception {
        return responseSuccess(service.setPrivate(id));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<?> setPublic(@PathVariable Long id) throws Exception {
        return responseSuccess(service.setPublic(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id
    ) {
        service.delete(id);
        return responseSuccess();
    }
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteById(
            @PathVariable Long id
    ) {
        service.deleteById(id);
        return responseSuccess();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody DiaryReq diaryReq
    ) throws Exception {
        return responseSuccess(service.update(diaryReq, id));
    }

    @GetMapping("/countByMoodAndCreatedBy/{createdBy}")
    public ResponseEntity<List<Object>> getCountByMoodAndCreatedBy(@PathVariable Long createdBy) {
        List<Object> result = service.getCountByMoodAndCreatedBy(createdBy);
        return (ResponseEntity<List<Object>>) responseSuccess(result);
    }


    @GetMapping("/countByMoodAndCreatedByByStartTimeEndTime/{createdBy}")
    public ResponseEntity<List<Object>> getCountByMoodAndCreatedByAndTime(
            @PathVariable Long createdBy,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Object> result = service.getCountByMoodAndCreatedByAndTime(createdBy, start, end);
        return (ResponseEntity<List<Object>>) responseSuccess(result);
    }
    @GetMapping("/countByMoodAndCreatedByByYear")
    public ResponseEntity<?> getCountByMoodAndCreatedByByYear(@RequestParam(required = false) Integer year) {
        Map<String, List<Object>> result = service.getCountByMoodAndCreatedByYear(year);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/countByMoodAndCreatedByByMonth/{createdBy}")
    public ResponseEntity<Map<String, List<Object>>> getCountByMoodAndCreatedByByMonth(@PathVariable Long createdBy, @RequestParam("month")Integer i) {
        Map<String, List<Object>> result = service.getCountByMoodAndCreatedByMonth(createdBy,i);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/count-mood-by-year")
    public ResponseEntity<Map<String, List<Object>>> getCountMoodByYear() {
        Map<String, List<Object>> result = service.getCountMoodByYear();
        return ResponseEntity.ok(result);
    }
    @GetMapping("/count-diary-mood-public-by-year")
    public ResponseEntity<Map<String, List<Object>>> getCountDiaryMoodPublicByYear() {
        Map<String, List<Object>> result = service.getCountDiaryMoodPublicByYear();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/count-diary-by-year")
    public ResponseEntity<Map<String, List<Object>>> getCountDiaryByByYear() {
        Map<String, List<Object>> result = service.getCountDiaryByYear();
        return ResponseEntity.ok(result);
    }
    @GetMapping("/count-quantity-diary")
    public ResponseEntity<Object> getCountQuantityDiary() {
        Object object = service.getCountQuantityDiary();
        return ResponseEntity.ok(object);
    }


}
