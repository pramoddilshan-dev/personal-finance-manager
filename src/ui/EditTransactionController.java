package ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Category;
import model.Transaction;
import repository.CategoryRepository;
import repository.IncomeRepository;
import repository.ExpenseRepository;

import java.util.List;

public class EditTransactionController {

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    private TextField noteField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Transaction transaction;

    private final CategoryRepository categoryRepo = new CategoryRepository();
    private final IncomeRepository incomeRepo = new IncomeRepository();
    private final ExpenseRepository expenseRepo = new ExpenseRepository();

    public void setTransaction(Transaction transaction) {

        this.transaction = transaction;

        amountField.setText(String.valueOf(transaction.getAmount()));
        noteField.setText(transaction.getNote());
        datePicker.setValue(transaction.getDate());

        loadCategories(transaction.getType());
    }

    private void loadCategories(String type) {

        List<Category> categories = categoryRepo.getCategoriesByType(type.toUpperCase());

        categoryComboBox.setItems(FXCollections.observableArrayList(categories));

        categoryComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        });

        categoryComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        });
    }

    @FXML
    public void initialize() {

        saveButton.setOnAction(e -> updateTransaction());

        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    private void updateTransaction() {

        try {

            double amount = Double.parseDouble(amountField.getText());

            Category category = categoryComboBox.getValue();

            if (category == null) {
                showAlert("Error", "Please select a category.");
                return;
            }

            if (transaction.getType().equals("Income")) {

                incomeRepo.updateIncome(
                        transaction.getId(),
                        amount,
                        category.getId(),
                        datePicker.getValue(),
                        noteField.getText()
                );

            } else {

                expenseRepo.updateExpense(
                        transaction.getId(),
                        amount,
                        category.getId(),
                        datePicker.getValue(),
                        noteField.getText()
                );
            }

            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            showAlert("Error", "Invalid input.");
        }
    }

    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}