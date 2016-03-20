package dk.gundmann.general;

import javax.annotation.PreDestroy;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.registry.RegistryListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.tensin.sonos.commander.Sonos;

import dk.gundmann.bell.sonos.Loader;
import dk.gundmann.bell.sonos.PlaySound;
import dk.gundmann.bell.sonos.SonosBell;

@Configuration
@EnableScheduling
public class ApplicationConfiguration {

	public static final String BELL_SOUND = "cifs://WEBSERVER/music/doorbell/Trumpet.mp3";

	private Sonos sonos = new Sonos(20000) {
		@Override
		protected UpnpService createUpnpService(RegistryListener listener) {
			return new UpnpServiceImpl(new BellUpnpServiceConfiguration(), listener);
		};
	};

	@Bean
	public Loader useLoadere() {
		return new Loader(sonos);
	}
	
	@Bean
	public PlaySound usePlaySounde() {
		return new PlaySound(sonos, BELL_SOUND);
	}
	
	@Bean 
	public SonosBell useSonosBell() {
		return new SonosBell();
	}
	
	@PreDestroy
	public void destroy() {
		sonos.close();
	}
	

}
