package ui;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Category;
import repository.CategoryRepository;

import java.util.List;

public class CategoryManagementController {

    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private TableColumn<Category, Integer> idColumn;

    @FXML
    private TableColumn<Category, String> nameColumn;

    @FXML
    private TableColumn<Category, String> typeColumn;

    @FXML
    private TextField categoryNameField;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button closeButton;

    private final CategoryRepository categoryRepo = new CategoryRepository();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));

        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        typeColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));

        typeComboBox.getItems().addAll("INCOME", "EXPENSE");
        typeComboBox.setValue("EXPENSE");

        loadCategories();

        addButton.setOnAction(e -> addCategory());
        deleteButton.setOnAction(e -> deleteCategory());

        closeButton.setOnAction(e -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
    }

    private void loadCategories() {

        List<Category> categories = categoryRepo.getAllCategories();
        categoryTable.setItems(FXCollections.observableArrayList(categories));
    }

    private void addCategory() {

        String name = categoryNameField.getText().trim();
        String type = typeComboBox.getValue();

        if (name.isEmpty()) {
            showAlert("Error", "Category name cannot be empty.");
            return;
        }

        Category category = new Category();
        category.setName(name);
        category.setType(type);

        boolean success = categoryRepo.addCategory(category);

        if (success) {
            categoryNameField.clear();
            loadCategories();
        } else {
            showAlert("Error", "Failed to add category.");
        }
    }

    private void deleteCategory() {

        Category selected = categoryTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Error", "Please select a category to delete.");
            return;
        }

        boolean success = categoryRepo.deleteCategory(selected.getId());

        if (success) {
            loadCategories();
        } else {
            showAlert("Error", "Failed to delete category.");
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