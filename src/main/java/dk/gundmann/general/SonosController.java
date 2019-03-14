package dk.gundmann.general;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
public class SonosController {
	
	private static final Logger logger = LoggerFactory.getLogger(SonosController.class);
	
	private ApiAiApp app = new ApiAiApp();
    
	@Autowired
	private HttpServletRequest context;
	
	public static final String BELL_SOUND = "cifs://192.168.1.100/music/Trumpet.mp3";
	
	@Autowired
	private Sonos sonos;

    @PostMapping("/ringbell")
    public String ringbell() {
    	logger.info("Call to sonos bell at " + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss z").format(ZonedDateTime.now()));
        try {
        	sonos.play(BELL_SOUND);
        } catch (Exception e) {
        	return e.getMessage();
        }
        return "Sonos bell is ringing!";
    }

    @PostMapping("/play")
    public String play(@RequestBody String sound) {
    	logger.info("Call to sonos play at " + DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss z").format(ZonedDateTime.now()));
        try {
        	sonos.play(sound);
        } catch (Exception e) {
        	return e.getMessage();
        }
        return "Sonos is playing the sound!";
    }

    
    @PostMapping(value = "/webHook")
    public ResponseEntity<Response> tell(@RequestBody Request request) throws IOException {
    	logger.info(context.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
    	logger.info(request.getIntent());
        String action = request.getIntent();
        Response response = new Response();
        switch (action) {
            case ("actions.intent.MAIN") :
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
