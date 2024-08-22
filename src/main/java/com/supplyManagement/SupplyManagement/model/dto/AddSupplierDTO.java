package com.supplyManagement.SupplyManagement.model.dto;

import com.supplyManagement.SupplyManagement.model.enums.ManufacturingProcess;
import com.supplyManagement.SupplyManagement.model.enums.NatureOfBusiness;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class AddSupplierDTO {

    @NotEmpty(message = "Company name is required")
    private String companyName;

    @NotEmpty(message = "Website is required")
    private String website;

    @NotEmpty(message = "Location is required")
    private String location;

    @NotNull(message = "Nature of business is required")
    private NatureOfBusiness natureOfBusiness;

    @NotEmpty(message = "At least one manufacturing process is required")
    private List<ManufacturingProcess> manufacturingProcesses;
}
