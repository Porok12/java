
public class Ticket {
	private String name;

	public Ticket() {
		
	}
	
	public Ticket(Ticket ticket) {
		this.name = ticket.getName();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("name: %s", name);
	}
}
