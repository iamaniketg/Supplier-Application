package com.supplyManagement.SupplyManagement.service;

import com.supplyManagement.SupplyManagement.model.Supplier;
import com.supplyManagement.SupplyManagement.model.dto.AddSupplierDTO;
import com.supplyManagement.SupplyManagement.model.enums.ManufacturingProcess;
import com.supplyManagement.SupplyManagement.model.enums.NatureOfBusiness;
import com.supplyManagement.SupplyManagement.repository.SupplierRepository;
import com.supplyManagement.SupplyManagement.util.Converters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@Slf4j
public class SupplierServiceIMPL implements SupplierService {


    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public List<Supplier> querySuppliers(String location, NatureOfBusiness natureOfBusiness, ManufacturingProcess manufacturingProcess) {
        log.info("Querying suppliers with Location: {}, NatureOfBusiness: {}, ManufacturingProcess: {}",
                location, natureOfBusiness, manufacturingProcess);

        List<Supplier> suppliers = supplierRepository.findByLocationAndNatureOfBusinessAndManufacturingProcessesIn(
                location,
                natureOfBusiness.name(),
                List.of(manufacturingProcess.name())
        );
        if (suppliers.isEmpty()) {
            log.warn("No suppliers found for Location: {}, NatureOfBusiness: {}, ManufacturingProcess: {}",
                    location, natureOfBusiness, manufacturingProcess);
        } else {
            log.info("Found {} supplier(s) matching the criteria", suppliers.size());
        }
        log.debug("Suppliers returned: {}", suppliers);
        return suppliers;
    }

    @Override
    public String addSupplier(AddSupplierDTO addSupplierDTO) {
        log.info("Adding a new supplier with details: {}", addSupplierDTO);

        Supplier supplier1 = Converters.convertToSupplier(addSupplierDTO);
        supplierRepository.save(supplier1);

        log.info("Supplier added successfully with ID: {}", supplier1.getSupplierId());

        return "Supplier added successfully";
    }
}