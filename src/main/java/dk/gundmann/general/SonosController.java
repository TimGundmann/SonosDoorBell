package dk.gundmann.general;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dk.gundmann.actions.SonosApp;
import dk.gundmann.sonos.Sonos;

@RestController
public class SonosController {

	private static final Logger logger = LoggerFactory.getLogger(SonosController.class);

	public static final String BELL_SOUND = "cifs://192.168.1.100/music/Trumpet.mp3";

	private Sonos sonos;

	private SonosApp sonsoApp;

	public SonosController(Sonos sonos) {
		this.sonos = sonos;
		this.sonsoApp = new SonosApp(sonos);
	}

	@PostMapping("/ringbell")
	public String ringbell() {
		logger.info("Call to sonos bell at "
				+ DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss z").format(ZonedDateTime.now()));
		try {
			sonos.play(BELL_SOUND);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "Sonos bell is ringing!";
	}

	@PostMapping("/play")
	public String play(@RequestBody String sound) {
		logger.info("Call to sonos play at "
				+ DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss z").format(ZonedDateTime.now()));
		try {
			sonos.play(sound);
		} catch (Exception e) {
			return e.getMessage();
		}
		return "Sonos is playing the sound!";
	}

	@PostMapping(value = "/webHook")
	public String tell(@RequestBody String body, HttpServletRequest request) throws Exception {
		return sonsoApp.handleRequest(body, getHeadersMap(request)).get();
	}

	private Map<String, String> getHeadersMap(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();

		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String key = (String) headerNames.nextElement();
			String value = request.getHeader(key);
			map.put(key, value);
		}
		return map;
	}
}
