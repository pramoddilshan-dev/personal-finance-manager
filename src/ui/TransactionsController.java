package ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import model.Income;
import model.Expense;
import model.Transaction;
import model.User;
import model.Category;

import repository.IncomeRepository;
import repository.ExpenseRepository;
import repository.CategoryRepository;

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
    private final CategoryRepository categoryRepo = new CategoryRepository();

    private User user;

    public void setUser(User user) {
        this.user = user;
        loadTransactions();
    }

    @FXML
    public void initialize() {

        dateColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDate().toString()));

        typeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getType()));

        categoryColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCategory()));

        amountColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getAmount()));

        noteColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().getNote() == null ? "" : data.getValue().getNote()
                ));

        // Currency formatting
        amountColumn.setCellFactory(column -> new TableCell<>() {

            @Override
            protected void updateItem(Double amount, boolean empty) {

                super.updateItem(amount, empty);

                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        });

        // Row coloring (Income / Expense)
        transactionTable.setRowFactory(tv -> new TableRow<>() {

            @Override
            protected void updateItem(Transaction item, boolean empty) {

                super.updateItem(item, empty);

                if (item == null || empty) {
                    setStyle("");
                }
                else if ("Income".equals(item.getType())) {
                    setStyle("-fx-background-color: #e8f5e9;");
                }
                else {
                    setStyle("-fx-background-color: #ffebee;");
                }
            }
        });

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
                    getCategoryName(income.getCategoryId()),
                    income.getAmount(),
                    income.getNote()
            ));
        }

        for (Expense expense : expenses) {

            list.add(new Transaction(
                    expense.getDate(),
                    "Expense",
                    getCategoryName(expense.getCategoryId()),
                    expense.getAmount(),
                    expense.getNote()
            ));
        }

        // Sort newest first
        list.sort((t1, t2) -> t2.getDate().compareTo(t1.getDate()));

        transactionTable.setItems(list);
    }

    private String getCategoryName(int categoryId) {

        Category category = categoryRepo.getCategoryById(categoryId);

        if (category != null) {
            return category.getName();
        }

        return "Unknown";
    }
}