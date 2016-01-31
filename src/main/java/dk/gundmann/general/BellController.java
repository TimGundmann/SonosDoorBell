package dk.gundmann.general;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BellController {
    
    @RequestMapping("/ringbell")
    public String index() {
        System.out.println("Call to sonos bell at " + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss z").format(ZonedDateTime.now()));    	
        return "Sonos bell is rining!";
    }
    
}
