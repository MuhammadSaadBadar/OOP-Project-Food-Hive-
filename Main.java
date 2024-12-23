package org.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public AccountManager accountManager = new AccountManager();
    public AdminData adminData = new AdminData();
    public AdminDashboard admindashboard = new AdminDashboard();
    Dashboard dashboard = new Dashboard();
    VBox loginForm, signUpForm, title, forgotPasswordForm;
    Stage stage = new Stage();

    @Override
    public void start(Stage stage) {
//        dashboard.showDashboard();
        loginForm = createLoginForm();
        title = titleForm();
        signUpForm = createRegisterForm();
        forgotPasswordForm = createForgotPasswordForm();

        StackPane root = new StackPane();
        StackPane.setAlignment(loginForm, Pos.CENTER_RIGHT);
        StackPane.setAlignment(signUpForm, Pos.CENTER_RIGHT);
        StackPane.setAlignment(forgotPasswordForm, Pos.CENTER_RIGHT);
        StackPane.setAlignment(title, Pos.CENTER_LEFT);

        loginForm.setMaxSize(300, 200);
        signUpForm.setMaxSize(300, 200);
        title.setMaxSize(300, 600);
        forgotPasswordForm.setMaxSize(300, 200);

        signUpForm.setVisible(false);
        forgotPasswordForm.setVisible(false);
        root.getChildren().addAll(title, loginForm, signUpForm, forgotPasswordForm);

        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("Cafe Shop Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private VBox createLoginForm() {
        VBox login = new VBox(20);
        login.setPadding(new Insets(10, 50, 50, 50));
        login.setAlignment(Pos.CENTER_LEFT);

        Label loginTitle = new Label("Login Account");
        loginTitle.getStyleClass().add("login-title");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(30);
        usernameField.setPrefWidth(200);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(30);
        passwordField.setPrefWidth(200);

        // TextField for showing password dynamically
        TextField passwordTextField = new TextField();
        passwordTextField.setManaged(false);
        passwordTextField.setVisible(false);

        CheckBox showPasswordCheckBox = new CheckBox("Show Password");
        showPasswordCheckBox.setStyle("-fx-text-fill: #000; -fx-font-family: \"Arial\";");

        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());

        CheckBox adminCheckBox = new CheckBox("Admin");
        showPasswordCheckBox.setStyle("-fx-text-fill: #000; -fx-font-family: \"Arial\";");

        // Toggle visibility on checkbox
        showPasswordCheckBox.setOnAction(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                passwordTextField.setVisible(true);
                passwordTextField.setManaged(true);
            } else {
                passwordTextField.setVisible(false);
                passwordTextField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
            }
        });

        Hyperlink forgotPasswordLink = new Hyperlink("Forgot Password?");
        forgotPasswordLink.getStyleClass().add("forgot-password-link");
        forgotPasswordLink.setOnAction(e -> {
            loginForm.setVisible(false);
            forgotPasswordForm.setVisible(true);
        });

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("button-login");
        loginButton.setPrefHeight(30);
        loginButton.setPrefWidth(200);

        Label warning = new Label();

        // Event handler for login button
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username.isEmpty() || password.isEmpty()) {
                warning.setText("Please fill in all fields.");
                warning.setStyle("-fx-text-fill: red; -fx-font-family: \"Arial\";");
                return;
            }
            if (adminCheckBox.isSelected()) {
                if (username.equals(adminData.getName()) && password.equals(adminData.getPassword())) {
                    admindashboard.showAdminDashboard();
                    usernameField.clear();
                    passwordField.clear();
                } else {
                    warning.setText("Invalid username or password.");
                    warning.setStyle("-fx-text-fill: red; -fx-font-family: \"Arial\";");
                }
            } else if (accountManager.verify(username, password)) {
                warning.setText("Login successful!");
                warning.setStyle("-fx-text-fill: green; -fx-font-family: \"Arial\";");
                dashboard.showDashboard();
                usernameField.clear();
                passwordField.clear();
                this.stage = stage;
                stage.close();
            } else {
                warning.setText("Invalid username or password.");
                warning.setStyle("-fx-text-fill: red; -fx-font-family: \"Arial\";");
            }
        });

        login.getChildren().addAll(loginTitle, usernameField, passwordField, passwordTextField, showPasswordCheckBox,
                adminCheckBox, forgotPasswordLink, loginButton, warning);
        login.setMargin(loginTitle, new Insets(30, 0, 0, 0));
        return login;
    }

    private VBox createRegisterForm() {
        VBox register = new VBox(20);
        register.setPadding(new Insets(50, 50, 50, 50));
        register.setAlignment(Pos.CENTER_LEFT);

        Label registerTitle = new Label("Register Account");
        registerTitle.getStyleClass().add("register-title");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(30);
        usernameField.setPrefWidth(200);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefHeight(30);
        emailField.setPrefWidth(200);

        // Password fields
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(30);
        passwordField.setPrefWidth(200);

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setPrefHeight(30);
        confirmPasswordField.setPrefWidth(200);

        // TextFields for showing passwords dynamically
        TextField passwordTextField = new TextField();
        passwordTextField.setManaged(false);
        passwordTextField.setVisible(false);

        TextField confirmPasswordTextField = new TextField();
        confirmPasswordTextField.setManaged(false);
        confirmPasswordTextField.setVisible(false);

        // Synchronize text properties
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
        confirmPasswordTextField.textProperty().bindBidirectional(confirmPasswordField.textProperty());

        // Checkbox to toggle password visibility
        CheckBox showPasswordCheckBox = new CheckBox("Show Passwords");
        showPasswordCheckBox.setStyle("-fx-text-fill: #000; -fx-font-family: \"Arial\";");

        showPasswordCheckBox.setOnAction(e -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordField.setVisible(false);
                passwordField.setManaged(false);
                confirmPasswordField.setVisible(false);
                confirmPasswordField.setManaged(false);

                passwordTextField.setVisible(true);
                passwordTextField.setManaged(true);
                confirmPasswordTextField.setVisible(true);
                confirmPasswordTextField.setManaged(true);
            } else {
                passwordTextField.setVisible(false);
                passwordTextField.setManaged(false);
                confirmPasswordTextField.setVisible(false);
                confirmPasswordTextField.setManaged(false);

                passwordField.setVisible(true);
                passwordField.setManaged(true);
                confirmPasswordField.setVisible(true);
                confirmPasswordField.setManaged(true);
            }
        });

        Button signupButton = new Button("Signup");
        signupButton.getStyleClass().add("button-login");
        signupButton.setPrefHeight(30);
        signupButton.setPrefWidth(200);

        Label warning = new Label();

        // Event handler for signup button
        signupButton.setOnAction(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                warning.setText("Please fill in all fields.");
                warning.setStyle("-fx-text-fill: red; -fx-font-family: \"Arial\";");
                return;
            }
            if (password.length() < 8) {
                warning.setText("Password must be at least 8 characters long.");
                warning.setStyle("-fx-text-fill: red; -fx-font-family: \"Arial\";");
                return;
            }
            if (!password.equals(confirmPassword)) {
                warning.setText("Passwords do not match.");
                warning.setStyle("-fx-text-fill: red; -fx-font-family: \"Arial\";");
                return;
            }
            if (accountManager.register(username, password, email)) {
                warning.setText("Account created successfully!");
                warning.setStyle("-fx-text-fill: green; -fx-font-family: \"Arial\";");
                loginForm.setVisible(true);
                signUpForm.setVisible(false);
            } else {
                warning.setText("Username already exists.");
                warning.setStyle("-fx-text-fill: red; -fx-font-family: \"Arial\";");
            }
        });

        register.getChildren().addAll(registerTitle, usernameField, emailField, passwordField, passwordTextField,
                confirmPasswordField, confirmPasswordTextField, showPasswordCheckBox, signupButton, warning);

        return register;
    }

    private VBox titleForm() {
        VBox switchPane = new VBox(20);
        switchPane.setAlignment(Pos.CENTER);
        switchPane.getStyleClass().add("shift-pane");

        Label logo = new Label("\uD83C\uDF7D");
        logo.setStyle("-fx-font-size: 100; -fx-text-fill: white;");

        Label systemTitle = new Label("Food Hive");
        systemTitle.getStyleClass().add("title");

        Button createAccountButton = new Button("Create new Account");
        createAccountButton.getStyleClass().add("button-create-account");
        createAccountButton.setOnAction(e -> {
            signUpForm.setVisible(true);
            loginForm.setVisible(false);
        });

        Button alreadyAccountButton = new Button("Already have Account");
        alreadyAccountButton.getStyleClass().add("button-create-account");
        alreadyAccountButton.setOnAction(e -> {
            loginForm.setVisible(true);
            signUpForm.setVisible(false);
            stage.close();
        });

        switchPane.getChildren().addAll(logo, systemTitle, createAccountButton, alreadyAccountButton);
        return switchPane;
    }

    private VBox createForgotPasswordForm() {
        VBox forgotPassword = new VBox(10);
        forgotPassword.setPadding(new Insets(10, 50, 50, 50));
        forgotPassword.setAlignment(Pos.CENTER_LEFT);

        Label forgotPasswordTitle = new Label("Forgot Password");
        forgotPasswordTitle.getStyleClass().add("forgot-password-title");

        Label enterEmailTitle = new Label("Enter your Email.");

        TextField emailField = new TextField();
        emailField.setPrefWidth(200);
        emailField.setPrefHeight(30);

        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("button-submit");
        submitButton.setPrefWidth(200);
        submitButton.setPrefHeight(20);

        Label statusLabel = new Label();

        // VBox to hold the new password fields
        VBox resetPasswordForm = new VBox(20);
        resetPasswordForm.setVisible(false);
        resetPasswordForm.setManaged(false);

        TextField newPasswordField = new TextField();
        newPasswordField.setPromptText("New Password");
        newPasswordField.setPrefWidth(200);
        newPasswordField.setPrefHeight(20);

        TextField confirmPasswordField = new TextField();
        confirmPasswordField.setPromptText("Confirm New Password");
        confirmPasswordField.setPrefWidth(200);
        confirmPasswordField.setPrefHeight(20);

        Button savePasswordButton = new Button("Save Password");
        savePasswordButton.getStyleClass().add("button-save");
        savePasswordButton.setPrefWidth(200);
        savePasswordButton.setPrefHeight(20);

        savePasswordButton.setOnAction(e -> {
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                statusLabel.setText("Please fill in all fields.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            if (newPassword.length() < 8) {
                statusLabel.setText("Password must be at least 8 characters long.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            if (!newPassword.equals(confirmPassword)) {
                statusLabel.setText("Passwords do not match.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            // Update the password in the account manager
            accountManager.updatePassword(emailField.getText(), newPassword);

            // Show success message
            statusLabel.setText("Password saved successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");

            // Hide all forms and go back to the login page
            forgotPassword.setVisible(false);
            resetPasswordForm.setVisible(false);
            loginForm.setVisible(true);
        });

        resetPasswordForm.getChildren().addAll(newPasswordField, confirmPasswordField, savePasswordButton);

        submitButton.setOnAction(e -> {
            String email = emailField.getText();
            if (email.isEmpty()) {
                statusLabel.setText("Please enter an email address.");
                statusLabel.setStyle("-fx-text-fill: red;");
            } else if (!accountManager.isEmailRegistered(email)) { // Check email validity
                statusLabel.setText("Email not registered.");
                statusLabel.setStyle("-fx-text-fill: red;");
            } else {
                statusLabel.setText("Email verified. Enter your new password.");
                statusLabel.setStyle("-fx-text-fill: green;");
                resetPasswordForm.setVisible(true);
                resetPasswordForm.setManaged(true);
            }
        });

        Button backButton = new Button("Back to Login");
        backButton.getStyleClass().add("button-back");
        backButton.setPrefWidth(200);
        backButton.setPrefHeight(20);

        // Event handling for the back button
        backButton.setOnAction(e -> {
            // Reset the visibility and fields when going back
            resetPasswordForm.setVisible(false);
            resetPasswordForm.setManaged(false);
            statusLabel.setText("");
            emailField.clear();
            newPasswordField.clear();
            confirmPasswordField.clear();

            forgotPassword.setVisible(false);
            loginForm.setVisible(true);
        });

        forgotPassword.getChildren().addAll(forgotPasswordTitle, enterEmailTitle, emailField, submitButton, statusLabel,
                resetPasswordForm, backButton);
        forgotPassword.setMargin(forgotPasswordTitle, new Insets(25, 0, 0, 0));
        return forgotPassword;
    }

    public static void main(String[] args) {
        launch(args);
    }
}