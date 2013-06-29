package test.de.twenty11.skysail.server.menu.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import de.twenty11.skysail.server.menu.domain.Menu;

public class MenuTest {

	private Menu mainMenu;

	@Before
	public void setUp() throws Exception {
		mainMenu = new Menu("name");
	}

	@Test 
	public void menu_without_parentMenu_can_be_created() {
		assertThat(mainMenu.getName(), is("name"));
		assertThat(mainMenu.getParent(), is(nullValue()));
	}
	
	@Test
	public void menu_with_parent_can_be_created() {
		Menu subMenu = new Menu(mainMenu, "sub");
		assertThat(subMenu.getName(), is("sub"));
		assertThat(subMenu.getParent(), is(equalTo(mainMenu)));
	}
	
	@Test
	public void parent_menu_knows_about_children() {
		Menu subMenu = new Menu(mainMenu, "sub");
		assertThat(mainMenu.getChildren().size(), is(1));
		assertThat(mainMenu.getChildren().get(0), is(equalTo(subMenu)));
	}
}
