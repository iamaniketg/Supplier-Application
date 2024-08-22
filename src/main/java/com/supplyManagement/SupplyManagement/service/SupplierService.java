package com.supplyManagement.SupplyManagement.service;

import com.supplyManagement.SupplyManagement.model.Supplier;
import com.supplyManagement.SupplyManagement.model.dto.AddSupplierDTO;
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

    List<Supplier> querySuppliers(String location, NatureOfBusiness natureOfBusiness, ManufacturingProcess manufacturingProcess);
}
