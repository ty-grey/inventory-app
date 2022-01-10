package main;

import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        primaryStage.setTitle("Inventory System");
        primaryStage.setScene(new Scene(root, 1200, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {

        // Test information
        InHouse inHouseOne = new InHouse(Inventory.generatePartId(), "Gear", 21.00, 5, 3, 11, 58);
        InHouse inHouseTwo = new InHouse(Inventory.generatePartId(), "Light", 20.00, 5, 1, 10, 46);
        Outsourced outsourcedOne = new Outsourced(Inventory.generatePartId(), "Cap", 25.00, 4, 3, 7, "CompanyName");

        Inventory.addPart(inHouseOne);
        Inventory.addPart(inHouseTwo);
        Inventory.addPart(outsourcedOne);

        Product productOne = new Product(Inventory.generateProductId(), "Clock", 43.00, 5, 3, 11);
        productOne.addAssociatedParts(inHouseOne);
        productOne.addAssociatedParts(inHouseTwo);
        productOne.addAssociatedParts(outsourcedOne);
        Product productTwo = new Product(Inventory.generateProductId(), "Television", 39.00, 4, 1, 48);

        Inventory.addProduct(productOne);
        Inventory.addProduct(productTwo);

        launch(args);
    }
}
