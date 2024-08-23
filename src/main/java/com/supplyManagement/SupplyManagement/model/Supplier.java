package com.supplyManagement.SupplyManagement.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.supplyManagement.SupplyManagement.model.enums.ManufacturingProcess;
import com.supplyManagement.SupplyManagement.model.enums.NatureOfBusiness;
import com.supplyManagement.SupplyManagement.util.UniqueMongoField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.util.List;



@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "suppliers")
public class Supplier {

    @Id
    private String supplierId;
    @UniqueMongoField(fieldName = "companyName", message = "Company name must be unique")
    private String companyName;

    @UniqueMongoField(fieldName = "website", message = "Website must be unique")
    private String website;

    @UniqueMongoField(fieldName = "location", message = "Location must be unique")
    private String location;
    // Example: "India"
    @Field(targetType = FieldType.STRING)
    private NatureOfBusiness natureOfBusiness; // Possible values: "small_scale", "medium_scale", "large_scale"
    @Field(targetType = FieldType.STRING)
    private List<ManufacturingProcess> manufacturingProcesses; // Example: ["3d_printing", "moulding"]
}