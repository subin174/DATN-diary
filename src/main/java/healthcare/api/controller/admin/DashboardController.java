package healthcare.api.controller.admin;

import healthcare.api.controller.ApiController;
import healthcare.api.data.RequestParams;
import healthcare.api.service.MoodSoundService;
import healthcare.api.service.SoundService;
import healthcare.entity.Sound;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class DashboardController extends ApiController {

    final MoodSoundService moodSoundService;
    final SoundService soundService;

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
    @GetMapping("home")
    public String home(Model model){
        return "page/index";
    }
    @GetMapping(value = "sign-up")
    public String signUp(Model model){
        return "page/pages-sign-up";
    }



    @GetMapping(value = "profile")
    public String profile(Model model){
        return "page/pages-profile";
    }

    @GetMapping(value = "forms")
    public String forms(Model model){
        return "page/ui-forms";
    }

    @GetMapping(value = "cards")
    public String cards(Model model){
        return "page/ui-cards";
    }

    @GetMapping(value = "typography")
    public String typography(Model model){
        return "page/ui-typography";
    }

    @GetMapping(value = "charts")
    public String charts(Model model){
        return "page/charts-chartjs";
    }
    @GetMapping(value = "icon-feather")
    public String icon(Model model){
        return "page/icon-feather";
    }

    @GetMapping(value = "web/{page}")
    public String web(Model model, @PathVariable String page){
        return "page/" + page;
    }


}
