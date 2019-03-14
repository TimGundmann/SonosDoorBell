package dk.gundmann.actions;

import org.springframework.stereotype.Component;

import com.google.actions.api.ActionRequest;
import com.google.actions.api.ActionResponse;
import com.google.actions.api.DialogflowApp;
import com.google.actions.api.ForIntent;
import com.google.actions.api.response.ResponseBuilder;

import dk.gundmann.sonos.Sonos;

@Component
public class SonosApp extends DialogflowApp {

	public static final String BELL_SOUND = "cifs://192.168.1.100/music/Trumpet.mp3";
	
	private Sonos sonos;

	public SonosApp(Sonos sonos) {
		this.sonos = sonos;
	}
	
	@ForIntent("spil-sonos")
	public ActionResponse welcome(ActionRequest request) {
		ResponseBuilder responseBuilder = getResponseBuilder(request);
	    
	    if ("klokke".equals(request.getParameter("sonos"))) {
	    	this.sonos.play(BELL_SOUND);
	    	responseBuilder.add("Spiller klokke");
	    } else {
	    	responseBuilder.add("Ikke implementeret endnu!");
	    }
	    
	    return responseBuilder.build();
	}

}
