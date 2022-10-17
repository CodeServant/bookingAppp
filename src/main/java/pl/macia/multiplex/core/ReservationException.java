package pl.macia.multiplex.core;

/**
 * Exception ocurs when there are certain issues that makes multiplex to refuse to create reservation.
 * @author CodeServant
 *
 */
public class ReservationException extends Exception {
	public ReservationException(String message) {
		super(message);
	}
}
