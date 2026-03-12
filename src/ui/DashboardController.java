package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
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
    private Button viewTransactionsButton;

    @FXML
    private Button viewReportsButton;

    @FXML
    private Button manageCategoriesButton;

    private User loggedInUser;

    public void setUser(User user) {

        this.loggedInUser = user;

        welcomeLabel.setText("Welcome, " + user.getName() + "!");

        // Temporary values (until we connect repositories)
        incomeLabel.setText("$0");
        expenseLabel.setText("$0");
        balanceLabel.setText("$0");
    }

    @FXML
    public void initialize() {

        addIncomeButton.setOnAction(e -> openAddIncomeWindow());

        addExpenseButton.setOnAction(e -> openAddExpense());

        viewTransactionsButton.setOnAction(e -> openTransactions());

        viewReportsButton.setOnAction(e -> openReports());

        manageCategoriesButton.setOnAction(e -> openCategoryManagement());
    }

    private void openAddIncomeWindow() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/AddIncomeView.fxml"));
            Parent root = loader.load();

            AddIncomeController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = new Stage();
            stage.setTitle("Add Income");
            stage.setScene(new Scene(root, 400, 320));
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openAddExpense() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/AddExpenseView.fxml"));
            Parent root = loader.load();

            AddExpenseController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = new Stage();
            stage.setTitle("Add Expense");
            stage.setScene(new Scene(root, 450, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openTransactions() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/TransactionsView.fxml"));
            Parent root = loader.load();

            TransactionsController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = new Stage();
            stage.setTitle("Transactions");
            stage.setScene(new Scene(root, 700, 500));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openReports() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/ReportsView.fxml"));
            Parent root = loader.load();

            ReportsController controller = loader.getController();
            controller.setUser(loggedInUser);

            Stage stage = new Stage();
            stage.setTitle("Reports");
            stage.setScene(new Scene(root, 900, 600));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCategoryManagement() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CategoryManagementView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Manage Categories");
            stage.setScene(new Scene(root, 450, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}