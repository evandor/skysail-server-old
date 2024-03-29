package de.twenty11.skysail.server.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.codehaus.jackson.annotate.JsonIgnore;

public class Menu {

	private String name;
	private String path;
    private String link;
	private Menu parent = null;

	@JsonIgnore
	private List<Menu> children = new ArrayList<Menu>();

	/**
	 * creates the root for the menu system. It doesn't have a name and the path is '/'.
	 */
	public Menu() {
		this.name = null;
		this.path = "/";
		this.link = "#";
	}
	
	/**
	 * creates a sub menu with given name for the parent menu.
	 * 
	 * The path always ends with '/'
	 */
	public Menu(Menu parentMenu, String name, String link) {
		Validate.notNull(parentMenu, "parent menu may not be null");
		this.name = name;
		this.parent = parentMenu;
		this.parent.addChild(this);
		this.path = parentMenu.getPath() + name + "/";
        this.link = link;
	}

    public List<Menu> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public List<Menu> getChildren(String path) {
		if (path == null) {
			return Collections.emptyList();
		}
		if (!path.endsWith("/")) {
			path = path + "/";
		}
		List<Menu> result = new ArrayList<Menu>();
		String[] pathParts = path.split("/");
		if (pathParts.length == 0) {
			return getChildren();
		}
		for (Menu child : getChildren()) {
			if (child.getName().equals(pathParts[1])) {
				result.addAll(child.getChildren());
				break;
			}
		}
		return result;
	}

    public void remove(String menuIdentifier) {
        
    }


	public String getName() {
		return name;
	}
	
	public Menu getParent() {
		return parent;
	}

	public String getLink() {
        return link;
    }

	@Override
	public String toString() {
		return getPath();
	}

	private void addChild(Menu menu) {
		this.children.add(menu);
	}

	public String getPath() {
		return path;
	}

}
