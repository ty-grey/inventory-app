package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import javafx.event.ActionEvent;

import java.util.Optional;

public class ControllerUtil {

    public void changeScreen(ActionEvent event, String path) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(path));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void errorPopup(String title, String header, String text) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public boolean confirmPopup(String title, String header, String text) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        Optional<ButtonType> button = alert.showAndWait();

        return button.get() == ButtonType.OK;
    }

    public void infoPopup(String title, String header, String text) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public boolean checkInvLevel(int min, int max, int inv) {
        // Ensure min is less than inv and max, max is more than inv and min
        return min < inv && inv < max;
    }

    public boolean checkMinMax(int min, int max) {
        // Ensure min is less than max, and both are not negative
        return min < max && min >= 0;
    }

}