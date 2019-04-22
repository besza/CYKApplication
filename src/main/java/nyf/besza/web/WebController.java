package nyf.besza.web;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class WebController {

    @GetMapping("/")
    public String doGet(CykForm cykForm) {
        return "index";
    }

    @PostMapping("/")
    public String doPost(@Valid CykForm cykForm, BindingResult bindingResult) {
        //TODO
        if (bindingResult.hasErrors()) {
        }
        return "index";
    }
}
