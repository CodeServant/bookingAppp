package pl.macia.multiplex.core;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.google.gson.annotations.Expose;

/**
 * It is specific time and place where the {@link Movie} will be shown.
 * @author CodeServant
 *
 */
public class Screening {
	@Expose
	private LocalDateTime when;
	@Expose
	private int id;
	private static int nextId;
	private Movie movie;
	private Room room;
	private static Map<Integer, Screening> allScreenings = new HashMap<>();
	private static Set<Reservation> allReservations = new HashSet<>();
	private Set<Reservation> reservations = new HashSet<>();

	public Screening(LocalDateTime when, Movie movie, Room room) {
		if (when == null || movie == null || room == null)
			throw new NullPointerException();
		this.when = when;
		this.movie = movie;
		this.room = room;
		this.bumpId();
		this.room.addScreening(this);
		this.movie.addScreening(this);
		allScreenings.put(this.id, this);
	}

	/**
	 * 
	 * @throws Exception
	 */
	protected void addReservation(Reservation r) throws ReservationException {

		if (r == null)
			throw new NullPointerException();
		if (!allReservations.contains(r)) {
			reservations.add(r);
			allReservations.add(r);
		} else {
			throw new ReservationException("this reservation is in another screening");
		}
	}

	public void delScreening() {
		this.delMovie();
		this.delRoom();
		this.delReservation(this.reservations);
		allScreenings.remove(this.id);
	}

	private void delMovie() {
		if (this.getMovie() != null) {
			Movie tmp = this.movie;
			this.movie = null;
			tmp.delScreening(this);
		}
	}

	private void delRoom() {
		if (this.getRoom() != null) {
			Room tmp = this.room;
			this.room = null;
			tmp.delScreening(this);
		}
	}

	protected void delReservation(Reservation r) {
		if (reservations.contains(r)) {
			allReservations.remove(r);
			reservations.remove(r);
			r.delReservation();
		}
	}

	public static Set<Screening> getAllScreenings() {
		return new HashSet<>(allScreenings.values());
	}

	/**
	 * 
	 * @param start Start date inclusive
	 * @param end   date exclusive
	 * @return
	 */
	public static Set<Screening> getAllFromPeriod(LocalDateTime start, LocalDateTime end) {
		return allScreenings.values().stream().filter(s -> s.when.compareTo(end) < 0 && s.when.compareTo(start) >= 0)
				.collect(Collectors.toSet());
	}

	protected void delReservation(Collection<Reservation> r) {
		new CopyOnWriteArrayList<>(r).forEach(this::delReservation);
	}

	private void bumpId() {
		this.id = nextId;
		nextId++;
	}

	public int getId() {
		return this.id;
	}

	public static Screening getScreeningById(Integer id) {
		return allScreenings.get(id);
	}

	public Set<Seat> getSeats() {
		return this.getRoom().getSeats();
	}

	public Movie getMovie() {
		return this.movie;
	}

	public Room getRoom() {
		return this.room;
	}

	public LocalDateTime getDate() {
		return this.when;
	}
}
