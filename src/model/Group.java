package model;

import java.util.logging.Logger;
import javax.swing.JList;
import GUI.SwingDemo;

public class Group {
	private int id;
	private Status status;
	private StringList users;
	private int capacity;
	private static final Logger logger = Logger.getLogger(Group.class.getName());

	public Group() {
		status = Status.AVAILABLE;
		users = new StringList();
		capacity = 10;
	}
	
	public Group(Group group) {
		this.status = group.getStatus();
		this.users = group.users;
		this.id = group.getId(); 
		this.capacity = group.getCapacity(); 
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return String.format("Group%d", id);
	}
	
	public static enum Status {
		AVAILABLE,
		RESERVED
	}

	public boolean addUser(String login) {
		if (status == Status.RESERVED) {
			return false;
		}
		
		logger.info(this.toString() + " > user " + login);
		users.add(login);
		
		if (users.size() >= capacity) {
			status = Status.RESERVED;
		}
		
		return true;
	}
	
	public void printUsers() {
		StringBuilder sb = new StringBuilder();
		users.forEach(s -> sb.append(s+'\n'));
		System.out.println(sb);
	}

	public final int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public StringList getUsers() {
		return users;
	}
}