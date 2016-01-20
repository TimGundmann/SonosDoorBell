package dk.gundmann.bell.sonos;

public class SonosBellException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SonosBellException(Exception exception) {
		super(exception);
	}
	
	public SonosBellException(String message, Exception exception) {
		super(message, exception);
	}

	public SonosBellException(String message) {
		super(message);
	}
	
}
