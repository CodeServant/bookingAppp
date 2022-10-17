package pl.macia.multiplex.servlets;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import pl.macia.multiplex.core.*;

/**
 * Servlet implementation class ReservationController
 */
@WebServlet("/reservation")
public class ReservationController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReservationController() {
		super();
		ServerConfig.loadData();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			ServerConfig.preConfig(request, response);
			
			String name = request.getParameter("name");
			String surname = request.getParameter("surname");
			Person person = null;
			Screening screening = Screening.getScreeningById(Integer.parseInt(request.getParameter("screening")));
			person = new Person(name, surname);
			
			
			String[] rows = request.getParameterValues("r");
			String[] places = request.getParameterValues("p");
			String[] tickets = request.getParameterValues("t") != null ? request.getParameterValues("t") : new String[0];
			Set<Seat> seats = new HashSet<>();
			if (rows.length == places.length && tickets.length <= places.length)
				for (int i = 0; i < rows.length; i++)
					seats.add(screening.getRoom().getSeat(Integer.parseInt(rows[i]), Integer.parseInt(places[i])));
			
			
			Reservation reservation = new Reservation(person, screening, seats);
			reservation.addTicketType(tickets);
			Map<String, Object> resProperties = new HashMap<>();
			resProperties.put("totalPLN", reservation.receivables());
			resProperties.put("exiration", reservation.expirationTime());

			ServerConfig.sendObj(request, response, resProperties);

		} catch (ReservationException e) {
			response.sendError(406, e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
