package service;

import dataaccess.SupplierDAO;
import model.Supplier;

import java.util.List;

public class SupplierService {

    private SupplierDAO supplierDAO;

    public SupplierService() {
        supplierDAO = new SupplierDAO();
    }

    // Add Supplier
    public void addSupplier(Supplier supplier) {
        supplierDAO.addSupplier();
    }

    // Update Supplier
    public void  updateSupplier(Supplier supplier) {
        supplierDAO.updateSupplier();
    }

    // Delete Supplier
    public void  deleteSupplier(int supplierId) {
         supplierDAO.deleteSupplier();
    }


    // View All Suppliers
    public void  displaySupplier() {
        supplierDAO.displaySupplier();
    }
}