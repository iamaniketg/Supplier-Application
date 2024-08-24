package com.supplyManagement.SupplyManagement.service;

import com.supplyManagement.SupplyManagement.exception.SupplierAdditionException;
import com.supplyManagement.SupplyManagement.exception.SupplierNotFoundException;
import com.supplyManagement.SupplyManagement.model.Supplier;
import com.supplyManagement.SupplyManagement.model.dto.AddSupplierDTO;
import com.supplyManagement.SupplyManagement.model.dto.UpdateSupplierDTO;
import com.supplyManagement.SupplyManagement.model.enums.ManufacturingProcess;
import com.supplyManagement.SupplyManagement.model.enums.NatureOfBusiness;
import com.supplyManagement.SupplyManagement.repository.SupplierRepository;
import com.supplyManagement.SupplyManagement.util.Converters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


@Service
@Slf4j
public class SupplierServiceIMPL implements SupplierService {


    @Autowired
    private SupplierRepository supplierRepository;



    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Page<Supplier> querySuppliers(String location, NatureOfBusiness natureOfBusiness, ManufacturingProcess manufacturingProcess, int page, int size) {
        try {
            log.info("Querying suppliers with Location: {}, NatureOfBusiness: {}, ManufacturingProcess: {}, Page: {}, Size: {}",
                    location, natureOfBusiness, manufacturingProcess, page, size);

            Query query = new Query();

            if (location != null) {
                query.addCriteria(Criteria.where("location").is(location));
            }
            if (natureOfBusiness != null) {
                query.addCriteria(Criteria.where("natureOfBusiness").is(natureOfBusiness));
            }
            if (manufacturingProcess != null) {
                query.addCriteria(Criteria.where("manufacturingProcesses").in(manufacturingProcess));
            }

            long total = mongoTemplate.count(query, Supplier.class);
            Pageable pageable = PageRequest.of(page, size);
            query.with(pageable);

            List<Supplier> suppliers = mongoTemplate.find(query, Supplier.class);

            if (suppliers.isEmpty()) {
                throw new SupplierNotFoundException("No suppliers found for the given criteria");
            }

            log.debug("Suppliers returned: {}", suppliers);
            return new PageImpl<>(suppliers, pageable, total);

        } catch (Exception e) {
            log.error("Error querying suppliers {}", e.getMessage());
            throw e;
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
            throw e;
        }
    }
    @Override
    public Supplier updateSupplier(String supplierId, UpdateSupplierDTO updateSupplierDTO) {
        try {
            log.info("Updating supplier with ID: {}", supplierId);

            Supplier supplier = supplierRepository.findById(supplierId)
                    .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with ID: " + supplierId));

            // Update only the fields provided
            if (updateSupplierDTO.getCompanyName() != null) {
                supplier.setCompanyName(updateSupplierDTO.getCompanyName());
            }
            if (updateSupplierDTO.getWebsite() != null) {
                supplier.setWebsite(updateSupplierDTO.getWebsite());
            }
            if (updateSupplierDTO.getLocation() != null) {
                supplier.setLocation(updateSupplierDTO.getLocation());
            }
            if (updateSupplierDTO.getNatureOfBusiness() != null) {
                supplier.setNatureOfBusiness(updateSupplierDTO.getNatureOfBusiness());
            }
            if (updateSupplierDTO.getManufacturingProcesses() != null) {
                supplier.setManufacturingProcesses(updateSupplierDTO.getManufacturingProcesses());
            }

            supplierRepository.save(supplier);

            log.info("Supplier updated successfully with ID: {}", supplier.getSupplierId());
            return supplier;
        } catch (Exception e) {
            log.error("Error occurred while updating supplier");
            throw e;
        }
    }

    @Override
    public void deleteSupplier(String supplierId) {
        try {
            log.info("Attempting to delete supplier with ID: {}", supplierId);

            Supplier supplier = supplierRepository.findById(supplierId)
                    .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with ID: " + supplierId));

            supplierRepository.delete(supplier);

            log.info("Supplier deleted successfully with ID: {}", supplierId);
        } catch (Exception e) {
            log.error("Error occurred while deleting supplier");
            throw e;
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