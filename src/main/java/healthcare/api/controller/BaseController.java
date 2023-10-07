package healthcare.api.controller;
import healthcare.api.service.BaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ResponseBody
@ResponseStatus(code = HttpStatus.OK)
@Slf4j
public abstract class BaseController<T,TService extends BaseService<T>> extends ApiController {
    protected final TService service;
}
