package de.twenty11.skysail.server.services;

public interface MenuService {
	
	void addApplicationToMenu(String appIdentifier, String menuIdentifier, String link);

	void removeApplicationFromMenu(String appIdentifier, String menuIdentifier);

}
