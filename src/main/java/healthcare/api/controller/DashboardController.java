package healthcare.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class DashboardController {
    @GetMapping(value = "login")
    public String logIn(Model model) {
        return "page/login";
    }
    @GetMapping(value = "address")
    public String address(Model model) {
        return "page/address";
    }
    @GetMapping("home")
    public String home(Model model){
        return "page/index";
    }
    @GetMapping(value = "sign-up")
    public String signUp(Model model){
        return "page/pages-sign-up";
    }

    @GetMapping(value = "maps")
    public String maps(Model model){
        return "page/maps-google";
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

    @GetMapping(value = "web/{page}")
    public String web(Model model, @PathVariable String page){
        return "page/" + page;
    }

}
