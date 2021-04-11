package ar.com.juani.melishows.exception;

public class ShowingNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 9071180773280375788L;

	public ShowingNotFoundException(Long id) {
		super("Could not find showing " + id);
	}
}