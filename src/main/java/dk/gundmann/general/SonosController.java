package dk.gundmann.general;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.sukhsingh.actions.on.google.ApiAiApp;
import ca.sukhsingh.actions.on.google.request.Request;
import ca.sukhsingh.actions.on.google.response.Response;
import dk.gundmann.sonos.Sonos;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SonosController {
	
	private ApiAiApp app = new ApiAiApp();
    
	public static final String BELL_SOUND = "cifs://192.168.1.100/music/Trumpet.mp3";
	
	@Autowired
	private Sonos sonos;

    @PostMapping("/ringbell")
    public String ringbell() {
        log.info("Call to sonos bell at " + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss z").format(ZonedDateTime.now()));
        try {
        	sonos.play(BELL_SOUND);
        } catch (Exception e) {
        	return e.getMessage();
        }
        return "Sonos bell is ringing!";
    }

    @PostMapping("/play")
    public String play(@RequestBody String sound) {
    	log.info("Call to sonos play at " + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss z").format(ZonedDateTime.now()));
        try {
        	sonos.play(sound);
        } catch (Exception e) {
        	return e.getMessage();
        }
        return "Sonos is playing the sound!";
    }

    
    @PostMapping(value = "/webHook")
    public ResponseEntity<Response> tell(@RequestBody Request request) {
    	log.info(request.toString());
        String action = request.getAction();
        Response response = new Response();
        switch (action) {
            case ("input.ask") :
                response = app.ask("Hello World!");
                break;
            case ("input.ask.rich") :
                response = app.ask(
                        app.buildRichResponse()
                        .addSimpleResponse("Hello World!", "Hello World!")
                        .addSimpleResponse("Simple response with bubble")
                        .addSuggestions("Suggestion chip")
                        .addSuggestions("List", "Carousel")
                        .addSuggestionLink("Visit me", "http://example.com")
                );
                break;
        }
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
