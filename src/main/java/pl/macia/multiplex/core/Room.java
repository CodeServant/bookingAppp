package pl.macia.multiplex.core;

import java.util.*;

/**
 * Room represents physical place to watch {@link Movie} via the {@link Screening}
 * @author CodeServant
 *
 */
public class Room {
	/**
	 * Numbers are from 1 to many
	 */
	private int number;
	private static Set<Room> rooms = new TreeSet<>((a, b) -> a.getNumber() - b.getNumber());
	private Set<Screening> screenings = new HashSet<>();
	private static int maxRows = 10, maxPlaces = 10;
	private Map<Integer, Map<Integer, Seat>> seats = new TreeMap();

	private Room() {
		super();
	}

	private Room(int number) {
		this.number = number;
	}

	public static Room newRoom() {
		Room nr = new Room(rooms.size() + 1);
		rooms.add(nr);
		try {
			Seat.newSeat(nr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nr;
	}

	/**
	 * Adds seats considering default layout grid maxRows x maxPlaces.
	 * 
	 * @param seat
	 * @throws Exception
	 */
	protected void addSeat(Seat seat) throws Exception {

		final int curPositionPlaceSize;
		if (this.seats.size() != 0 && (curPositionPlaceSize = seats.get(this.getRows() - 1).size()) < this.maxPlaces) {
			this.seats.get(this.getRows() - 1).put(curPositionPlaceSize, seat);
		} else if (this.getRows() < this.maxRows) {
			Map m = new TreeMap<Integer, Seat>();
			m.put(0, seat);
			this.seats.put(this.getRows(), m);
		} else {
			throw new Exception("no room for new seat");
		}

	}

	protected void addScreening(Screening sc) {
		screenings.add(sc);
	}

	public void delScreening(Screening screening) {
		if (this.screenings.contains(screening)) {
			this.screenings.remove(screening);
			screening.delScreening();
		}
	}

	public void delRoom() {
		if (rooms.contains(this)) {
			rooms.remove(this);
			getSeats().forEach(t -> t.delSeat());
			seats = null;
		}
	}

	/**
	 * Gets the rows quantity.
	 * 
	 * @return
	 */
	public int getRows() {
		return this.seats.size();
	}

	/**
	 * Gets the quantity of places in the given row.
	 * 
	 * @return
	 */
	public int getPlace(int rowNum) {
		return this.seats.get(rowNum).size();
	}

	public int getNumber() {
		return this.number;
	}

	public static int roomCount() {
		return Room.rooms.size();
	}

	public int countPlaces() {
		int i = 0;
		if (this.seats != null)
			for (Map<Integer, Seat> row : this.seats.values())
				i += row.size();
		return i;

	}

	/**
	 * Checks if seats are properly picked. It they meet multiplex policy.
	 */
	protected static boolean seatsDisplacementCorrect(Collection<Seat> seats) {
		// wyciągnąć mapę row i integerów
		Map<Integer, List<Integer>> rowMap = new TreeMap<>();
		// building map of rows
		for (Seat s : seats) {
			int row = s.getRow();
			int place = s.getPlace();
			List<Integer> rowList = rowMap.getOrDefault(row, new ArrayList<>());
			rowList.add(place);
			rowMap.put(row, rowList);
		}
		// checking each row if it is correctly displaced
		for (List<Integer> rowList : rowMap.values()) {
			if (!rowDisplacementCorrect(rowList))
				return false;
		}
		return true;
	}

	/**
	 * 
	 * 
	 * @param row list with numbers of seats taken in one row, don't have to be
	 *            sorted
	 * @return
	 */
	private static boolean rowDisplacementCorrect(List<Integer> row) {
		Collections.sort(row);
		// chacking the corners
		if (row.get(0) == 1 || maxPlaces - row.get(row.size() - 1) - 1 == 1)
			return false;
		// checking for the in row gap
		for (int i = 1; i < row.size(); i++) {
			if ((row.get(i) - row.get(i - 1)) == 2)
				return false;
		}
		return true;
	}

	public Set<Seat> getSeats() {
		Set<Seat> returned = new HashSet<>();
		this.seats.forEach((k, v) -> {
			v.forEach((k2, v2) -> returned.add(v2));
		});
		return returned;
	}

	public static Set<Room> getRooms() {
		Set<Room> rRooms = new TreeSet<Room>((a, b) -> a.getNumber() - b.getNumber());
		rRooms.addAll(rooms);
		return rRooms;
	}
	@Override
	public String toString() {
		return "num: "+getNumber()+", seats: "+countPlaces();
	}
	public Seat getSeat(int row, int place) {
		return seats.get(row).get(place);
	}
}
