package dk.gundmann.bell.sonos;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.tensin.sonos.commander.Sonos;

@RunWith(MockitoJUnitRunner.class)
public class LoaderTest {

	private static final int ONE_TIME = 1;

	@Mock
	private Sonos sonos;
	
	private Loader loader;
	
	@Before
	public void setUpFixture() {
		loader = new Loader(sonos, ONE_TIME);
	}
	
	@Test
	public void verifyThatAListOfPlayersIsResolved() {
		// given
		when(sonos.getZoneNames()).thenReturn(Collections.singletonList("Test"));
		
		// when then
		assertThat("Ther should be returnd a list of sonar names", loader.getPlayers().size(), greaterThan(0));
	}

	@Test(expected = SonosBellException.class)
	public void givenNoSonosPlayersWillFaile() throws Exception {
		// given
		when(sonos.getZoneNames()).thenReturn(Collections.emptyList());

		// when then
		loader.getPlayers();
	}
	
}
