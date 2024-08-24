package com.supplyManagement.SupplyManagement.service;

import com.supplyManagement.SupplyManagement.model.Supplier;
import com.supplyManagement.SupplyManagement.model.dto.AddSupplierDTO;
import com.supplyManagement.SupplyManagement.model.dto.UpdateSupplierDTO;
import com.supplyManagement.SupplyManagement.model.enums.ManufacturingProcess;
import com.supplyManagement.SupplyManagement.model.enums.NatureOfBusiness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
public interface SupplierService {


    String addSupplier(AddSupplierDTO addSupplierDTO);

    Page<Supplier> querySuppliers(String location, NatureOfBusiness natureOfBusiness, ManufacturingProcess manufacturingProcess, int page, int size);

    List<Supplier> getAllSuppliers();

    Supplier updateSupplier(String supplierId, UpdateSupplierDTO updateSupplierDTO);

    void deleteSupplier(String supplierId);
}
