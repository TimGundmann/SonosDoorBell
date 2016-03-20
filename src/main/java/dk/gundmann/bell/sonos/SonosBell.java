package dk.gundmann.bell.sonos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tensin.sonos.control.ZonePlayer;

@Component
public class SonosBell {
	
	@Autowired
	private Loader loader;
	
	@Autowired
	private PlaySound playSound;

	public void ring() {
		for (ZonePlayer player : loader.getPlayers()) {
			new Thread(new ThreadPlayer(player)).start();
		}
	}
	
	public class ThreadPlayer implements Runnable {

		private ZonePlayer player;

		public ThreadPlayer(ZonePlayer player) {
			this.player = player;
		}
		
		@Override
		public void run() {
			playSound.play(player);
		}
		
	}
	
}
