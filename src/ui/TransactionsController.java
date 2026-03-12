package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Income;
import model.Expense;
import model.Transaction;
import model.User;
import repository.IncomeRepository;
import repository.ExpenseRepository;

import java.util.List;

public class TransactionsController {

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, String> categoryColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    @FXML
    private TableColumn<Transaction, String> noteColumn;

    @FXML
    private Button closeButton;

    private final IncomeRepository incomeRepo = new IncomeRepository();
    private final ExpenseRepository expenseRepo = new ExpenseRepository();

    private User user;

    public void setUser(User user) {
        this.user = user;
        loadTransactions();
    }

    @FXML
    public void initialize() {

        dateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate().toString()));
        typeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        categoryColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));
        amountColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getAmount()));
        noteColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNote()));

        closeButton.setOnAction(e -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }

    private void loadTransactions() {

        ObservableList<Transaction> list = FXCollections.observableArrayList();

        List<Income> incomes = incomeRepo.getIncomeByUser(user.getId());
        List<Expense> expenses = expenseRepo.getExpensesByUser(user.getId());

        for (Income income : incomes) {

            list.add(new Transaction(
                    income.getDate(),
                    "Income",
                    String.valueOf(income.getCategoryId()),
                    income.getAmount(),
                    income.getNote()
            ));
        }

        for (Expense expense : expenses) {

            list.add(new Transaction(
                    expense.getDate(),
                    "Expense",
                    String.valueOf(expense.getCategoryId()),
                    expense.getAmount(),
                    expense.getNote()
            ));
        }

        transactionTable.setItems(list);
    }
}