package pl.macia.multiplex.core;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

public class Person {
	private String name;
	private String surneme;
	private Set<Reservation> reservations = new HashSet<>();
	private static Set<Person> persons = new TreeSet<Person>(
			(a, b) -> a.getSurname().compareTo(a.getSurname()) * 2 + a.getName().compareTo(a.getName()));

	public Person(String name, String surneme) throws Exception {
		setName(name);
		setSurneme(surneme);
		Person.persons.add(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) throws Exception {
		if (name == null)
			throw new NullPointerException();
		if (validName(name))
			this.name = name;
		else
			throw new Exception("name not valid");
	}

	public String getSurname() {
		return surneme;
	}
	public static Set<Person> getPersons() {
		Set<Person> prs = new TreeSet<Person>(
				(a, b) -> a.getSurname().compareTo(a.getSurname()) * 2 + a.getName().compareTo(a.getName()));
		prs.addAll(Person.persons);
		return prs;
	}
	
	protected Set<Reservation> getReservations() {
		return reservations;
	}
	public void setSurneme(String surneme) throws Exception {
		if (surneme == null)
			throw new NullPointerException();
		if (validSurname(surneme))
			this.surneme = surneme;
		else
			throw new Exception("surname not valid");
	}

	private static boolean validName(String name) {
		return name.matches("[A-ZŻŹĄĆŃŚĘÓŁ][a-zzżźąćńśęół]{2,}");
	}

	private static boolean validSurname(String surname) {
		return surname.matches("[A-ZŻŹĄĆŃŚĘÓŁ][a-zzżźąćńśęół]{2,}(-[A-ZŻŹĄĆŃŚĘÓŁ][a-zzżźąćńśęół]{2,})?");
	}

	public void delPerson() {
		Person.persons.remove(this);
	}
	protected void addReservation(Reservation reservation) {
		this.reservations.add(reservation);
	}

	public void delReservation(Reservation r) {
		if (this.reservations.contains(r)) {
			this.reservations.remove(r);
			r.delReservation();
		}
	}

	protected void delReservation(Collection<Reservation> r) {
		new CopyOnWriteArrayList<>(r).forEach(this::delReservation);

	}
}
