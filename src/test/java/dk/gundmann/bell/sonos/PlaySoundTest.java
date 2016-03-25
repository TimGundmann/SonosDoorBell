package dk.gundmann.bell.sonos;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
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

	private static final int TRACK_NUMBER = 2;
	
	@Mock
	private Sonos sonos;
	
	@Mock
	private GetPositionInfoResponse currentSongInfo;
	
	private ZonePlayer player = mock(ZonePlayer.class);

	@Mock
	private PlaySound playSound;

	@Before
	public void setUpFixture() {
		playSound = new PlaySound(sonos, SOUND);
		when(sonos.currentSongPosition(any(ZonePlayer.class))).thenReturn(currentSongInfo);
		when(currentSongInfo.trackDuration()).thenReturn("00:00:01");
	}
	
	@Test
	public void givenAPlayerWillPauseTheCurrentSong() throws Exception {
		// given when
		playSound.play(player);

		// then
		verify(sonos, atLeast(1)).currentSongPosition(player);
	}
	
	@Test
	public void verifyThatTheBellSoundIsPlayed() throws Exception {
		// given when
		playSound.play(player);

		// then
		verify(sonos).play(eq(player), eq(SOUND));
		verify(sonos, atLeast(1)).play(eq(player));
	}
	
	@Test
	public void verifyThatTheBellTrackIsRemoved() throws Exception {
		// given when
		playSound.play(player);

		// then
		verify(sonos).remove(eq(player), anyInt());		
	}
	
	@Test
	public void verifyThatTheCurrentSongPlayingIsRestarted() throws Exception {
		// given 
		when(currentSongInfo.track()).thenReturn(TRACK_NUMBER);
		
		// when
		playSound.play(player);

		// then
		verify(sonos).play(eq(player), eq(TRACK_NUMBER));
		verify(sonos).forward(eq(player), anyString());
		verify(sonos, atLeast(1)).play(eq(player));
	}
}
