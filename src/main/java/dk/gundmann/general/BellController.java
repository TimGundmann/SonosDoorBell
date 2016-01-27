package dk.gundmann.general;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class BellController {
    
    @RequestMapping("/ringbell")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
}
