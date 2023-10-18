package healthcare.api.controller;

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
        return Arrays.asList("status","createdAt","moodId");
    }
    @Override
    public List<String> getSortableFields() {
        return Arrays.asList("createdAt");
    }
    final SoundService service;
    @GetMapping("/all")
    public ResponseEntity<?> getListSound() throws Exception{
        return responseSuccess(service.getListSound(this.getParams()));
    }
    @GetMapping("/{moodId}")
    public ResponseEntity<?> getListSoundByMood(@PathVariable("moodId") Long moodId) throws Exception{
        return responseSuccess(service.getSoundByMood(moodId));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) throws Exception{
        return responseSuccess(service.getById(id));
    }

}
