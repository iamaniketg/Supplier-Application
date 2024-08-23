package com.supplyManagement.SupplyManagement.util;

import com.supplyManagement.SupplyManagement.model.Supplier;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class UniqueMongoFieldValidator implements ConstraintValidator<UniqueMongoField, String> {

    @Autowired
    private MongoTemplate mongoTemplate;

    private String fieldName;

    @Override
    public void initialize(UniqueMongoField constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        Query query = new Query();
        query.addCriteria(Criteria.where(fieldName).is(value));

        boolean exists = mongoTemplate.exists(query, Supplier.class);

        if (exists) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(fieldName + " must be unique")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
