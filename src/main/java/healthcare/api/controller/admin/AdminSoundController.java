package healthcare.api.controller.admin;

import healthcare.api.controller.ApiController;
import healthcare.api.service.SoundService;
import healthcare.entity.dto.req.DiaryReq;
import healthcare.entity.dto.req.SoundReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/sound")
public class AdminSoundController extends ApiController {
    final SoundService service;
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SoundReq req, @RequestPart("audio")MultipartFile file) throws Exception{
        return responseSuccess(service.create(req,file));
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
            @RequestBody SoundReq req
    ) throws Exception {
        return responseSuccess(service.update(req,id));
    }
    @GetMapping("/all")
    public ResponseEntity<?> getListSound() throws Exception{
        return responseSuccess(service.getListSound(this.getParams()));
    }
}
