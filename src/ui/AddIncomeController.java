package ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Income;
import model.User;
import repository.IncomeRepository;

import java.time.LocalDate;

public class AddIncomeController {

    @FXML
    private TextField amountField;

    @FXML
    private TextField categoryIdField;

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

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void initialize() {

        saveButton.setOnAction(e -> saveIncome());

        cancelButton.setOnAction(e -> {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        });
    }

    private void saveIncome() {

        try {

            double amount = Double.parseDouble(amountField.getText().trim());
            int categoryId = Integer.parseInt(categoryIdField.getText().trim());
            String note = noteField.getText().trim();
            LocalDate date = datePicker.getValue();

            if (date == null) {
                showAlert("Error", "Please select a date.");
                return;
            }

            Income income = new Income();
            income.setUserId(user.getId());
            income.setCategoryId(categoryId);
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
            showAlert("Input Error", "Amount and Category ID must be numbers.");
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