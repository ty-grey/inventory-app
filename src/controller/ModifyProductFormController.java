package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Inventory;
import model.Part;
import model.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class ModifyProductFormController implements Initializable {

    private ObservableList<Part> allAssocParts = FXCollections.observableArrayList();
    private final ControllerUtil controllerUtil = new ControllerUtil();

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
    private TextField minField;

    @FXML
    private TableView<Part> partTable;

    @FXML
    private TableColumn<?, ?> partID;

    @FXML
    private TableColumn<?, ?> partName;

    @FXML
    private TableColumn<?, ?> partInventory;

    @FXML
    private TableColumn<?, ?> partCost;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Part> assocTable;

    @FXML
    private TableColumn<?, ?> assocID;

    @FXML
    private TableColumn<?, ?> assocName;

    @FXML
    private TableColumn<?, ?> assocInventory;

    @FXML
    private TableColumn<?, ?> assocCost;

    @FXML
    void addHandler() {
        allAssocParts.add(partTable.getSelectionModel().getSelectedItem());
        refreshAssocTable();
    }

    @FXML
    void cancelHandler(ActionEvent event) {
        controllerUtil.changeScreen(event, "../view/MainForm.fxml");
    }

    @FXML
    void removeHandler() {
        Part selectedAssoc = assocTable.getSelectionModel().getSelectedItem();

        if (selectedAssoc == null) {
            controllerUtil.infoPopup("Info", "You need to select a part to delete.", "No part was selected.");
            return;
        }

        if (controllerUtil.confirmPopup("Confirmation", "Are you sure you want to delete this product?",
                selectedAssoc.getName() + " has been selected for deletion.")) {
            allAssocParts.remove(selectedAssoc);
            refreshAssocTable();
        }
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

            // Check for non empty name
            if (name.length() == 0) {
                controllerUtil.errorPopup("Error", "Name is empty.", "Please ensure the name field is filled.");
                return;
            }

            // Check for valid inv, min, and max values
            if (!controllerUtil.checkInvLevel(min, max, inv) || !controllerUtil.checkMinMax(min, max)) {
                controllerUtil.errorPopup("Error", "Invalid value for Min, Max, or Inventory.",
                        "Please check that min is less than max, and inventory is between min and max.");
                return;
            }

            Product newProduct = new Product(id, name, price, inv, min, max);
            for (Part allAssoc : allAssocParts) {
                newProduct.addAssociatedParts(allAssoc);
            }

            Inventory.updateProduct(newProduct);
            controllerUtil.changeScreen(event, "../view/MainForm.fxml");
        } catch (Exception e) {
            controllerUtil.errorPopup("Error", "Empty/Invalid Values.", "Please ensure all text fields are filled with valid values.");
        }
    }

    @FXML
    void searchHandler() {
        partTable.setItems(Inventory.lookupPartName(searchField.getText().trim()));
    }

    private void refreshAssocTable() {
        assocTable.setItems(allAssocParts);
    }

    private void displayTables() {
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTable.setItems(Inventory.getPartInventory());

        assocID.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        assocTable.setItems(allAssocParts);
    }

    private void setFields(Product product) {
        idField.setText(Integer.toString(product.getId()));
        nameField.setText(product.getName());
        invField.setText(Integer.toString(product.getStock()));
        priceField.setText(Double.toString(product.getPrice()));
        maxField.setText(Integer.toString(product.getMax()));
        minField.setText(Integer.toString(product.getMin()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Product selectedProduct = MainFormController.getSelectedProduct();
        allAssocParts = selectedProduct.getAllAssociatedParts();
        displayTables();
        setFields(selectedProduct);
    }
}