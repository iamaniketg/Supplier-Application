package com.supplyManagement.SupplyManagement.repository;
import com.supplyManagement.SupplyManagement.model.Supplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, String> {
    Page<Supplier> findByLocationAndNatureOfBusinessAndManufacturingProcessesIn(
            String location,
            String natureOfBusiness,
            List<String> manufacturingProcesses,
            Pageable pageable);

}
