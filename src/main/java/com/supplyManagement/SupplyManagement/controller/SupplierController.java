package com.supplyManagement.SupplyManagement.controller;

import com.supplyManagement.SupplyManagement.model.Supplier;
import com.supplyManagement.SupplyManagement.model.dto.AddSupplierDTO;
import com.supplyManagement.SupplyManagement.model.dto.UpdateSupplierDTO;
import com.supplyManagement.SupplyManagement.model.enums.ManufacturingProcess;
import com.supplyManagement.SupplyManagement.model.enums.NatureOfBusiness;
import com.supplyManagement.SupplyManagement.service.SupplierService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;


@RestController
@Validated
public class SupplierController {

    private static final Logger logger = LoggerFactory.getLogger(SupplierController.class);

    @Autowired
    private SupplierService supplierService;
/*
Post API to fetch data regarding to supplier.
 */
@PostMapping("/query")
public ResponseEntity<List<Supplier>> querySuppliers(
        @RequestParam(required = false) String location,
        @RequestParam(required = false) String natureOfBusiness,  // Change to String
        @RequestParam(required = false) String manufacturingProcess,  // Change to String
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    logger.info("Received query request: location={}, natureOfBusiness={}, manufacturingProcess={}, page={}, size={}",
            location, natureOfBusiness, manufacturingProcess, page, size);

    // Convert the strings to enum values if present
    NatureOfBusiness natureOfBusinessEnum = null;
    if (natureOfBusiness != null) {
        try {
            natureOfBusinessEnum = NatureOfBusiness.valueOf(natureOfBusiness.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid NatureOfBusiness value: " + natureOfBusiness);
        }
    }

    ManufacturingProcess manufacturingProcessEnum = null;
    if (manufacturingProcess != null) {
        try {
            manufacturingProcessEnum = ManufacturingProcess.valueOf(manufacturingProcess.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ManufacturingProcess value: " + manufacturingProcess);
        }
    }

    Page<Supplier> suppliersPage = supplierService.querySuppliers(location, natureOfBusinessEnum, manufacturingProcessEnum, page, size);
    List<Supplier> suppliers = suppliersPage.getContent();
    if (suppliers.isEmpty()) {
        logger.warn("No suppliers found for the given criteria");
    }
    return ResponseEntity.ok(suppliers);
}

    /*
PostAPI To add supplier
 */
    @PostMapping("/add")
    public ResponseEntity<String> addSupplier(@Valid @RequestBody AddSupplierDTO addSupplierDTO) {
        logger.info("Received request to add supplier: {}", addSupplierDTO);
        String supplier = supplierService.addSupplier(addSupplierDTO);
        logger.info("Supplier added successfully with ID: {}", supplier);
        return ResponseEntity.ok("Supplier added successfully");
    }
/*
Get api to Fetch all supplier
 */
    @GetMapping("/all")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }
/*
Updating particular fields using patch api
 */
    @PatchMapping("/{supplierId}")
    public ResponseEntity<Supplier> updateSupplier(
            @PathVariable String supplierId,
            @RequestBody UpdateSupplierDTO updateSupplierDTO) {
        Supplier updatedSupplier = supplierService.updateSupplier(supplierId, updateSupplierDTO);
        return ResponseEntity.ok(updatedSupplier);
    }
/*
Delete api to remove supplier
 */
    @DeleteMapping("/{supplierId}")
    public ResponseEntity<String> deleteSupplier(@PathVariable String supplierId) {
        supplierService.deleteSupplier(supplierId);
        return ResponseEntity.ok("Supplier deleted successfully");
    }
}
