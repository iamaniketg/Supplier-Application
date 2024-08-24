package com.supplyManagement.SupplyManagement.model.dto;

import com.supplyManagement.SupplyManagement.model.enums.ManufacturingProcess;
import com.supplyManagement.SupplyManagement.model.enums.NatureOfBusiness;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSupplierDTO {
    private String companyName;
    private String website;
    private String location;
    private NatureOfBusiness natureOfBusiness;
    private List<ManufacturingProcess> manufacturingProcesses;

}
