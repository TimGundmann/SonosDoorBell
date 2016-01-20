package dk.gundmann.bell.sonos;

import java.util.Collection;
import java.util.List;

import org.tensin.sonos.commander.Sonos;

public class Loader {

	public static final int WAIT_SECONDS = 50;
	
	private Sonos sonos;

	private int waitForSecondsToLoad;

	public Loader(Sonos sonos, int waitForSecondsToLoad) {
		this.sonos = sonos;
		this.waitForSecondsToLoad = waitForSecondsToLoad;
	}

	public Loader(Sonos sonos) {
		this(sonos, WAIT_SECONDS);
	}
	
	public Collection<String> getPlayers() {
		try {
			return resolveAndWaitForPlaysers();
		} catch (InterruptedException e) {
			throw new SonosBellException("Error loading players", e);
		}
	}

	private List<String> resolveAndWaitForPlaysers() throws InterruptedException {
		List<String> zoneNames = sonos.getZoneNames();
		zoneNames = loadAndWaite(zoneNames);
		if (noPlayers(zoneNames)) {
			throw new SonosBellException("Error loading players, there was no players available");
		}
		return zoneNames; 
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
