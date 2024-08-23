package com.supplyManagement.SupplyManagement.service;

import com.supplyManagement.SupplyManagement.exception.SupplierAdditionException;
import com.supplyManagement.SupplyManagement.exception.SupplierNotFoundException;
import com.supplyManagement.SupplyManagement.model.Supplier;
import com.supplyManagement.SupplyManagement.model.dto.AddSupplierDTO;
import com.supplyManagement.SupplyManagement.model.enums.ManufacturingProcess;
import com.supplyManagement.SupplyManagement.model.enums.NatureOfBusiness;
import com.supplyManagement.SupplyManagement.repository.SupplierRepository;
import com.supplyManagement.SupplyManagement.util.Converters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class SupplierServiceIMPL implements SupplierService {


    @Autowired
    private SupplierRepository supplierRepository;
    @Override
    public Page<Supplier> querySuppliers(String location, NatureOfBusiness natureOfBusiness, ManufacturingProcess manufacturingProcess, int page, int size) {
        try {
            log.info("Querying suppliers with Location: {}, NatureOfBusiness: {}, ManufacturingProcess: {}, Page: {}, Size: {}",
                    location, natureOfBusiness, manufacturingProcess, page, size);

            Pageable pageable = PageRequest.of(page, size);
            Page<Supplier> suppliersPage = supplierRepository.findByLocationAndNatureOfBusinessAndManufacturingProcessesIn(
                    location,
                    natureOfBusiness.name(),
                    List.of(manufacturingProcess.name()),
                    pageable
            );

            if (suppliersPage.isEmpty()) {
                throw new SupplierNotFoundException("No suppliers found for Location: " + location);
            }

            log.debug("Suppliers returned: {}", suppliersPage.getContent());
            return suppliersPage;
        } catch (Exception e) {
            log.error("Error querying suppliers");
            throw e; // Re-throw to be handled by controller
        }
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        try {
            log.info("Fetching all suppliers");
            List<Supplier> suppliers = supplierRepository.findAll();

            if (suppliers.isEmpty()) {
                log.info("No suppliers found");
                return new ArrayList<>(); // Return an empty list
            }

            log.debug("Found {} supplier(s)", suppliers.size());
            return suppliers;
        } catch (Exception e) {
            log.error("Error occurred while fetching suppliers");
            throw e; // Re-throw the exception to be handled by global exception handler or caught higher up
        }
    }
    @Override
    public String addSupplier(AddSupplierDTO addSupplierDTO) {
        try {
            log.info("Adding a new supplier with details: {}", addSupplierDTO);

            Supplier supplier1 = Converters.convertToSupplier(addSupplierDTO);
            supplierRepository.save(supplier1);

            log.info("Supplier added successfully with ID: {}", supplier1.getSupplierId());

            return "Supplier added successfully";
        } catch (Exception e) {
            log.error("Error adding supplier");
            throw new SupplierAdditionException("Failed to add supplier");
        }
    }
}