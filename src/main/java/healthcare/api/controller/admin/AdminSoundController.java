package healthcare.api.controller.admin;

import groovy.util.logging.Slf4j;
import healthcare.api.controller.ApiController;
import healthcare.api.service.DropboxService;
import healthcare.api.service.SoundService;
import healthcare.entity.dto.req.SoundReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("admin/sound")
public class AdminSoundController extends ApiController {
    final SoundService service;
    final DropboxService dropboxService;
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SoundReq req) throws Exception{
        return responseSuccess(service.create(req));
    }
    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("audio") MultipartFile audio) {
        return  responseSuccess(dropboxService.uploadAudioFileToDropboxV2(audio));
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
