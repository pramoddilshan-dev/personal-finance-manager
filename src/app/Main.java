package app;

import java.util.List;
import java.util.Scanner;
import model.User;
import repository.UserRepository;
import service.ReportService;
import model.Income;
import repository.IncomeRepository;
import model.Expense;
import repository.BudgetRepository;
import repository.CategoryRepository;
import repository.ExpenseRepository;
import model.Budget;
import model.Category;

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
            System.out.println("4. Transaction History");
            System.out.println("5. Add Category");
            System.out.println("6. Set Budget");
            System.out.println("7. Logout");

            int choice;

            try {
                System.out.print("Select option: ");
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Enter a number.");
                continue;
            }

            switch (choice) {

                case 1:
                    addIncome(user);
                    break;

                case 2:
                    addExpense(user);
                    break;

                case 3:

                    int year;
                    int month;

                    try {
                        System.out.print("Enter Year: ");
                        year = Integer.parseInt(scanner.nextLine());

                        System.out.print("Enter Month (1-12): ");
                        month = Integer.parseInt(scanner.nextLine());

                        if (month < 1 || month > 12) {
                            System.out.println("Invalid month.");
                            break;
                        }

                    } catch (Exception e) {
                        System.out.println("Invalid input.");
                        break;
                    }

                    ReportService report = new ReportService(user.getId());

                    double income = report.getTotalIncome(year, month);
                    double expenses = report.getTotalExpenses(year, month);
                    double balance = income - expenses;

                    System.out.println("\n===== Monthly Finance Report =====");
                    System.out.println("Year: " + year + " Month: " + month);
                    System.out.println("Total Income: " + income);
                    System.out.println("Total Expenses: " + expenses);
                    System.out.println("Balance: " + balance);

                    report.printCategoryExpenses(year, month);

                    break;

                case 4:
                    viewTransactions(user);
                    break;

                case 5:
                    addCategory();
                    break;

                case 6:
                    setBudget(user);
                    break;

                case 7:
                    System.out.println("Logged out.");
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    static void addIncome(User user) {

        System.out.println("\n--- Add Income ---");

        CategoryRepository categoryRepo = new CategoryRepository();
        List<Category> categories = categoryRepo.getCategoriesByType("income");

        if (categories.isEmpty()) {
            System.out.println("No income categories found. Please add categories first.");
            return;
        }

        System.out.println("Select Category:");

        for (int i = 0; i < categories.size(); i++) {
            Category c = categories.get(i);
            System.out.println((i + 1) + ". " + c.getName());
        }

        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > categories.size()) {
            System.out.println("Invalid category.");
            return;
        }

        Category selectedCategory = categories.get(choice - 1);

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);

        System.out.print("Note: ");
        String note = scanner.nextLine();

        // Create Income object
        Income income = new Income();
        income.setUserId(user.getId());
        income.setCategoryId(selectedCategory.getId());
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

        CategoryRepository categoryRepo = new CategoryRepository();
        List<Category> categories = categoryRepo.getCategoriesByType("expense");

        if (categories.isEmpty()) {
            System.out.println("No expense categories found. Please add categories first.");
            return;
        }

        System.out.println("Select Category:");

        for (int i = 0; i < categories.size(); i++) {
            Category c = categories.get(i);
            System.out.println((i + 1) + ". " + c.getName());
        }

        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > categories.size()) {
            System.out.println("Invalid category.");
            return;
        }

        Category selectedCategory = categories.get(choice - 1);

        System.out.print("Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);

        System.out.print("Note: ");
        String note = scanner.nextLine();

        // -------- Budget Checking --------
        BudgetRepository budgetRepo = new BudgetRepository();

        int month = date.getMonthValue();
        int year = date.getYear();

        double budgetLimit = budgetRepo.getBudgetLimit(
                user.getId(),
                selectedCategory.getId(),
                month,
                year
        );

        if (budgetLimit > 0 && amount > budgetLimit) {
            System.out.println("⚠ WARNING: This expense exceeds the budget limit!");
        }
        // ---------------------------------

        // Create Expense object
        Expense expense = new Expense();
        expense.setUserId(user.getId());
        expense.setCategoryId(selectedCategory.getId());
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

    static void addCategory() {

        System.out.println("\n--- Add Category ---");

        System.out.print("Category Name: ");
        String name = scanner.nextLine();

        System.out.print("Type (income/expense): ");
        String type = scanner.nextLine();

        Category category = new Category();
        category.setName(name);
        category.setType(type);

        CategoryRepository repo = new CategoryRepository();

        boolean success = repo.addCategory(category);

        if (success) {
            System.out.println("Category added successfully!");
        } else {
            System.out.println("Failed to add category.");
        }
    }

    static void viewTransactions(User user) {

        System.out.println("\n===== Transaction History =====");

        // Updated header with Category name
        System.out.printf("%-12s %-10s %-12s %-10s %-15s\n",
                "Date", "Type", "Category", "Amount", "Note");

        System.out.println("-------------------------------------------------------------");

        IncomeRepository incomeRepo = new IncomeRepository();
        ExpenseRepository expenseRepo = new ExpenseRepository();

        // Print transactions with category names
        incomeRepo.printIncomeTransactions(user.getId());
        expenseRepo.printExpenseTransactions(user.getId());
    }

    static void setBudget(User user){

        CategoryRepository categoryRepo = new CategoryRepository();
        List<Category> categories = categoryRepo.getCategoriesByType("expense");

        System.out.println("\nSelect Category:");

        for(int i=0;i<categories.size();i++){
            System.out.println((i+1)+". "+categories.get(i).getName());
        }

        int choice = scanner.nextInt();
        scanner.nextLine();

        Category selected = categories.get(choice-1);

        System.out.print("Budget Amount: ");
        double amount = scanner.nextDouble();

        System.out.print("Month (1-12): ");
        int month = scanner.nextInt();

        System.out.print("Year: ");
        int year = scanner.nextInt();
        scanner.nextLine();

        Budget budget = new Budget();
        budget.setUserId(user.getId());
        budget.setCategoryId(selected.getId());
        budget.setLimitAmount(amount);
        budget.setMonth(month);
        budget.setYear(year);

        BudgetRepository repo = new BudgetRepository();

        if(repo.addBudget(budget)){
            System.out.println("Budget saved!");
        }else{
            System.out.println("Failed to save budget.");
        }
    }
}