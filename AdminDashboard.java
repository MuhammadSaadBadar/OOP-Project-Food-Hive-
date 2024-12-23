package org.project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static  org.project.Dashboard.categories;
import static org.project.Dashboard.showRestaurant;

public class AdminDashboard {

    TableView<MenuItem> tableView = new TableView<>();

    private static final String MENU_DATA_FILE = "MenuData.txt";

    public void showAdminDashboard() {
        Stage adminStage = new Stage();
        adminStage.setTitle("Admin Dashboard - Cafe Shop Management");

        // Define VBox for layout
        VBox vBox1 = new VBox(20);
        VBox vBox2 = new VBox(20);
        VBox vBox3 = new VBox(20);

        vBox1.setMaxWidth(150);
        vBox2.setMaxWidth(500);
        vBox3.setMaxWidth(300);

        // Set styles
        vBox1.getStyleClass().add("vbox1-style");
        vBox2.getStyleClass().add("vbox2-style");
        vBox3.getStyleClass().add("vbox3-style");

        setupVBox1(vBox1);
        setupVBox2(vBox2);

        HBox mainLayout = new HBox(20, vBox1, vBox2, vBox3);
        mainLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainLayout, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        adminStage.setScene(scene);
        adminStage.setFullScreen(false);
        adminStage.show();
    }

    // Set up VBox1 for navigation buttons
    private void setupVBox1(VBox vBox1) {
        vBox1.setPadding(new Insets(20));
        vBox1.setAlignment(Pos.TOP_CENTER);

        Button homeButton = new Button("Home");
        homeButton.setOnAction(event -> {
            // Implement Home functionality (if needed)
        });

        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(event -> {
            // Implement Sign-out functionality to go back to the login form
            // Close the admin dashboard and show the login screen
            Stage stage = (Stage) signOutButton.getScene().getWindow();
            stage.close();  // Close the admin dashboard window
            new Main();  // Implement LoginForm to go back to the login screen
        });

        VBox buttons = new VBox(20, homeButton, signOutButton);
        buttons.setAlignment(Pos.CENTER);
        vBox1.getChildren().add(buttons);
    }

    // Set up VBox2 to display menu items and allow editing
    private void setupVBox2(VBox vBox2) {
        vBox2.setPadding(new Insets(10));
        vBox2.setAlignment(Pos.CENTER);

        // Define the columns for the table
        TableColumn<MenuItem, String> itemNameColumn = new TableColumn<>("Item Name");
        itemNameColumn.setMinWidth(100);
        itemNameColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() -> cellData.getValue().getName()));

        TableColumn<MenuItem, Double> itemPriceColumn = new TableColumn<>("Price");
        itemPriceColumn.setMinWidth(100);
        itemPriceColumn.setCellValueFactory(cellData ->
                Bindings.createDoubleBinding(() -> cellData.getValue().getPrice()).asObject());

        TableColumn<MenuItem, String> itemImageColumn = new TableColumn<>("Image Path");
        itemImageColumn.setMinWidth(150);
        itemImageColumn.setCellValueFactory(cellData ->
                javafx.beans.binding.Bindings.createStringBinding(() -> cellData.getValue().getImagePath()));


        // Define the action to edit a menu item
        TableColumn<MenuItem, Void> editColumn = new TableColumn<>("Edit");
        editColumn.setMinWidth(100);
        editColumn.setCellFactory(param -> new TableCell<MenuItem, Void>() {
            private final Button editButton = new Button("Edit");

            {
                editButton.setOnAction(event -> {
                    MenuItem menuItem = getTableView().getItems().get(getIndex());
                    showEditDialog(menuItem); // Show edit dialog
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        });

        // Add the columns to the table
        tableView.getColumns().clear();
        tableView.getColumns().addAll(itemNameColumn, itemPriceColumn, itemImageColumn, editColumn);

        // Load menu items from the file
        List<MenuItem> menuItems = DataLoader.loadMenuItems(MENU_DATA_FILE);
        tableView.getItems().setAll(menuItems);

        vBox2.getChildren().clear();
        vBox2.getChildren().add(tableView);

        // Add "Save Changes" button to save changes
        Button saveChangesButton = new Button("Submit Changes");
        saveChangesButton.setOnAction(event -> saveMenuChanges());
        vBox2.getChildren().add(saveChangesButton);
    }

    // Show a dialog to edit a menu item
    private void showEditDialog(MenuItem menuItem) {
        TextInputDialog nameInput = new TextInputDialog(menuItem.getName());
        nameInput.setTitle("Edit Item");
        nameInput.setHeaderText("Edit Item Name");
        nameInput.setContentText("Name:");

        TextInputDialog priceInput = new TextInputDialog(String.valueOf(menuItem.getPrice()));
        priceInput.setTitle("Edit Item");
        priceInput.setHeaderText("Edit Price");
        priceInput.setContentText("Price:");

        TextInputDialog imageInput = new TextInputDialog(menuItem.getImagePath());
        imageInput.setTitle("Edit Item");
        imageInput.setHeaderText("Edit Image Path");
        imageInput.setContentText("Image Path:");

        // Show dialog and wait for the result
        nameInput.showAndWait().ifPresent(newName -> menuItem.setName(newName));
        priceInput.showAndWait().ifPresent(newPrice -> menuItem.setPrice(Double.parseDouble(newPrice)));
        imageInput.showAndWait().ifPresent(newImage -> menuItem.setImagePath(newImage));
    }

    // Save the changes made to the menu items back to the file
    private void saveMenuChanges() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MENU_DATA_FILE))) {
            for (MenuItem menuItem : tableView.getItems()) {
                writer.write(menuItem.getName() + "," + menuItem.getPrice() + "," + menuItem.getImagePath() + "\n");
            }
            showInfoDialog("Changes saved successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            showInfoDialog("Failed to save changes!");
        }
    }

    // Show an information dialog to the admin
    private void showInfoDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showRestaurants() {
        // Clear the current content
        categories.getChildren().clear();
        showRestaurant.getChildren().clear();
        showRestaurant.setPadding(new Insets(20));

        // Load restaurant data using DataLoader
        List<Restaurant> restaurants = DataLoader.loadRestaurants();

        // Create a FlowPane for restaurants
        FlowPane flowPane = new FlowPane();
        flowPane.setPadding(new Insets(2, 10, 5, 10));
        flowPane.setHgap(25);
        flowPane.setVgap(25);
        flowPane.setPrefWidth(470);
        flowPane.setAlignment(Pos.CENTER);

        for (Restaurant restaurant : restaurants) {
            // Create a VBox for each restaurant
            VBox restaurantBox = new VBox(5);
            restaurantBox.setPrefSize(180, 280);
            restaurantBox.getStyleClass().add("restaurantBox");
            restaurantBox.setAlignment(Pos.CENTER);

            // Add restaurant name and description
            Button nameLabel = new Button(restaurant.getName());
            nameLabel.setPrefSize(150, 30);
            nameLabel.getStyleClass().add("nameLabel");

            // Display restaurant image
            ImageView imageView = new ImageView(restaurant.getImagePath());
            imageView.setFitWidth(230);
            imageView.setFitHeight(80);
            imageView.setPreserveRatio(true);

            // Description of restaurant
            Label descriptionLabel = new Label(restaurant.getDescription());
            descriptionLabel.setWrapText(true);
            descriptionLabel.setStyle("-fx-text-fill: #ffce1b; -fx-font-size: 12; -fx-font-weight: bold;");

            // Action to show menu for the selected restaurant
            nameLabel.setOnMouseClicked(event -> showMenu(restaurant));

            // Add all elements to the restaurant VBox
            restaurantBox.getChildren().addAll(imageView, descriptionLabel, nameLabel);
            flowPane.getChildren().add(restaurantBox);
        }

        // Add the flow pane to the showRestaurant VBox
        showRestaurant.getChildren().add(flowPane);
    }

    private void showMenu(Restaurant restaurant) {
        // Clear the current content in showRestaurant VBox
        showRestaurant.getChildren().clear();

        // Set padding and alignment for showRestaurant
        showRestaurant.setPadding(new Insets(20));
        showRestaurant.setAlignment(Pos.CENTER);

        // Add a back button to navigate back to the restaurant list
        Button backButton = new Button("Back to Restaurants");
        backButton.setStyle("-fx-font-size: 14; -fx-background-color: #ffce1b; -fx-text-fill: black;");
        backButton.setOnAction(event -> showRestaurants());

        // Get the menu items for the selected restaurant
        List<MenuItem> menuItems = restaurant.getMenuItems();

        // Create a TilePane for displaying menu items
        TilePane tilePane = new TilePane();
        tilePane.setPadding(new Insets(5));
        tilePane.setHgap(10);
        tilePane.setVgap(5);
        tilePane.setPrefColumns(3);
        tilePane.setAlignment(Pos.CENTER);

        for (MenuItem menuItem : menuItems) {
            // Create a VBox for each menu item
            VBox itemBox = new VBox(10);
            itemBox.setPadding(new Insets(10));
            itemBox.setAlignment(Pos.CENTER);
            itemBox.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-padding: 10px;");

            // Menu item name, price, and image path
            String itemName = menuItem.getName();
            double itemPrice = menuItem.getPrice();
            String imagePath = menuItem.getImagePath();

            // Create an ImageView for the menu item
            ImageView imageView = null;
            URL resource = getClass().getResource(imagePath);
            if (resource != null) {
                imageView = new ImageView(resource.toExternalForm());
                imageView.setFitWidth(150);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
            }

            // Create labels for item name and price
            Label itemNameLabel = new Label(itemName);
            itemNameLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #ffce1b;");

            Label itemPriceLabel = new Label("$" + itemPrice);
            itemPriceLabel.setStyle("-fx-font-size: 12; -fx-text-fill: white;");

            // Create an HBox for the counter buttons
            HBox counterBox = new HBox(10);
            counterBox.setAlignment(Pos.CENTER);

            Button minusButton = new Button("-");
            minusButton.getStyleClass().add("minus-button");

            Label quantityLabel = new Label("0");
            quantityLabel.setStyle("-fx-font-size: 16; -fx-text-fill: #ffce1b;");

            Button plusButton = new Button("+");
            plusButton.getStyleClass().add("plus-button");

            // Add buttons to the counter box
            counterBox.getChildren().addAll(quantityLabel);

            final int[] quantity = {0};

            // Action for minus button
            minusButton.setOnAction(e -> {
                if (quantity[0] > 0) {
                    quantity[0]--;
                    quantityLabel.setText(String.valueOf(quantity[0]));
                }
            });

            // Action for plus button
            plusButton.setOnAction(e -> {
                quantity[0]++;
                quantityLabel.setText(String.valueOf(quantity[0]));
            });

            // Create an "Add to Order" button
            Button addToCartButton = new Button("Add to Order");
            addToCartButton.setStyle("-fx-background-color: #ffce1b; -fx-text-fill: black;");
            addToCartButton.setOnAction(event -> {
                if (quantity[0] > 0) {
                    addItemToTable(itemName, quantity[0], itemPrice * quantity[0]);
                    quantity[0] = 0; // Reset quantity after adding to cart
                    quantityLabel.setText(String.valueOf(quantity[0]));
                }
            });

            // Create an HBox for the name and price labels
            HBox namePriceBox = new HBox(10);
            namePriceBox.setAlignment(Pos.CENTER);
            namePriceBox.getChildren().addAll(itemNameLabel, itemPriceLabel);

            // Create an HBox for the counter buttons and add-to-cart button
            HBox hBoxCart = new HBox(10);
            hBoxCart.setAlignment(Pos.CENTER);
            hBoxCart.getChildren().addAll(minusButton, addToCartButton, plusButton);

            // Add all elements to the itemBox
            itemBox.getChildren().addAll(namePriceBox, imageView, counterBox, hBoxCart);

            // Add the itemBox to the tilePane
            tilePane.getChildren().add(itemBox);
        }

        // Add the back button and the tilePane to showRestaurant VBox
        showRestaurant.getChildren().addAll(backButton, tilePane);
    }

    private void addItemToTable(String itemName, int quantity, double itemPrice) {
        ObservableList<MenuItem> items = tableView.getItems();

        // Check if the item already exists in the table
//        for (MenuItem item : items) {
//            if (item.getName().equals(itemName)) {
//                item.setQuantity(item.getQuantity() + quantity);
//                item.setPrice(itemPrice * item.getQuantity());
//                tableView.refresh();
//                return;
//            }
//        }

        // If item doesn't exist, add it to the table as a new item
//        items.add(new Item(itemName, quantity, itemPrice));
    }

}
