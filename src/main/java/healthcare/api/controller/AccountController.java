package healthcare.api.controller;

import healthcare.api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("account")
public class AccountController extends ApiController  {
    private final AccountService service;
    @PutMapping("/approve/{id}")
    public ResponseEntity<?> save(@PathVariable Long id) throws Exception {
        return responseSuccess(service.approve(id));
    }
    @GetMapping("/all")

    public ResponseEntity<?> getAll() throws Exception {
        return responseSuccess(service.getList(this.getParams()));
    }
    @GetMapping
    public ResponseEntity<?> getPage() throws Exception {
        return responseSuccess(service.getPage(this.getParams()));
    }

}
