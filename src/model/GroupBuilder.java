package model;

import java.util.logging.Logger;

import javax.swing.JList;

import GUI.SwingDemo;

public class GroupBuilder {
	private Group group;
	private static final Logger logger = Logger.getLogger(GroupBuilder.class.getName());	
	
	public GroupBuilder() {
		group = new Group();
	}
	
	public Group build() {
		return new Group(group);
	}
	
	public Group buildAndAddToSwing() {
		Group newGroup = new Group(group);
		SwingDemo.panel.add(new JList<String>(newGroup.getUsers()));
		SwingDemo.panel.validate();
		SwingDemo.panel.repaint();
		return newGroup;
	}
	
	public GroupBuilder setId(int id) {
		group.setId(id);
		return this;
	}
	
	public GroupBuilder setCapacity(int capacity) {
		group.setCapacity(capacity);
		return this;
	}
}
