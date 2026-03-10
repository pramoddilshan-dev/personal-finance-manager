package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;
import repository.UserRepository;

public class RegisterController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button registerButton;

    private UserRepository userRepo = new UserRepository();

    @FXML
    public void initialize() {
        registerButton.setOnAction(e -> register());
    }

    private void register() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
            showAlert("Error", "All fields are required!");
            return;
        }

        // Check if email already exists
        if(userRepo.existsByEmail(email)){
            showAlert("Error", "Email already registered!");
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        if(userRepo.register(user)){
            showAlert("Success", "Registration successful!");
            // TODO: Go back to Login screen
        } else {
            showAlert("Error", "Registration failed!");
        }
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}