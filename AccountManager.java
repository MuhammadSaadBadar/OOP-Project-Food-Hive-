package org.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {

    private static final String ACCOUNTS_FILE = "accounts.dat";
    private List<Account> accounts;

    public AccountManager() {
        accounts = new ArrayList<>();
        loadAccounts();
    }

    // Method to load accounts from the file
    private void loadAccounts() {
        File file = new File(ACCOUNTS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object data = ois.readObject();

                if (data instanceof List<?>) {
                    accounts = (List<Account>) data;
                } else {
                    System.err.println("Error: The file does not contain a valid List<Account>.");
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading accounts: " + e.getMessage());
            }
        }
    }


    // Method to save accounts to the file
    private void saveAccounts() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ACCOUNTS_FILE))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }

    // Method to register a new account
    public boolean register(String username, String password, String email) {
        for (Account account : accounts) {
            if (account.getUsername().equals(username)) {
                return false; 
            }
        }
        accounts.add(new Account(username, password, email));
        saveAccounts();
        return true;
    }

    // Method to verify login credentials
    public boolean verify(String username, String password) {
        for (Account account : accounts) {
            if (account.getUsername().equals(username) && account.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Check if an email is registered
    public boolean isEmailRegistered(String email) {
        for (Account account : accounts) {
            if (account.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    // Update password for a given email
    public void updatePassword(String email, String newPassword) {
        for (Account account : accounts) {
            if (account.getEmail().equals(email)) {
                account.setPassword(newPassword);
                saveAccounts();
                return;
            }
        }
    }
}
