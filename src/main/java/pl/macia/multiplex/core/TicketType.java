package pl.macia.multiplex.core;

import java.math.BigDecimal;
import java.util.*;

public enum TicketType {

	ADULT("a", new BigDecimal(25)), STUDENT("s", new BigDecimal(18)), CHILD("c", new BigDecimal("12.5"));

	private String shortName;
	private BigDecimal price;

	/**
	 * Creates new TicketType with the given price. If string already exists then
	 * the price is beeing replaced.
	 * 
	 * @param shortName
	 * @param price
	 */
	TicketType(String shortName, BigDecimal price) {
		this.shortName = shortName;
		this.price = price;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public String getShortName() {
		return this.shortName;
	}

	public static TicketType ofShortName(String shortName) {
		for (TicketType tick : TicketType.values()) {
			if (tick.getShortName().equals(shortName))
				return tick;
		}
		return null;
	}
}
