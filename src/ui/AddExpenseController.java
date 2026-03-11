package ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Category;
import model.Expense;
import model.User;
import repository.CategoryRepository;
import repository.ExpenseRepository;

import java.time.LocalDate;
import java.util.List;

public class AddExpenseController {

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

    private User user;

    private final ExpenseRepository expenseRepo = new ExpenseRepository();
    private final CategoryRepository categoryRepo = new CategoryRepository();

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void initialize() {

        loadCategories();

        saveButton.setOnAction(e -> saveExpense());

        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    private void loadCategories() {

        List<Category> categories = categoryRepo.getCategoriesByType("expense");

        categoryComboBox.getItems().addAll(categories);
    }

    private void saveExpense() {

        try {

            double amount = Double.parseDouble(amountField.getText().trim());

            Category category = categoryComboBox.getValue();

            String note = noteField.getText().trim();

            LocalDate date = datePicker.getValue();

            if (category == null || date == null) {
                showAlert("Error", "Please complete all required fields.");
                return;
            }

            Expense expense = new Expense();
            expense.setUserId(user.getId());
            expense.setCategoryId(category.getId());
            expense.setAmount(amount);
            expense.setDate(date);
            expense.setNote(note);

            boolean success = expenseRepo.addExpense(expense);

            if (success) {

                showAlert("Success", "Expense added successfully.");

                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();

            } else {

                showAlert("Error", "Failed to save expense.");
            }

        } catch (NumberFormatException e) {

            showAlert("Input Error", "Amount must be a number.");

        } catch (Exception e) {

            e.printStackTrace();
            showAlert("Error", "Unexpected error occurred.");
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