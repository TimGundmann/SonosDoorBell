package dk.gundmann.sonos;

import org.springframework.stereotype.Component;
import org.tensin.sonos.SonosException;
import org.tensin.sonos.commander.Sonos;
import org.tensin.sonos.control.ZonePlayer;
import org.tensin.sonos.gen.AVTransport.GetPositionInfoResponse;

@Component
public class PlaySound {

	private static final GetPositionInfoResponse NOT_PLAYING = null;
	
	private Sonos sonos;

	public PlaySound(Sonos sonos) {
		this.sonos = sonos;
	}
	
	public void play(ZonePlayer player, String bellSound) {
		GetPositionInfoResponse currentSongInfo = getCurrentPosition(player);
		VolumAdjuster volumAdjuster = new VolumAdjuster(sonos, player);
		GetPositionInfoResponse bellSongInfo = playBellSound(player, volumAdjuster, bellSound);
		waitForBellAndRemove(player, bellSongInfo, volumAdjuster);
		resume(player, currentSongInfo);
		
	}

	private GetPositionInfoResponse getCurrentPosition(ZonePlayer player) {
		if (sonos.isPlaying(player)) {
			return sonos.currentSongPosition(player);
		}
		return NOT_PLAYING;
	}

	private GetPositionInfoResponse playBellSound(ZonePlayer player, VolumAdjuster volumAdjuster, String bellSound) {
		GetPositionInfoResponse position = NOT_PLAYING;
		try {
			int track = sonos.enqueue(player, bellSound).firstTrackNumberEnqueued();
			volumAdjuster.adjustToBellLevel();
			sonos.track(player, track);
			position = sonos.currentSongPosition(player);
			sonos.play(player);
			System.out.println("Playing " + bellSound + " on " + player.toString());
		} catch (SonosException e) {
			e.printStackTrace();
		}
		return position;
	}
	
	private void waitForBellAndRemove(ZonePlayer player, GetPositionInfoResponse positionInfo, VolumAdjuster volumAdjuster) {
		if (positionInfo != null) {
			sleep(getDurationOfDoorBell(positionInfo.trackDuration()));
			try {sonos.pause(player);} catch (Exception e) {}
			sonos.remove(player, positionInfo.track());
			volumAdjuster.resume();
			System.out.println("Removing bell song from " + player.toString());
		}
	}

	private void resume(ZonePlayer player, GetPositionInfoResponse positionInfo) {
		if (NOT_PLAYING != positionInfo && positionInfo.track() > 0) {
			sonos.play(player, positionInfo.track());
			sonos.forward(player, positionInfo.relTime());
			sonos.play(player);
		}
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

	public void browsAndPlay(ZonePlayer player, BrowsSongParameters parameters) {
		this.sonos.browse(player, "EN:", "");
	}

}
