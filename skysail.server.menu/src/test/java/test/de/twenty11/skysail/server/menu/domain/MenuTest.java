package test.de.twenty11.skysail.server.menu.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import de.twenty11.skysail.server.menu.domain.Menu;

public class MenuTest {

	private Menu root;
	private Menu mainMenu;

	@Before
	public void setUp() throws Exception {
		root = new Menu();
		mainMenu = new Menu(root, "name", "link");
	}

	@Test
	public void root_has_proper_attributes() {
		assertThat(root.getName(), is(nullValue()));
		assertThat(root.getParent(), is (nullValue()));
		assertThat(root.getPath(), is("/"));
	}

	@Test
	public void menu_without_parentMenu_has_proper_attributes() {
		assertThat(mainMenu.getName(), is("name"));
		assertThat(mainMenu.getParent(), is(root));
		assertThat(mainMenu.getPath(), is(equalTo("/name/")));
	}

	@Test
	public void menu_with_parent_has_proper_path() {
		Menu subMenu = new Menu(mainMenu, "sub", "sublink");
		assertThat(subMenu.getName(), is("sub"));
		assertThat(subMenu.getParent(), is(equalTo(mainMenu)));
		assertThat(subMenu.getPath(), is(equalTo("/name/sub/")));
	}

	@Test
	public void parent_menu_knows_about_children() {
		Menu subMenu = new Menu(mainMenu, "sub", "sublink");
		assertThat(mainMenu.getChildren().size(), is(1));
		assertThat(mainMenu.getChildren().get(0), is(equalTo(subMenu)));
	}

	@Test
	public void children_are_found_via_path_from_root() {
		new Menu(mainMenu, "sub1", "sublink1"); // path is '/name/sub1/'
		new Menu(mainMenu, "sub2", "sublink2"); // path is '/name/sub2/'
		assertThat(root.getChildren("/").size(), is(1));
		assertThat(root.getChildren("/name/").size(), is(2));
		assertThat(mainMenu.getChildren("/").size(), is(2));
		assertThat(mainMenu.getChildren("/sub1").size(), is(0));
	}
}
