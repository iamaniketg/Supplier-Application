package com.supplyManagement.SupplyManagement.controller;

import com.supplyManagement.SupplyManagement.model.Supplier;
import com.supplyManagement.SupplyManagement.model.dto.AddSupplierDTO;
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

    @PostMapping("/query")
    public ResponseEntity<List<Supplier>> querySuppliers(
            @RequestParam String location,
            @RequestParam NatureOfBusiness natureOfBusiness,
            @RequestParam ManufacturingProcess manufacturingProcess,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Received query request: location={}, natureOfBusiness={}, manufacturingProcess={}, page={}, size={}",
                location, natureOfBusiness, manufacturingProcess, page, size);
        Page<Supplier> suppliersPage = supplierService.querySuppliers(location, natureOfBusiness, manufacturingProcess, page, size);
        List<Supplier> suppliers = suppliersPage.getContent(); // Extract the content
        if (suppliers.isEmpty()) {
            logger.warn("No suppliers found for the given criteria");
        }
        return ResponseEntity.ok(suppliers); // Return only the content
    }

    @PostMapping("/add")
    public ResponseEntity<String> addSupplier(@Valid @RequestBody AddSupplierDTO addSupplierDTO) {
        logger.info("Received request to add supplier: {}", addSupplierDTO);
        String supplier = supplierService.addSupplier(addSupplierDTO);
        logger.info("Supplier added successfully with ID: {}", supplier);
        return ResponseEntity.ok("Supplier added successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }
}
