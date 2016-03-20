package dk.gundmann.bell.sonos;

import org.tensin.sonos.commander.Sonos;
import org.tensin.sonos.control.ZonePlayer;

public class VolumAdjuster {

	private Sonos sonos;
	private ZonePlayer player;

	public VolumAdjuster(Sonos sonos, ZonePlayer player) {
		this.sonos = sonos;
		this.player = player;
	}

	public void adjustToBellLevel() {
		adjust(sonos.volume(player) - 20);
	}

	public void resume() {
		adjust(20 - sonos.volume(player));
	}

	private void adjust(int volume) {
		try {
			sonos.adjustVolume(player, volume);
		} catch (Exception e1) {
			try {
				sonos.adjustVolume(player, volume);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

}
