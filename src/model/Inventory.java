package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    private static final ObservableList<Part> partInventory = FXCollections.observableArrayList();
    private static final ObservableList<Product> productInventory = FXCollections.observableArrayList();
    private static int partIdGen = 0;
    private static int productIdGen = 0;

    public static void addPart(Part part) {
        partInventory.add(part);
    }

    public static void addProduct(Product product) {
        productInventory.add(product);
    }

    public static Part lookupPart(int id) {
        for (Part part : partInventory) {
            if (part.getId() == id) {
                return part;
            }
        }
        return null;
    }

    public static Product lookupProduct(int id) {
        for (Product product : productInventory) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    public static ObservableList<Part> lookupPartName(String searchTerm) {
        ObservableList<Part> foundList = FXCollections.observableArrayList();

        for (Part part : partInventory) {
            if (Integer.toString(part.getId()).contains(searchTerm)
                    || part.getName().contains(searchTerm)) {
                foundList.add(part);
            }
        }
        return foundList;
    }

    public static ObservableList<Product> lookupProductName(String searchTerm) {
        ObservableList<Product> foundList = FXCollections.observableArrayList();

        for (Product product : productInventory) {
            if (Integer.toString(product.getId()).contains(searchTerm) || product.getName().contains(searchTerm)) {
                foundList.add(product);
            }
        }
        return foundList;
    }

    public static void updatePart(Part part) {
        for (int i = 0; i < partInventory.size(); i++) {
            if (partInventory.get(i).getId() == part.getId()) {
                partInventory.set(i, part);
            }
        }
    }

    public static void updateProduct(Product product) {
        for (int i = 0; i < productInventory.size(); i++) {
            if (productInventory.get(i).getId() == product.getId()) {
                productInventory.set(i, product);
            }
        }
    }

    public static boolean deletePart(Part part) {
        if (partInventory.contains(part)) {
            partInventory.remove(part);
            for (Product product : productInventory) {
                if (product.getAllAssociatedParts().contains(part)) {
                    product.deleteAssociatedPart(part);
                }
            }
            return true;
        }
        return false;
    }

    public static boolean deleteProduct(Product product) {
        if (productInventory.contains(product)) {
            productInventory.remove(product);
            return true;
        }
        return false;
    }

    public static int generatePartId() {
        partIdGen++;
        return partIdGen;
    }

    public static int generateProductId() {
        productIdGen++;
        return productIdGen;
    }

    public static ObservableList<Part> getPartInventory() {
        return partInventory;
    }

    public static ObservableList<Product> getProductInventory() {
        return productInventory;
    }
}
