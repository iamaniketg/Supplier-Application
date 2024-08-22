package com.supplyManagement.SupplyManagement.util;

import com.supplyManagement.SupplyManagement.model.Supplier;
import com.supplyManagement.SupplyManagement.model.dto.AddSupplierDTO;

public class Converters {
    public static Supplier convertToSupplier(AddSupplierDTO addSupplierDTO) {
        Supplier supplier = new Supplier();
        supplier.setCompanyName(addSupplierDTO.getCompanyName());
        supplier.setWebsite(addSupplierDTO.getWebsite());
        supplier.setLocation(addSupplierDTO.getLocation());
        supplier.setNatureOfBusiness(addSupplierDTO.getNatureOfBusiness());
        supplier.setManufacturingProcesses(addSupplierDTO.getManufacturingProcesses());

        return supplier;
    }


}
