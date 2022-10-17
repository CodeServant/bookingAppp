package pl.macia.multiplex.core;

import java.util.*;
import java.util.stream.Collectors;

import com.google.gson.annotations.Expose;


public class Seat {
	@Expose
	private int row = 0;
	@Expose
	private int place = 0;
	private Room room;
	private Set<Reservation> reservations = new HashSet<>();

	private Seat() {
		super();
	}

	protected Seat(int row, int place) {
		this.row = row;
		this.place = place;
	}

	public static Seat newSeat(Room room) throws Exception {
		if (room == null)
			throw new NullPointerException("parameter is null");
		Seat ns = new Seat();
		ns.setRoom(room);
		room.addSeat(ns);
		final int row = room.getRows() - 1;
		ns.setRow(row);
		ns.setPlace(room.getPlace(row) - 1);
		return ns;
	}

	private void setRoom(Room room) {
		this.room = room;
	}

	public int setRow(int row) {
		return this.row = row;
	}

	public int getRow() {
		return row;
	}

	public int setPlace(int place) {
		return this.place = place;
	}

	public int getPlace() {
		return place;
	}

	/**
	 * Checks if the steat is reserved for the specific screening.
	 * 
	 * @param screening
	 * @return
	 */
	public boolean reserved(Screening screening) {
		return reservations.stream().anyMatch(r -> r.getScreening().equals(screening));
	}

	public static Set<Seat> availbleSeats(Screening screening) {
		return screening.getSeats().stream().filter(v -> !v.reserved(screening)).collect(Collectors.toSet());
	}

	public static Set<Seat> notAvailbleSeats(Screening screening) {
		return screening.getSeats().stream().filter(v -> v.reserved(screening)).collect(Collectors.toSet());
	}

	protected void addReservation(Reservation reservation) throws ReservationException {
		if (reservation == null)
			throw new NullPointerException();
		if (this.reserved(reservation.getScreening()))
			throw new ReservationException("already reserved");
		reservations.add(reservation);
	}

	public String toString() {
		return "(r" + this.row + ", p" + this.place + ")";
	}

	public void delReservation(Reservation reservation) {
		if (this.reservations.contains(reservation)) {
			this.reservations.remove(reservation);
			reservation.delReservation();
		}
	}

	private void delReservations() {
		reservations.forEach(this::delReservation);
	}

	public Room getRoom() {
		return room;
	}

	protected void delSeat() {
		if (this.room != null) {
			delReservations();
			Room tmp = this.room;
			this.room = null;
		}
	}
}
