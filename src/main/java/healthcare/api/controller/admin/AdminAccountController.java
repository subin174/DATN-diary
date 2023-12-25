package healthcare.api.controller.admin;

import healthcare.api.controller.ApiController;
import healthcare.api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin/account")
public class AdminAccountController extends ApiController {
    private final AccountService service;
    @Override
    public List<String> getFilterableFields(){
        return Arrays.asList("id","status","createdAt","createdBy","phone","email");
    }
    @Override
    public List<String> getSortableFields() {
        return Arrays.asList("id","createdAt");
    }
    @GetMapping("/approve/{id}")
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
        service.deleteAccountAndRoles(id);
        return responseSuccess();
    }
    @GetMapping("/count-account-by-year")
    public ResponseEntity<Map<String, List<Object>>> getCountAccountByByYear() {
        Map<String, List<Object>> result = service.getCountAccByYear();
        return ResponseEntity.ok(result);
    }
    @GetMapping("/count-quantity-account")
    public ResponseEntity<Object> getCountQuantityAccount() {
        Object object = service.getCountQuantityAccount();
        return ResponseEntity.ok(object);
    }
}
