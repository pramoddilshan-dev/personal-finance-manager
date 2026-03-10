package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.User;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label incomeLabel;

    @FXML
    private Label expenseLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Button addIncomeButton;

    @FXML
    private Button addExpenseButton;

    @FXML
    private Button viewReportsButton;

    @FXML
    private Button manageCategoriesButton;

    private User loggedInUser;

    public void setUser(User user) {

        this.loggedInUser = user;

        welcomeLabel.setText("Welcome, " + user.getName() + "!");

        // Temporary demo values
        incomeLabel.setText("$0");
        expenseLabel.setText("$0");
        balanceLabel.setText("$0");
    }

    @FXML
    public void initialize() {

        addIncomeButton.setOnAction(e -> System.out.println("Add Income Clicked"));

        addExpenseButton.setOnAction(e -> System.out.println("Add Expense Clicked"));

        viewReportsButton.setOnAction(e -> System.out.println("View Reports Clicked"));

        manageCategoriesButton.setOnAction(e -> System.out.println("Manage Categories Clicked"));
    }
}