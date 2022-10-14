package pl.macia.multiplex.servlets;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import pl.macia.multiplex.core.*;

/**
 * Servlet implementation class ScreeningController
 */
@WebServlet("/screenings")
public class ScreeningController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ScreeningController() {
		super();
		ServerConfig.loadData();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServerConfig.preConfig(request, response);
		Screening sc = Screening.getScreeningById(Integer.parseInt(request.getParameter("id")));

		
		Map<String, Set<Seat>> seatsGroups = new HashMap<>();
		seatsGroups.put("availble", Seat.availbleSeats(sc));
		seatsGroups.put("reserved", Seat.notAvailbleSeats(sc));
		
		
		ServerConfig.sendObj(request, response, seatsGroups);
		
	}
}
