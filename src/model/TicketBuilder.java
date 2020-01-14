package model;

public class TicketBuilder {
	private static TicketBuilder instance = null;
	private Ticket ticket;
	
	private TicketBuilder() {
		ticket = new Ticket();
	}

	public static TicketBuilder getBuilder() {
		if (instance == null) {
			instance = new TicketBuilder();
		}
		return instance;
	}
	
	public Ticket build() {
		return new Ticket(ticket);
	}
	
	public TicketBuilder setName(String name) {
		ticket.setName(name);
		return this;
	}
}
