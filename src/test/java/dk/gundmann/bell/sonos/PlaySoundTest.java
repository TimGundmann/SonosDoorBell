package dk.gundmann.bell.sonos;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.tensin.sonos.commander.Sonos;
import org.tensin.sonos.control.ZonePlayer;
import org.tensin.sonos.gen.AVTransport.GetPositionInfoResponse;
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class PlaySoundTest {

	private final static String SOUND = "SOUND";
	
	@Mock
	private Sonos sonos;
	
	@Mock
	private GetPositionInfoResponse currentSongInfo;
	
	private PlaySound playSound;

	@Before
	public void setUpFixture() {
		playSound = new PlaySound(sonos, SOUND);
		when(sonos.currentSongPosition(any(ZonePlayer.class))).thenReturn(currentSongInfo);
		when(currentSongInfo.trackDuration()).thenReturn("00:00:01");
	}
	
	@Test
	public void givenAPlayerWillPauseTheCurrentSong() throws Exception {
		// given
		ZonePlayer player = mock(ZonePlayer.class);

		// when
		playSound.play(player);

		// then
		verify(sonos).currentSongPosition(player);
	}
	
	@Test
	public void verifyThatTheBellSoundIsPlayed() throws Exception {
		// given
		ZonePlayer player = mock(ZonePlayer.class);

		// when
		playSound.play(player);

		// then
		verify(sonos).play(eq(player), eq(SOUND));
		verify(sonos).play(eq(player));
	}
	
}
