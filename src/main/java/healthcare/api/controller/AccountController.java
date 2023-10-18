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
    @DeleteMapping("/{id}")
    public  ResponseEntity<?> delete( @PathVariable Long id)throws Exception {
        service.delete(id);
        return responseSuccess();
    }
    @GetMapping("/search")
    public  ResponseEntity<?> findByPhone(@RequestParam(name = "phone") String phone) throws  Exception{

        return responseSuccess(service.findByPhone(phone));
    }

}
