package dk.gundmann.integration;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.tensin.sonos.commander.Sonos;
import org.tensin.sonos.control.ZonePlayer;

import dk.gundmann.bell.sonos.Loader;
import dk.gundmann.bell.sonos.PlaySound;
import dk.gundmann.general.ApplicationConfiguration;

@Ignore
public class SonosBellIntegrationTest {

	private Sonos sonos = new Sonos(10000);
	
	private Loader loader = new Loader(sonos);
	
	private PlaySound playSound = new PlaySound(sonos, ApplicationConfiguration.BELL_SOUND);
	
	private ZonePlayer player;
	
	@Before
	public void setUpSonosPlayer() {
		loader.getPlayers();
		player = sonos.getPlayer("Køkken");
//		player = loader.resolvePlayer("Køkken");
	}
	
	@After
	public void teardown() {
		sonos.close();
	}
	
	@Test
	public void givenAPlayerWillPlayASong() throws Exception {
		// given

		// when
		Collection<ZonePlayer> players = loader.getPlayers();

		// then
		for (ZonePlayer player : players) {
			System.out.println(player);
		}
		
	}
	
}
