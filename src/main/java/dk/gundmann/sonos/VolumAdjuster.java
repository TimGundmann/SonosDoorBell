package dk.gundmann.sonos;

import org.tensin.sonos.SonosException;
import org.tensin.sonos.commander.Sonos;
import org.tensin.sonos.control.ZonePlayer;

public class VolumAdjuster {

	private Sonos sonos;
	private ZonePlayer player;
	private int volume;

	public VolumAdjuster(Sonos sonos, ZonePlayer player) {
		this.sonos = sonos;
		this.player = player;
	}

	public void adjustToBellLevel() {
		saveCurrentvolume();
		adjust(30);
	}

	public void resume() {
		adjust(volume);
	}

	private void saveCurrentvolume() {
		volume = sonos.volume(player);
	}

	private void adjust(int volume) {
		try {
			sonos.setVolume(player, volume);
		} catch (Exception e1) {
			try {
				sonos.setVolume(player, volume);
			} catch (SonosException e2) {
				if (!"501".equals(e2.getMessage())) {
					e2.printStackTrace();
				}
			}
		}
	}

}
