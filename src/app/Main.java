package app;

import java.util.Scanner;
import model.User;
import repository.UserRepository;
import service.ReportService;
import model.Income;
import repository.IncomeRepository;
import model.Expense;
import repository.CategoryRepository;
import repository.ExpenseRepository;

import java.time.LocalDate;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n===== Personal Finance Manager =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Select option: ");

            String input = scanner.nextLine();

            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number.");
                continue;
            }

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
                    System.out.println("Invalid option. Try again.");
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
            int choice = Integer.parseInt(scanner.nextLine());
            scanner.nextLine();

            switch (choice) {

                case 1:
                    addIncome(user);
                    break;

                case 2:
                    addExpense(user);
                    break;

                case 3:
                    ReportService report = new ReportService(user.getId());
                    double totalIncome = report.getTotalIncome();
                    double totalExpenses = report.getTotalExpenses();
                    double balance = totalIncome - totalExpenses;

                    System.out.println("\n===== Monthly Report =====");
                    System.out.println("Total Income: " + totalIncome);
                    System.out.println("Total Expenses: " + totalExpenses);
                    System.out.println("Remaining Balance: " + balance);

                    report.printCategoryExpenses();

                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }

    static void addIncome(User user) {

        System.out.println("\n--- Add Income ---");

        String[] incomeCategories = {"Salary", "Freelance", "Gift", "Other"};

        System.out.println("Select Category:");
        for (int i = 0; i < incomeCategories.length; i++) {
            System.out.println((i + 1) + ". " + incomeCategories[i]);
        }

        System.out.print("Choice: ");
        int catChoice = scanner.nextInt();
        scanner.nextLine();

        if (catChoice < 1 || catChoice > incomeCategories.length) {
            System.out.println("Invalid category.");
            return;
        }

        String categoryName = incomeCategories[catChoice - 1];

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);

        System.out.print("Note: ");
        String note = scanner.nextLine();

        // Fetch category_id from DB
        CategoryRepository categoryRepo = new CategoryRepository();
        int categoryId = categoryRepo.getCategoryIdByName(categoryName, "income");

        if (categoryId == -1) {
            System.out.println("Error: category not found in database.");
            return;
        }

        // Create Income object
        Income income = new Income();
        income.setUserId(user.getId());
        income.setCategoryId(categoryId);
        income.setAmount(amount);
        income.setDate(date);
        income.setNote(note);

        // Save to database
        IncomeRepository repo = new IncomeRepository();
        boolean success = repo.addIncome(income);

        if (success) {
            System.out.println("Income added successfully!");
        } else {
            System.out.println("Failed to add income.");
        }
    }

    static void addExpense(User user) {

        System.out.println("\n--- Add Expense ---");

        String[] expenseCategories = {"Food", "Transport", "Bills", "Shopping", "Entertainment"};

        System.out.println("Select Category:");
        for (int i = 0; i < expenseCategories.length; i++) {
            System.out.println((i + 1) + ". " + expenseCategories[i]);
        }

        System.out.print("Choice: ");
        int catChoice = scanner.nextInt();
        scanner.nextLine();

        if (catChoice < 1 || catChoice > expenseCategories.length) {
            System.out.println("Invalid category.");
            return;
        }

        String categoryName = expenseCategories[catChoice - 1];

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);

        System.out.print("Note: ");
        String note = scanner.nextLine();

        // Fetch category_id from DB
        CategoryRepository categoryRepo = new CategoryRepository();
        int categoryId = categoryRepo.getCategoryIdByName(categoryName, "expense");

        if (categoryId == -1) {
            System.out.println("Error: category not found in database.");
            return;
        }

        // Create Expense object
        Expense expense = new Expense();
        expense.setUserId(user.getId());
        expense.setCategoryId(categoryId);
        expense.setAmount(amount);
        expense.setDate(date);
        expense.setNote(note);

        // Save to database
        ExpenseRepository repo = new ExpenseRepository();
        boolean success = repo.addExpense(expense);

        if (success) {
            System.out.println("Expense added successfully!");
        } else {
            System.out.println("Failed to add expense.");
        }
    }
}