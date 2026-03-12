package ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Expense;
import model.Income;
import model.User;
import repository.ExpenseRepository;
import repository.IncomeRepository;

import java.util.List;

public class ReportsController {

    @FXML
    private Label incomeLabel;

    @FXML
    private Label expenseLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private PieChart expensePieChart;

    @FXML
    private BarChart<String, Number> incomeExpenseChart;

    @FXML
    private Button closeButton;

    private final IncomeRepository incomeRepo = new IncomeRepository();
    private final ExpenseRepository expenseRepo = new ExpenseRepository();

    private User user;

    public void setUser(User user) {
        this.user = user;
        loadReportData();
    }

    @FXML
    public void initialize() {

        closeButton.setOnAction(e -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }

    private void loadReportData() {

        List<Income> incomes = incomeRepo.getIncomeByUser(user.getId());
        List<Expense> expenses = expenseRepo.getExpensesByUser(user.getId());

        double totalIncome = 0;
        double totalExpense = 0;

        for (Income income : incomes) {
            totalIncome += income.getAmount();
        }

        for (Expense expense : expenses) {
            totalExpense += expense.getAmount();
        }

        double balance = totalIncome - totalExpense;

        incomeLabel.setText(String.format("$%.2f", totalIncome));
        expenseLabel.setText(String.format("$%.2f", totalExpense));
        balanceLabel.setText(String.format("$%.2f", balance));

        loadExpensePieChart(expenses);
        loadBarChart(incomes, expenses);
    }

    private void loadExpensePieChart(List<Expense> expenses) {

        double total = 0;

        for (Expense expense : expenses) {
            total += expense.getAmount();
        }

        PieChart.Data data = new PieChart.Data("Expenses", total);

        expensePieChart.setData(FXCollections.observableArrayList(data));
    }

    private void loadBarChart(List<Income> incomes, List<Expense> expenses) {

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");

        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");

        for (Income income : incomes) {

            incomeSeries.getData().add(
                    new XYChart.Data<>(income.getDate().toString(), income.getAmount())
            );
        }

        for (Expense expense : expenses) {

            expenseSeries.getData().add(
                    new XYChart.Data<>(expense.getDate().toString(), expense.getAmount())
            );
        }

        incomeExpenseChart.getData().addAll(incomeSeries, expenseSeries);
    }
}