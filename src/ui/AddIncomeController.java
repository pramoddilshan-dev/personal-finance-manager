package ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Category;
import model.Income;
import model.User;
import repository.CategoryRepository;
import repository.IncomeRepository;

import java.time.LocalDate;
import java.util.List;

public class AddIncomeController {

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

    private final IncomeRepository incomeRepo = new IncomeRepository();
    private final CategoryRepository categoryRepo = new CategoryRepository();

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void initialize() {

        loadCategories();

        saveButton.setOnAction(e -> saveIncome());

        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    private void loadCategories() {

        List<Category> categories = categoryRepo.getCategoriesByType("INCOME");

        categoryComboBox.getItems().addAll(categories);
    }

    private void saveIncome() {

        try {

            double amount = Double.parseDouble(amountField.getText().trim());

            Category category = categoryComboBox.getValue();

            String note = noteField.getText().trim();

            LocalDate date = datePicker.getValue();

            if (category == null || date == null) {
                showAlert("Error", "Please complete all required fields.");
                return;
            }

            Income income = new Income();
            income.setUserId(user.getId());
            income.setCategoryId(category.getId());
            income.setAmount(amount);
            income.setDate(date);
            income.setNote(note);

            boolean success = incomeRepo.addIncome(income);

            if (success) {

                showAlert("Success", "Income added successfully.");

                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();

            } else {

                showAlert("Error", "Failed to save income.");
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