package pl.macia.multiplex.core;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Reservation {
	private Map<TicketType, Integer> quantityByTicketType = new HashMap<>();

	private Set<Seat> seats = new HashSet<>();
	private Person person;
	private Screening screening;
	private static Duration expirationDuration = Duration.ofMinutes(15);

	public Reservation(Person person, Screening screening, Set<Seat> seats) throws ReservationException {

		setPerson(person);
		setScreening(screening);
		if (expired()) {
			this.delReservation();
			throw new ReservationException("can't book becouse expired");
		}
		addSeats(seats);
	}

	public void addTicketType(TicketType type, Integer quantity) throws ReservationException {
		if ((quantity + this.ticketsCount()) > this.seatCount()) {
			throw new ReservationException("to many types of tickets for the seat amount");
		}
		quantityByTicketType.merge(type, quantity, (v1, v2) -> v1 + v2);
	}

	public void addTicketType(String[] shortNames) throws ReservationException {
		for (String shortName : shortNames) {
			addTicketType(TicketType.ofShortName(shortName), 1);
		}
	}

	/**
	 * How much the client have to pay.
	 * @return
	 */
	public BigDecimal receivables() {
		BigDecimal reciv = new BigDecimal(0);
		for (Map.Entry<TicketType, Integer> value : quantityByTicketType.entrySet()) {

			reciv = reciv.add(value.getKey().getPrice().multiply(new BigDecimal(value.getValue())));
		}

		int fulfill = this.seatCount() - this.ticketsCount();
		// if there are less tickets choosen then seats default ticket type is ADULT
		reciv = reciv.add(TicketType.ADULT.getPrice().multiply(new BigDecimal(fulfill)));
		return reciv;
	}

	public LocalDateTime expirationTime() {
		return screening.getDate().minus(expirationDuration);
	}

	public boolean expired() {
		return expirationTime().isBefore(LocalDateTime.now());
	}

	private void setPerson(Person person) {
		if (person == null)
			throw new NullPointerException();
		this.person = person;
		person.addReservation(this);
	}

	public void addSeats(Set<Seat> seats) throws ReservationException {

		if (seats == null)
			throw new NullPointerException();
		if (seats.size() < 1)
			throw new ReservationException("reservation have to have at least 1 seat");

		if (seats.stream().anyMatch(s -> s.getRoom() != this.getRoom()))
			throw new ReservationException("booking seat is from the other room");
		// checking if reservation meets multiplex policy
		Set<Seat> afterReservationSeats = Seat.notAvailbleSeats(this.getScreening());
		afterReservationSeats.addAll(seats);
		if (!Room.seatsDisplacementCorrect(afterReservationSeats))
			throw new ReservationException("reservation doesn't meet multiplex policy");

		for (Seat st : seats)
			st.addReservation(this);
		this.seats.addAll(seats);
	}

	public Screening getScreening() {
		return screening;
	}

	/**
	 * Ticket coutnt is how many ticket was defined by the user.
	 * 
	 * @return
	 */
	public int ticketsCount() {
		return quantityByTicketType.values().stream().reduce(0, Integer::sum);
	}

	protected int seatCount() {
		return seats.size();
	}

	private void setScreening(Screening screening) throws ReservationException {
		if (screening == null)
			throw new NullPointerException();
		this.screening = screening;
		screening.addReservation(this);
	}

	public Room getRoom() {
		return screening.getRoom();
	}

	public void delReservation() {
		delSeat(this.seats);
		delPerson();
		delScreening();
	}

	private void delScreening() {
		if (this.screening != null) {
			Screening tmp = this.screening;
			this.screening = null;
			tmp.delReservation(this);
		}
	}

	private void delPerson() {
		if (this.person != null) {
			Person tmp = this.person;
			this.person = null;
			tmp.delReservation(this);
		}
	}

	private void delSeat(Seat s) {
		if (seats.contains(s)) {
			seats.remove(s);
			s.delReservation(this);
		}
	}

	private void delSeat(Collection<Seat> ss) {
		ss = new CopyOnWriteArrayList<Seat>(ss);
		ss.forEach(this::delSeat);
	}
}
