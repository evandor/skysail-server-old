package de.twenty11.skysail.server.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;

public class Menu {

	private String name;
	private Menu parent = null;
	private List<Menu> children = new ArrayList<Menu>();

	/**
	 * creates a main (root) menu with given name
	 */
	public Menu(String name) {
		this.name = name;
	}
	
	/**
	 * creates a sub menu with given name for menu mainMenu
	 */
	public Menu(Menu parentMenu, String name) {
		Validate.notNull(parentMenu, "parent menu may not be null");
		this.name = name;
		this.parent = parentMenu;
		this.parent.addChild(this);
	}

	public List<Menu> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public String getName() {
		return name;
	}
	
	public Menu getParent() {
		return parent;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	private void addChild(Menu menu) {
		this.children.add(menu);
	}


}
