package pl.macia.multiplex.core;

import java.time.LocalDateTime;
import java.util.*;

import com.google.gson.annotations.Expose;

/**
 * Movie object can be played in multiplex in the  {@link Screening} time
 * @author CodeServant
 *
 */
public class Movie {
	@Expose
	private String title;
	private Set<Screening> screenings = new HashSet<>();
	private static Map<String, Movie> movies = new TreeMap<>();

	public Movie(String title) throws Exception {
		if (title == null)
			throw new NullPointerException();
		this.title = title;
		addMovie(this);
	}

	protected static void addMovie(Movie m) throws Exception {
		if (movies.containsKey(m.title))
			throw new Exception("movie already have a title");
		movies.put(m.title, m);
	}

	protected void addScreening(Screening sc) {
		this.screenings.add(sc);
	}

	public Set<Screening> getScreenings() {
		return new HashSet<>(this.screenings);
	}

	public String getTitle() {
		return this.title;
	}

	public void delScreening(Screening screening) {
		if (this.screenings.contains(screening)) {
			this.screenings.remove(screening);
			screening.delScreening();
		}
	}

	/**
	 * How many movies there are in the extent.
	 * @return
	 */
	public static int movieCount() {
		return Movie.movies.size();
	}
	
	/**
	 * Get the movies and their screenings list with screenings between specific dates and time.
	 * @param start
	 * @param end
	 * @return
	 */
	public static Map<Movie, List<Screening>> getMoviesInBetween(LocalDateTime start, LocalDateTime end) {
		Map<Movie, List<Screening>> retMap = new TreeMap<>((a,b) -> a.getTitle().compareTo(b.getTitle()));
		Set<Screening> screenings = Screening.getAllFromPeriod(start, end);
		for (Screening s : screenings) {
			List<Screening> list = retMap.getOrDefault(s.getMovie(), new ArrayList<>());
			list.add(s);
			retMap.put(s.getMovie(), list);
		}
		for (List<Screening> entry : retMap.values())
			entry.sort((a, b) -> a.getDate().compareTo(b.getDate()));
		return retMap;
	}
	/**
	 * Retrierves movie instance by title.
	 * @param title
	 * @return
	 */
	public static Movie getMovieByTitle(String title) {
		return movies.get(title);
	}
	public String toString() {
		return getTitle();
	}
	public void delMovie() {
		this.screenings.forEach(this::delScreening);
		movies.remove(this.title);
	}

}
