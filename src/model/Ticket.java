package model;

public class Ticket {
	private String name;
	private Status status;

	public Ticket() {
		status = Status.AVAILABLE;
	}
	
	public Ticket(Ticket ticket) {
		this();
		this.name = ticket.getName(); 
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return String.format("name: %s", name);
	}
	
	public static enum Status {
		AVAILABLE,
		RESERVED
	}
}
