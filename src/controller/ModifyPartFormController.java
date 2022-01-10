package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class ModifyPartFormController implements Initializable {

    private final ControllerUtil controllerUtil = new ControllerUtil();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private RadioButton radioInHouse;

    @FXML
    private RadioButton radioOutsourced;

    @FXML
    private Label transformLabel;

    @FXML
    private TextField idField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField invField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField maxField;

    @FXML
    private TextField transformField;

    @FXML
    private TextField minField;

    @FXML
    void cancelHandler(ActionEvent event) throws IOException {
        controllerUtil.changeScreen(event, "../view/MainForm.fxml");
    }

    @FXML
    void inHouseHandler() {
        transformLabel.setText("Machine ID");
    }

    @FXML
    void outsourcedHandler() {
        transformLabel.setText("Company Name");
    }

    @FXML
    void saveHandler(ActionEvent event) {
        // Check if all non-string fields are filled
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            Double price = Double.parseDouble(priceField.getText().trim());
            int inv = Integer.parseInt(invField.getText().trim());
            int max = Integer.parseInt(maxField.getText().trim());
            int min = Integer.parseInt(minField.getText().trim());
            String transform = transformField.getText().trim();

            // Check for non empty name
            if (name.length() == 0) {
                controllerUtil.errorPopup("Error", "Name is empty.", "Please ensure the name field is filled.");
                return;
            }

            // Check for valid inv, min, and max values
            else if (!controllerUtil.checkInvLevel(min, max, inv) || !controllerUtil.checkMinMax(min, max)) {
                controllerUtil.errorPopup("Error", "Invalid value for Min, Max, or Inventory.",
                        "Please check that min is less than max, and inventory is between min and max.");
                return;
            }

            // Change validation check based on radio button selected
            if (radioInHouse.isSelected()) {
                // Check if machineID is a number
                try {
                    int machineId = Integer.parseInt(transform);
                    InHouse newPart = new InHouse(id, name, price, inv, min, max, machineId);
                    Inventory.updatePart(newPart);
                } catch (Exception e) {
                    controllerUtil.errorPopup("Error", "Invalid MachineID", "Please ensure MachineID is a number.");
                    return;
                }
            } else {
                // Check if company name has any value
                if (transform.length() == 0) {
                    controllerUtil.errorPopup("Error", "Invalid Company Name", "Please ensure Company Name is filled.");
                    return;
                }

                Outsourced newPart = new Outsourced(id, name, price, inv, min, max, transform);
                Inventory.updatePart(newPart);
            }

            controllerUtil.changeScreen(event, "../view/MainForm.fxml");
        } catch (Exception e) {
            controllerUtil.errorPopup("Error", "Empty/Invalid Values.", "Please ensure all text fields are filled with valid values.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Part selectedPart = MainFormController.getSelectedPart();

        idField.setText(Integer.toString(selectedPart.getId()));
        nameField.setText(selectedPart.getName());
        invField.setText(Integer.toString(selectedPart.getStock()));
        priceField.setText(Double.toString(selectedPart.getPrice()));
        maxField.setText(Integer.toString(selectedPart.getMax()));
        minField.setText(Integer.toString(selectedPart.getMin()));

        if (selectedPart instanceof Outsourced) {
            Outsourced tempPart = (Outsourced) selectedPart;
            transformField.setText(tempPart.getCompanyName());

            radioOutsourced.setSelected(true);
            transformLabel.setText("Company Name");
        } else if (selectedPart instanceof InHouse) {
            InHouse tempPart = (InHouse) selectedPart;
            transformField.setText(Integer.toString(tempPart.getMachineID()));

            radioInHouse.setSelected(true);
            transformLabel.setText("Machine ID");
        }
    }
}