package eng.banking.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salam")
public class HelloController {

    @PostMapping("/hello")
    public String getName(){
        return "Name";
    }
}
