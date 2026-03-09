package app;

import java.util.Scanner;
import model.User;
import repository.UserRepository;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n===== Personal Finance Manager =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    register();
                    break;

                case 2:
                    login();
                    break;

                case 3:
                    System.out.println("Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid option");
            }
        }
    }

    static void register() {

        System.out.println("\n--- Register ---");

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        UserRepository repo = new UserRepository();

        boolean success = repo.registerUser(user);

        if (success) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("Registration failed.");
        }
    }

        static void login() {

        System.out.println("\n--- Login ---");

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        UserRepository repo = new UserRepository();
        User user = repo.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {

            System.out.println("Login successful! Welcome " + user.getName());

            userMenu(user);

        } else {

            System.out.println("Invalid email or password.");
        }
    }
    static void userMenu(User user) {

        while (true) {

            System.out.println("\n===== Dashboard =====");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Report");
            System.out.println("4. Logout");

            System.out.print("Select option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("Income feature coming next...");
                    break;

                case 2:
                    System.out.println("Expense feature coming next...");
                    break;

                case 3:
                    System.out.println("Report feature coming next...");
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
}