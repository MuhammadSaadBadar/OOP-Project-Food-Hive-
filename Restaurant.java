package org.project;
import java.util.List;

public class Restaurant {
    private String name;
    private String imagePath;
    private String description;
    private List<MenuItem> menuItems;

    public Restaurant(String name, String imagePath, String description, List<MenuItem> menuItems) {
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
        this.menuItems = menuItems;
    }

    public Restaurant(String name, String imagePath, String description) {
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }


}
