package pl.macia.multiplex.servlets;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import javax.servlet.http.*;

import com.google.gson.*;

import pl.macia.multiplex.core.*;

public class ServerConfig {
	private static boolean valGenerated = false;
	public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param obj
	 * @throws IOException
	 */
	public static void sendObj(HttpServletRequest request, HttpServletResponse response, Object obj)
			throws IOException {
		Gson gson = createGson();
		String encoding = "UTF-8";
		response.setContentType("application/json");
		response.setCharacterEncoding(encoding);
		response.getOutputStream().write(gson.toJson(obj).getBytes(encoding));
		response.getOutputStream().flush();
	}

	public static Gson createGson() {
		GsonBuilder gb = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		gb.registerTypeAdapter(LocalDateTime.class, new JsonDateSerialize());
		gb.registerTypeAdapter(LocalDateTime.class, new JsonDateDeserialize());
		gb.enableComplexMapKeySerialization();

		return gb.create();
	}
	
	/**
	 * Instantiate data. Movies, rooms and screening, even reservation.
	 */
	public static void loadData() {
		if (!valGenerated) {
			try {
				Movie mov1 = new Movie("Dzień świra");
				Movie mov2 = new Movie("Harry Potter i więzień azkabanu");
				Movie mov3 = new Movie("Apokawixa");
				Room room1 = Room.newRoom();
				Room room2 = Room.newRoom();
				Room room3 = Room.newRoom();
				for (int i = 0; i < 99; i++) {
					Seat.newSeat(room1);
					Seat.newSeat(room2);
					Seat.newSeat(room3);
				}
				LocalDateTime firstDate = LocalDateTime.now().plus(15, ChronoUnit.MINUTES);
				LocalDateTime secoundDate = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
				
				Screening scr1 = new Screening(firstDate, mov1, room1); //id 0
				Screening scr2 = new Screening(secoundDate, mov1, room1); //id 1
				Screening scr3 = new Screening(firstDate, mov2, room2); //id 2
				Screening scr4 = new Screening(secoundDate, mov2, room2); //id 3
				Screening scr5 = new Screening(firstDate, mov3, room3); //id 4
				Screening scr6 = new Screening(secoundDate, mov3, room3); //id 5
				
				Person person1 = new Person("Jan", "Kowalski");
				
				Set<Seat> seats = new HashSet<>();
				seats.add(scr6.getRoom().getSeat(0, 0));
				seats.add(scr6.getRoom().getSeat(0, 1));
				seats.add(scr6.getRoom().getSeat(0, 2));
				new Reservation(person1, scr6, seats); // making reserrvation for Apokawixa 2020-10-11 13:00 
				new Screening(LocalDateTime.of(2020, 10, 10, 15, 30), mov2, room1);
				valGenerated = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void preConfig(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
