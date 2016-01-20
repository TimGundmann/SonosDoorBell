package dk.gundmann.bell.sonos;

import org.tensin.sonos.commander.Sonos;
import org.tensin.sonos.control.ZonePlayer;
import org.tensin.sonos.gen.AVTransport.GetPositionInfoResponse;

public class PlaySound {

	private String bellSound;
	private Sonos sonos;

	public PlaySound(Sonos sonos, String bellSound) {
		this.sonos = sonos;
		this.bellSound = bellSound;
	}
	
	public void play(ZonePlayer player) {
		GetPositionInfoResponse currentSongInfo = sonos.currentSongPosition(player);
		playBellSound(player);
		waitForBellAndRemove(player, sonos.currentSongPosition(player));
		resume(player, currentSongInfo);
	}

	private void playBellSound(ZonePlayer player) {
		sonos.play(player, bellSound);
		sonos.play(player);
	}
	
	private void waitForBellAndRemove(ZonePlayer player, GetPositionInfoResponse positionInfo) {
		sleep(getDurationOfDoorBell(positionInfo.trackDuration()));
		try {sonos.pause(player);} catch (Exception e) {}
		sonos.remove(player, positionInfo.track());
	}

	private void resume(ZonePlayer player, GetPositionInfoResponse positionInfo) {
		sonos.play(player, positionInfo.track());
		sonos.forward(player, positionInfo.relTime());
		sonos.play(player);
	}

	private void sleep(long sleepSecond) {
		try {
			Thread.sleep(sleepSecond);
		} catch (InterruptedException e) {
			throw new SonosBellException("Error wating for bell sound", e);
		}
	}

	private long getDurationOfDoorBell(String timestampStr) {
		String[] tokens = timestampStr.split(":");
		int hours = Integer.parseInt(tokens[0]);
		int minutes = Integer.parseInt(tokens[1]);
		int seconds = Integer.parseInt(tokens[2]);
		return ((3600 * hours) + (60 * minutes) + seconds) * 1000 + 500;
	}

}
