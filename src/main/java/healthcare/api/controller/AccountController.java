package healthcare.api.controller;

import healthcare.api.service.AccountService;
import healthcare.entity.dto.account.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("account")
public class AccountController extends ApiController  {
    private final AccountService service;
    @Override
    public List<String> getFilterableFields(){
        return Arrays.asList("id","status","createdAt","createdBy","phone","email");
    }
    @Override
    public List<String> getSortableFields() {
        return Arrays.asList("id","createdAt");
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<?> delete( @PathVariable Long id)throws Exception {
        service.delete(id);
        return responseSuccess();
    }
    @GetMapping("/search")
    public  ResponseEntity<?> findByPhone(@RequestParam(name = "phone") String phone) throws  Exception{
        return responseSuccess(service.findByPhone(phone));
    }
    @PutMapping("/update-info")
    public ResponseEntity<?> updateAccount(@RequestBody AccountDto accountDto) throws Exception{
        return responseSuccess(service.update(accountDto));
    }
    @GetMapping ("/update-pass")
    public ResponseEntity<?> updatePass(@RequestParam String oldPassword,
                                        @RequestParam String password) throws Exception{
        return responseSuccess(service.updatePass(oldPassword,password));
    }

}
