package healthcare.api.controller.admin;

import healthcare.api.controller.ApiController;
import healthcare.api.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/diary")
public class AdminDiaryController extends ApiController{
    @Override
    public List<String> getFilterableFields(){
        return Arrays.asList("id","status");
    }
    @Override
    public List<String> getSortableFields() {
        return Arrays.asList("id","createdAt");
    }
    final DiaryService service;
    @GetMapping("/all")
    public ResponseEntity<?> getAll() throws Exception{
        return responseSuccess(service.getList(this.getParams()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id ) throws Exception {
        return responseSuccess(service.findById(id));
    }
    @GetMapping
    public ResponseEntity<?> getPageAdmin() throws Exception{
        return responseSuccess(service.getPaginated(this.getParams()));
    }
    @GetMapping("/feed")
    public ResponseEntity<?> getDiaryPublic() throws Exception{
        return responseSuccess(service.getDiaryActive(this.getParams()));
    }

}
