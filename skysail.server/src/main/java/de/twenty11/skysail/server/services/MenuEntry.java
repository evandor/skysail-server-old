package de.twenty11.skysail.server.services;

public class MenuEntry {

    private String name;
    private String link;
    private String menuIdentifier;

    public MenuEntry(String menuIdentifier,String name,String link) {
        this.menuIdentifier = menuIdentifier;
        this.name = name;
        this.link = link;
    }
    
    public String getName() {
        return name;
    }
    public String getLink() {
        return link;
    }
    public String getMenuIdentifier() {
        return menuIdentifier;
    }
}
