package dk.gundmann.sonos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tensin.sonos.control.ZonePlayer;

@Component
public class Sonos {
	
	@Autowired
	private Loader loader;
	
	@Autowired
	private PlaySound playSound;

	public void play(String sound) {
		for (ZonePlayer player : loader.getPlayers()) {
			new Thread(() -> playSound.play(player, sound)).start();
		}
	}
	
	public void browsAndPlay(BrowsSongParameters parameters) {
		for (ZonePlayer player : loader.getPlayers()) {
			new Thread(() -> playSound.browsAndPlay(player, parameters)).start();
		}
	}
	
	public class ThreadPlayer implements Runnable {

		private ZonePlayer player;
		private String sound;

		public ThreadPlayer(ZonePlayer player, String sound) {
			this.player = player;
			this.sound = sound;
		}
		
		@Override
		public void run() {
			playSound.play(player, sound);
		}
		
	}
	
}
