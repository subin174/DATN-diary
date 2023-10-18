package healthcare.api.controller.admin;

import healthcare.api.controller.ApiController;
import healthcare.api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/account")
public class AdminAccountController extends ApiController {
    private final AccountService service;
    @PutMapping("/approve/{id}")
    public ResponseEntity<?> save(@PathVariable Long id) throws Exception {
        return responseSuccess(service.approve(id));
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAll() throws Exception {
        return responseSuccess(service.getList(this.getParams()));
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id")Long id) throws Exception {
        return responseSuccess(service.findById(id));
    }
    @GetMapping
    public ResponseEntity<?> getPage() throws Exception {
        return responseSuccess(service.getPage(this.getParams()));
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<?> delete( @PathVariable Long id)throws Exception {
        service.delete(id);
        return responseSuccess();
    }
}
