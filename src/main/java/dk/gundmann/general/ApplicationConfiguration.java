package dk.gundmann.general;

import javax.annotation.PreDestroy;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.registry.RegistryListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.tensin.sonos.commander.Sonos;

import dk.gundmann.sonos.Loader;
import dk.gundmann.sonos.PlaySound;

@Configuration
@EnableScheduling
public class ApplicationConfiguration {

	private Sonos sonos = new Sonos(20000) {
		@Override
		protected UpnpService createUpnpService(RegistryListener listener) {
			return new UpnpServiceImpl(new SonosUpnpServiceConfiguration(), listener);
		};
	};

	@Bean
	public Loader useLoadere() {
		return new Loader(sonos);
	}
	
	@Bean
	public PlaySound usePlaySounde() {
		return new PlaySound(sonos);
	}
	
	@Bean 
	public dk.gundmann.sonos.Sonos useSonosBell() {
		return new dk.gundmann.sonos.Sonos();
	}
	
	@PreDestroy
	public void destroy() {
		sonos.close();
	}
	

}
