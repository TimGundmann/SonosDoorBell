package dk.gundmann.general;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BellController {
    
    @RequestMapping("/ringbell")
    public String index() {
        System.out.println("Call to door bell at " + DateTimeFormatter.ofPattern("yyyy/MM/dd HHS:mm:ss z").format(ZonedDateTime.now()));    	
        return "Sons bell is ringing!";
    }
    
}
