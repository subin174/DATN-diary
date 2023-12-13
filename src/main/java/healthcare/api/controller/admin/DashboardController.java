package healthcare.api.controller.admin;

import healthcare.api.controller.ApiController;
import healthcare.api.data.RequestParams;
import healthcare.api.service.AccountService;
import healthcare.api.service.DiaryService;
import healthcare.api.service.MoodSoundService;
import healthcare.api.service.SoundService;
import healthcare.entity.Account;
import healthcare.entity.Diary;
import healthcare.entity.Sound;
import healthcare.entity.dto.account.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class DashboardController extends ApiController {

    final MoodSoundService moodSoundService;
    final SoundService soundService;
    final AccountService accountService;
    final DiaryService diaryService;

    @GetMapping(value = "login")
    public String logIn(Model model) {
        return "page/login";
    }
    @GetMapping(value = "sound")
    public String address(Model model) {


        model.addAttribute("moodSound", moodSoundService.getList());
        return "page/sound";
    }
    @GetMapping(value = "list-sound")
    public String listSound(Model model) throws Exception {
        List<Sound> soundList = (List<Sound>) soundService.getListSoundApi();

//            model.addAttribute("sounds", soundService.getListSound(this.getParams()));
        model.addAttribute("soundList", soundList);
        return "page/list-sound";
    }
    @GetMapping(value = "user")
    public String User(Model model) throws Exception {
        List<Account> accounts = (List<Account>) accountService.getUser();
//            model.addAttribute("sounds", soundService.getListSound(this.getParams()));
        model.addAttribute("accountList", accounts);
        return "page/user-manager";
    }
    @GetMapping(value = "user-detail")
    public String userDetail(Model model,@RequestParam Long createdBy) {
        List<Diary> diaries = (List<Diary>) diaryService.getDiaryByCreatedBy(createdBy);
        model.addAttribute("diaries", diaries);
        return "page/user-detail";
    }
    @GetMapping("home")
    public String home(Model model){
        return "page/index";
    }
    @GetMapping(value = "profile")
    public String profile(Model model){
        return "page/pages-profile";
    }

    @GetMapping(value = "charts")
    public String charts(Model model){
        return "page/charts-chartjs";
    }

    @GetMapping(value = "web/{page}")
    public String web(Model model, @PathVariable String page){
        return "page/" + page;
    }


}
