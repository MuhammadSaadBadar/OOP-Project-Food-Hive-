package org.project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataLoader {

    private static final String IMAGE_PATH = "/";
    private static final String RESTAURANT_FILE_PATH = "RestaurantsData.txt";
    private static final String IMAGE_PATH2 = "/";

    public static List<MenuItem> loadMenuItems(String filePath) {
        List<MenuItem> menuItems = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String name = data[0].trim();
                    double price = Double.parseDouble(data[1].trim());
                    String imagePath = IMAGE_PATH + data[2].trim(); // Concatenate base path
                    menuItems.add(new MenuItem(name, price, imagePath));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    public static List<Restaurant> loadRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(RESTAURANT_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Parse the line manually with handling for quoted fields
                String[] parts = parseCSVLine(line);

                if (parts.length < 4) {
                    continue; // Skip malformed lines
                }

                String name = parts[0].trim();
                String imagePath = parts[1].trim();
                String description = parts[2].trim();

                List<MenuItem> menuItems = new ArrayList<>();
                for (int i = 3; i < parts.length; i++) {
                    // Split each menu item by commas
                    String[] itemParts = parts[i].split(",");
                    if (itemParts.length == 3) {
                        String itemName = itemParts[0].trim();
                        double itemPrice = Double.parseDouble(itemParts[1].trim());
                        String itemImage = itemParts[2].trim();
                        menuItems.add(new MenuItem(itemName, itemPrice, itemImage));
                    } else {
                        System.out.println("Invalid menu item format: " + parts[i]);
                    }
                }

                restaurants.add(new Restaurant(name, imagePath, description, menuItems));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    // Custom method to parse the CSV line considering quoted fields
    private static String[] parseCSVLine(String line) {
        List<String> parts = new ArrayList<>();
        Matcher matcher = Pattern.compile("\"([^\"]*)\"|([^\",]+)").matcher(line);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                parts.add(matcher.group(1)); // This is the quoted part
            } else {
                parts.add(matcher.group(2)); // This is the unquoted part
            }
        }
        return parts.toArray(new String[0]);
    }

    public static List<MenuItem> loadCategoryItems(String category) {
        List<MenuItem> menuItems = new ArrayList<>();
        String fileName = category + "_items.txt"; // Construct the file name based on category

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String name = data[0].trim();
                    double price = Double.parseDouble(data[1].trim());
                    String imagePath = IMAGE_PATH2 + data[2].trim(); // Concatenate base path
                    menuItems.add(new MenuItem(name, price, imagePath));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    
    

}
