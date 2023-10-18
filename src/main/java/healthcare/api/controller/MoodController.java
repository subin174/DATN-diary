package healthcare.api.controller;

import healthcare.api.service.MoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mood")
public class MoodController extends ApiController {
    final MoodService service;
    @GetMapping("/all")
    public ResponseEntity<?> getListMood() throws Exception{
        return responseSuccess(service.getAll());
    }


}
