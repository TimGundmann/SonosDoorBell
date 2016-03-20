package dk.gundmann.bell.sonos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tensin.sonos.commander.Sonos;
import org.tensin.sonos.control.ZonePlayer;

@Component
public class Loader {

	public static final int WAIT_SECONDS = 7200;

	private static final long ONE_HOUR = 3600000;
	
	private Sonos sonos;

	private int waitForSecondsToLoad;
	
	private Collection<ZonePlayer> players;

	public Loader(Sonos sonos, int waitForSecondsToLoad) {
		this.sonos = sonos;
		this.waitForSecondsToLoad = waitForSecondsToLoad;
	}

	public Loader(Sonos sonos) {
		this(sonos, WAIT_SECONDS);
	}
	
	@Scheduled(fixedDelay=ONE_HOUR)
	public void refreshPlayers() {
		players = null;
		getPlayers();
	}
	
	public synchronized Collection<ZonePlayer> getPlayers() {
		if (players == null) {
			initPlayers();
		}
		return players;
	}	
	
	private void initPlayers() {
		try {
			players = resolveAndWaitForPlaysers();
		} catch (InterruptedException e) {
			throw new SonosBellException("Error loading players", e);
		}
	}

	private List<ZonePlayer> resolveAndWaitForPlaysers() throws InterruptedException {
		List<String> zoneNames = sonos.getZoneNames();
		zoneNames = loadAndWaite(zoneNames);
		if (noPlayers(zoneNames)) {
			throw new SonosBellException("Error loading players, there was no players available");
		}
		return resolvePlayers(zoneNames); 
	}

	public List<ZonePlayer> resolvePlayers(List<String> zoneNames) {
		List<ZonePlayer> result = new ArrayList<>();
		for (String playerName : zoneNames) {
			result.add(sonos.getPlayer(playerName));
		}
		return result;
	}

	private List<String> loadAndWaite(List<String> zoneNames) throws InterruptedException {
		int tires = 0;
		while (noPlayers(zoneNames) && tires < waitForSecondsToLoad) {
			Thread.sleep(1000);
			zoneNames = sonos.getZoneNames();
			tires++;
		}
		return zoneNames;
	}

	private boolean noPlayers(List<String> zoneNames) {
		return zoneNames.size() == 0;
	}
	
}
