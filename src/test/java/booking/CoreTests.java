package booking;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.junit.jupiter.api.Test;

import pl.macia.multiplex.core.*;

class CoreTests {
	/**
	 * Testing connections between objects.
	 */
	@Test
	void testConnection() {
		try {
			Movie mov1 = new Movie("Pocahontas");
			Room room1 = Room.newRoom();

			Screening scr1 = new Screening(LocalDateTime.now().plus(15, ChronoUnit.MINUTES).plus(100, ChronoUnit.MILLIS),
					mov1, room1);
			assertEquals(0, scr1.getId());
			assertEquals(1, Movie.movieCount());
			assertEquals(1, Room.roomCount());
			assertEquals(1, Screening.getAllScreenings().size());
			assertEquals("Pocahontas", mov1.getTitle());
			assertEquals(1, room1.countPlaces());
			assertTrue(mov1.getScreenings().contains(scr1));
			assertSame(mov1, scr1.getMovie());
			Seat.newSeat(room1);
			Seat.newSeat(room1);
			assertTrue(Room.getRooms().contains(room1));
			assertEquals(3, room1.countPlaces());

			for (int i = 0; i < 8; i++)
				Seat.newSeat(room1);
			assertEquals(11, room1.countPlaces());
			assertFalse(room1.getSeat(0, 0).reserved(scr1));
			Person user1 = new Person("Grażyna", "Kowalska-Polak");
			TicketType type = TicketType.STUDENT;
			assertEquals(1, Person.getPersons().size());

			assertEquals("Grażyna", user1.getName());
			assertEquals("Kowalska-Polak", user1.getSurname());

			Set<Seat> pickedSeats = new HashSet<>();
			pickedSeats.add(scr1.getRoom().getSeat(0, 0));
			pickedSeats.add(scr1.getRoom().getSeat(0, 1));
			pickedSeats.add(scr1.getRoom().getSeat(0, 2));
			assertIterableEquals(room1.getSeats(), Seat.availbleSeats(scr1));
			assertTrue(Seat.notAvailbleSeats(scr1).isEmpty());
			Reservation res1 = new Reservation(user1, scr1, pickedSeats);
			Thread.sleep(100);
			assertTrue(res1.expired(), " reservation should be expired");
			assertEquals(8, Seat.availbleSeats(scr1).size());
			assertThrows(Exception.class, () -> res1.addTicketType(type, 4));
			assertAll(() -> res1.addTicketType(type, 2));
			assertEquals(2, res1.ticketsCount());
			assertTrue(res1.receivables().compareTo(new BigDecimal(61)) == 0, "receivables is " + res1.receivables());
			// object deletion
			mov1.delMovie();
			room1.delRoom();
			user1.delPerson();
			assertEquals(0, Movie.movieCount());
			assertEquals(0, Room.roomCount());
			assertEquals(0, Screening.getAllScreenings().size());
			assertEquals(0, room1.countPlaces());
			assertEquals(0, room1.countPlaces());
			assertFalse(mov1.getScreenings().contains(scr1));
			assertNull(scr1.getMovie());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Testing the gap between the seats. Can't have 1 seat gap.
	 */
	@Test
	void testSeatGap() {
		try {
			Movie mov1 = new Movie("Hp1");
			Room room1 = Room.newRoom();

			Screening scr1 = new Screening(LocalDateTime.now().plus(2, ChronoUnit.DAYS), mov1, room1);
			for (int i = 0; i < 11; i++)
				Seat.newSeat(room1);
			Person user1 = new Person("Grażyna", "Kowalska-Polak");
			TicketType type = TicketType.ADULT;

			Set<Seat> pickedSeats = new HashSet<>();
			pickedSeats.add(scr1.getRoom().getSeat(0, 0));
			pickedSeats.add(scr1.getRoom().getSeat(0, 1));
			pickedSeats.add(scr1.getRoom().getSeat(0, 3));

			assertThrows(Exception.class, () -> new Reservation(user1, scr1, pickedSeats));

			pickedSeats.remove(scr1.getRoom().getSeat(0, 3));
			new Reservation(user1, scr1, pickedSeats);
			pickedSeats.remove(scr1.getRoom().getSeat(0, 1));
			assertThrows(Exception.class, () -> new Reservation(user1, scr1, pickedSeats));

			pickedSeats.remove(scr1.getRoom().getSeat(0, 0));
			pickedSeats.add(scr1.getRoom().getSeat(0, 8));
			assertThrows(Exception.class, () -> new Reservation(user1, scr1, pickedSeats));
			mov1.delMovie();
			user1.delPerson();
			room1.delRoom();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Testing persons naming conventions.
	 */
	@Test
	void testPerson() {
		try {
			new Person("Jań", "Kowalskiń-Soboń");
			assertThrows(Exception.class, () -> new Person("Jń", "Kowalskiń-Polakń"));
			assertThrows(Exception.class, () -> new Person("jan", "Kowalskiń-Polakń"));
			assertThrows(Exception.class, () -> new Person("Jasdń", "Kowalskiń-polakń"));
			assertThrows(Exception.class, () -> new Person("Jan", "Kow-So"));
			assertThrows(Exception.class, () -> new Person("JahsS", "Kowalski"));
			assertThrows(Exception.class, () -> new Person("Jahs", "Kowalski2"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("exeption thrown");
		}

	}
}
