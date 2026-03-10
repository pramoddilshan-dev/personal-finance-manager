package ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import repository.UserRepository;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerLink;

    private final UserRepository userRepo = new UserRepository();

    @FXML
    public void initialize() {

        loginButton.setOnAction(e -> login());
        registerLink.setOnAction(e -> openRegisterScreen());
    }

    private void login() {

        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter email and password.");
            return;
        }

        User user = userRepo.login(email, password);

        if (user != null) {

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/DashboardView.fxml"));
                Parent root = loader.load();

                DashboardController controller = loader.getController();
                controller.setUser(user);

                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root, 700, 500);

                stage.setTitle("Personal Finance Manager - Dashboard");
                stage.setScene(scene);
                stage.centerOnScreen();

            } catch (Exception ex) {

                ex.printStackTrace();

                showAlert("FXML Load Error",
                        "Failed to open Dashboard.\n\nCheck:\n" +
                                "• DashboardView.fxml exists\n" +
                                "• Controller name is correct\n" +
                                "• fx:controller=\"ui.DashboardController\"");
            }

        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }

    private void openRegisterScreen() {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/RegisterView.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) registerLink.getScene().getWindow();

            stage.setScene(new Scene(root, 500, 400));
            stage.setTitle("Register New Account");
            stage.centerOnScreen();

        } catch (Exception e) {

            e.printStackTrace();
            showAlert("Error", "Failed to open registration screen.");
        }
    }

    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}