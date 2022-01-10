package controller;

import javafx.application.Platform;
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

public class MainFormController implements Initializable {

    private static Part selectedPart;
    private static Product selectedProduct;
    private final ControllerUtil controllerUtil = new ControllerUtil();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField searchPartText;

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
    private TextField searchProductText;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<?, ?> productID;

    @FXML
    private TableColumn<?, ?> productName;

    @FXML
    private TableColumn<?, ?> productInventory;

    @FXML
    private TableColumn<?, ?> productCost;

    @FXML
    void addPartHandler(ActionEvent event) {
        controllerUtil.changeScreen(event, "../view/AddPartForm.fxml");
    }

    @FXML
    void addProductHandler(ActionEvent event) {
        controllerUtil.changeScreen(event, "../view/AddProductForm.fxml");
    }

    @FXML
    void deletePartHandler() {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            controllerUtil.infoPopup("Info", "You need to select a part to delete.", "No part was selected.");
            return;
        }

        if (controllerUtil.confirmPopup("Confirmation", "Are you sure you want to delete this part?",
                selectedPart.getName() + " has been selected for deletion.")) {
            Inventory.deletePart(selectedPart);
            displayTables();
        }
    }

    @FXML
    void exitHandler() {
        if (controllerUtil.confirmPopup("Confirmation", "Are you sure you want to exit?",
                "")) {
            Platform.exit();
        }
    }

    @FXML
    void modifyPartHandler(ActionEvent event) {
        selectedPart = partTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            controllerUtil.infoPopup("Info", "You need to select a part to modify.", "");
            return;
        }

        controllerUtil.changeScreen(event, "../view/ModifyPartForm.fxml");
    }

    @FXML
    void deleteProductHandler() {
        selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            controllerUtil.infoPopup("Info", "You need to select a product to delete.", "No product was selected.");
            return;
        }

        if (selectedProduct.getAllAssociatedParts().size() != 0) {
            controllerUtil.errorPopup("Error", "You cannot delete a product with associated parts.",
                    selectedProduct.getName() + " has associated parts.");
            return;
        }

        if (controllerUtil.confirmPopup("Confirmation", "Are you sure you want to delete this product?",
                selectedProduct.getName() + " has been selected for deletion.")) {
            Inventory.deleteProduct(selectedProduct);
            displayTables();
        }
    }

    @FXML
    void modifyProductHandler(ActionEvent event) {
        selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            controllerUtil.infoPopup("Info", "You need to select a product to modify.", "No product was selected.");
            return;
        }

        controllerUtil.changeScreen(event, "../view/ModifyProductForm.fxml");
    }

    @FXML
    void searchPartHandler() {
        partTable.setItems(Inventory.lookupPartName(searchPartText.getText().trim()));
    }

    @FXML
    void searchProductHandler() {
        productTable.setItems(Inventory.lookupProductName(searchProductText.getText().trim()));
    }

    private void displayTables() {
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTable.setItems(Inventory.getPartInventory());

        productID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventory.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCost.setCellValueFactory(new PropertyValueFactory<>("price"));
        productTable.setItems(Inventory.getProductInventory());
    }

    public static Part getSelectedPart() {
        return selectedPart;
    }

    public static Product getSelectedProduct() {
        return selectedProduct;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedPart = null;
        selectedProduct = null;
        displayTables();
    }
}
