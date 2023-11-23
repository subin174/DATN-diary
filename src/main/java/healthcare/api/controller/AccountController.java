package healthcare.api.controller;

import healthcare.api.service.AccountService;
import healthcare.entity.dto.account.AccountDto;
import healthcare.entity.dto.resp.AccountResp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<?> updateAccount(@RequestBody AccountResp accountResp) throws Exception{
        return responseSuccess(service.update(accountResp));
    }
    @PutMapping("/avatar")
    public ResponseEntity<?> updateAccount(@RequestParam("avatar")MultipartFile file) throws Exception{
        return responseSuccess(service.setAvatar(file));
    }
    @GetMapping ("/update-pass")
    public ResponseEntity<?> updatePass(@RequestParam String oldPassword,
                                        @RequestParam String password) throws Exception{
        return responseSuccess(service.updatePass(oldPassword,password));
    }
    @GetMapping("/info")
    public  ResponseEntity<?> getInfoByUser() {
        return responseSuccess(service.getInfoByUser());
    }


}
