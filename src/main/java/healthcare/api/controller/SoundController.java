package healthcare.api.controller;

import healthcare.api.service.DropboxService;
import healthcare.api.service.MoodSoundService;
import healthcare.api.service.SoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("sound")
public class SoundController extends ApiController{
    @Override
    public List<String> getFilterableFields(){
        return Arrays.asList("status","createdAt","moodSoundId");
    }
    @Override
    public List<String> getSortableFields() {
        return Arrays.asList("createdAt");
    }
    final SoundService service;
    final DropboxService dropboxService;
    final MoodSoundService moodSoundService;
    @GetMapping("/all")
    public ResponseEntity<?> getListSound() throws Exception{
        return responseSuccess(service.getListSound(this.getParams()));
    }
    @GetMapping("/mood-sound")
    public ResponseEntity<?> getListSoundByMood(@RequestParam ("moodSound") long moodSound) throws Exception{
        return responseSuccess(service.getSoundByMood(moodSound));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) throws Exception{
        return responseSuccess(service.findById(id));
    }
    @GetMapping
    public ResponseEntity<?> getPageSound() throws Exception{
        return responseSuccess(service.getPaginated(this.getParams()));
    }
//    @GetMapping("/all-mood-sound")
//    public ResponseEntity<?> getListMoodSound() throws Exception{
//        return responseSuccess(moodSoundService.getList(this.getParams()));
//    }
}
//    @RequestParam("moodSound") String moodSound
