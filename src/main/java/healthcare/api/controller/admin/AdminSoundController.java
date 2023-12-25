package healthcare.api.controller.admin;


import groovy.util.logging.Slf4j;
import healthcare.api.controller.ApiController;
import healthcare.api.service.DropboxService;
import healthcare.api.service.FileService;
import healthcare.api.service.SoundService;
import healthcare.entity.dto.req.SoundReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("admin/sound")
public class AdminSoundController extends ApiController {
    final SoundService service;
    final DropboxService dropboxService;
    final FileService fileService;
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SoundReq req) throws Exception{
        return responseSuccess(service.create(req));
    }
    @PostMapping("/upload-img")
    public ResponseEntity<?> upLoadImg(@RequestParam MultipartFile multipartFile) throws Exception{
        return responseSuccess(fileService.uploadImageToImgur(multipartFile));
    }
    @PostMapping("/upload")
    public ResponseEntity<?> uploadAudioCloud(@RequestParam("audio") MultipartFile audio) throws IOException {
        return  responseSuccess(dropboxService.uploadAudioCloud(audio));
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
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) throws Exception{
        return responseSuccess(service.findById(id));
    }
    @GetMapping("/mood-sound")
    public ResponseEntity<?> getListSoundByMood(@RequestParam ("moodSound") long moodSound) throws Exception{
        return responseSuccess(service.getSoundByMood(moodSound));
    }
    @GetMapping
    public ResponseEntity<?> getPageSound() throws Exception{
        return responseSuccess(service.getPaginated(this.getParams()));
    }

}
