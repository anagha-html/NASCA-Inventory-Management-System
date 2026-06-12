package service;

import dataaccess.InventoryDAO;
import model.Inventory;

import java.util.List;
import java.util.Scanner;

public class InventoryService {

    private InventoryDAO inventoryDAO;

    public InventoryService() {
        inventoryDAO = new InventoryDAO();
    }

    // Add Item
    public void  addItem() {
         inventoryDAO.addItem();
    }
 // Update Item
    public void updateItem() {
         inventoryDAO.updateItem();
    }

    // Delete Item
    public void deleteItem() {
         inventoryDAO.deleteItem();
    }

    // Search Item
    public void  SearchItemByName() {
         inventoryDAO.SearchItemByName();
    }

    // View All Items
    public void viewItem(){
        inventoryDAO. viewItem();
    }
}