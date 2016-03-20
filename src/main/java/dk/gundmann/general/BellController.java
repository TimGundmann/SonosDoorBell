package dk.gundmann.general;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dk.gundmann.bell.sonos.SonosBell;

@RestController
public class BellController {
    
	@Autowired
	private SonosBell sonosBell;

    @RequestMapping("/ringbell")
    public String ringbell() {
        System.out.println("Call to sonos bell at " + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss z").format(ZonedDateTime.now()));
        try {
        	sonosBell.ring();
        } catch (Exception e) {
        	return e.getMessage();
        }
        return "Sonos bell is rining!";
    }
    
}
