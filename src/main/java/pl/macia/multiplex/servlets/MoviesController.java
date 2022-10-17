package pl.macia.multiplex.servlets;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Function;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


import pl.macia.multiplex.core.*;

/**
 * Servlet implementation class MoviesController
 */
@WebServlet("/movies")
public class MoviesController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MoviesController() {
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
		Function<String, LocalDateTime> parseDate = (s) -> LocalDateTime.parse(request.getParameter(s),
				ServerConfig.dateFormatter);

		LocalDateTime from = parseDate.apply("from"),
				to = parseDate.apply("to");

		ServerConfig.sendObj(request, response, Movie.getMoviesInBetween(from, to));
	}
}
