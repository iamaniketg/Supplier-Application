package com.supplyManagement.SupplyManagement.model.dto;

import com.supplyManagement.SupplyManagement.model.enums.ManufacturingProcess;
import com.supplyManagement.SupplyManagement.model.enums.NatureOfBusiness;
import com.supplyManagement.SupplyManagement.util.UniqueMongoField;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSupplierDTO {

    @UniqueMongoField(fieldName = "companyName", message = "Company name must be unique")
    private String companyName;

    @UniqueMongoField(fieldName = "website", message = "Website must be unique")
    private String website;

    @UniqueMongoField(fieldName = "location", message = "Location must be unique")
    private String location;

    @NotNull(message = "Nature of business is required")
    private NatureOfBusiness natureOfBusiness;

    @NotEmpty(message = "At least one manufacturing process is required")
    private List<ManufacturingProcess> manufacturingProcesses;
}
