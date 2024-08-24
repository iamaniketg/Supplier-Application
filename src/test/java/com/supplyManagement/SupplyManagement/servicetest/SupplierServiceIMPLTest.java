package com.supplyManagement.SupplyManagement.servicetest;

import com.supplyManagement.SupplyManagement.exception.SupplierAdditionException;
import com.supplyManagement.SupplyManagement.exception.SupplierNotFoundException;
import com.supplyManagement.SupplyManagement.model.Supplier;
import com.supplyManagement.SupplyManagement.model.dto.AddSupplierDTO;
import com.supplyManagement.SupplyManagement.model.dto.UpdateSupplierDTO;
import com.supplyManagement.SupplyManagement.model.enums.ManufacturingProcess;
import com.supplyManagement.SupplyManagement.model.enums.NatureOfBusiness;
import com.supplyManagement.SupplyManagement.repository.SupplierRepository;
import com.supplyManagement.SupplyManagement.service.SupplierServiceIMPL;
import com.supplyManagement.SupplyManagement.util.Converters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Profile("dev")
@ExtendWith(MockitoExtension.class)
public class SupplierServiceIMPLTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private SupplierServiceIMPL supplierService;
    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private Converters converters;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testQuerySuppliers_ValidParameters_ReturnsSuppliers() {
        String location = "New York";
        NatureOfBusiness natureOfBusiness = NatureOfBusiness.SMALL_SCALE;
        ManufacturingProcess manufacturingProcess = ManufacturingProcess.MOULDING;
        int page = 0;
        int size = 10;

        Supplier mockSupplier = new Supplier();
        PageRequest pageable = PageRequest.of(page, size);
        List<Supplier> suppliers = List.of(mockSupplier);
        Page<Supplier> supplierPage = new PageImpl<>(suppliers, pageable, 1);

        when(mongoTemplate.find(any(Query.class), eq(Supplier.class))).thenReturn(suppliers);
        when(mongoTemplate.count(any(Query.class), eq(Supplier.class))).thenReturn(1L);

        Page<Supplier> result = supplierService.querySuppliers(location, natureOfBusiness, manufacturingProcess, page, size);

        assertEquals(supplierPage, result);
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Supplier.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(Supplier.class));
    }

    @Test
    public void testQuerySuppliers_NoSuppliersFound_ThrowsException() {
        String location = "New York";
        NatureOfBusiness natureOfBusiness = NatureOfBusiness.SMALL_SCALE;
        ManufacturingProcess manufacturingProcess = ManufacturingProcess.MOULDING;
        int page = 0;
        int size = 10;

        when(mongoTemplate.find(any(Query.class), eq(Supplier.class))).thenReturn(Collections.emptyList());
        when(mongoTemplate.count(any(Query.class), eq(Supplier.class))).thenReturn(0L);

        assertThrows(SupplierNotFoundException.class, () -> {
            supplierService.querySuppliers(location, natureOfBusiness, manufacturingProcess, page, size);
        });

        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Supplier.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(Supplier.class));
    }

    @Test
    public void testQuerySuppliers_NullParameters_ReturnsSuppliers() {
        String location = null;
        NatureOfBusiness natureOfBusiness = null;
        ManufacturingProcess manufacturingProcess = null;
        int page = 0;
        int size = 10;

        Supplier mockSupplier = new Supplier();
        List<Supplier> suppliers = List.of(mockSupplier);
        PageRequest pageable = PageRequest.of(page, size);
        Page<Supplier> supplierPage = new PageImpl<>(suppliers, pageable, 1);

        when(mongoTemplate.find(any(Query.class), eq(Supplier.class))).thenReturn(suppliers);
        when(mongoTemplate.count(any(Query.class), eq(Supplier.class))).thenReturn(1L);

        Page<Supplier> result = supplierService.querySuppliers(location, natureOfBusiness, manufacturingProcess, page, size);

        assertEquals(supplierPage, result);
        verify(mongoTemplate, times(1)).find(any(Query.class), eq(Supplier.class));
        verify(mongoTemplate, times(1)).count(any(Query.class), eq(Supplier.class));
    }

    @Test
    void testGetAllSuppliers_NoSuppliersFound() {
        // Arrange
        when(supplierRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Supplier> result = supplierService.getAllSuppliers();

        // Assert
        assertTrue(result.isEmpty(), "Expected empty list when no suppliers are found");
        verify(supplierRepository, times(1)).findAll();
    }

    @Test
    void testGetAllSuppliers_SuppliersFound() {
        // Arrange
        Supplier supplier1 = new Supplier(); // Set the fields as needed
        Supplier supplier2 = new Supplier(); // Set the fields as needed
        List<Supplier> mockSuppliers = List.of(supplier1, supplier2);
        when(supplierRepository.findAll()).thenReturn(mockSuppliers);

        // Act
        List<Supplier> result = supplierService.getAllSuppliers();

        // Assert
        assertEquals(2, result.size(), "Expected list size of 2 when two suppliers are found");
        assertEquals(mockSuppliers, result, "Expected returned list to match the mock suppliers");
        verify(supplierRepository, times(1)).findAll();
    }

    @Test
    void testGetAllSuppliers_ExceptionThrown() {
        // Arrange
        when(supplierRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        try {
            supplierService.getAllSuppliers();
        } catch (Exception e) {
            assertEquals("Database error", e.getMessage(), "Expected exception message to match");
        }

        verify(supplierRepository, times(1)).findAll();
    }
    @Test
    void testUpdateSupplier_SupplierNotFound() {
        // Arrange
        String supplierId = "supplier123";
        UpdateSupplierDTO updateSupplierDTO = new UpdateSupplierDTO(); // Fill in fields as needed
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.empty());

        // Act & Assert
        SupplierNotFoundException exception = assertThrows(SupplierNotFoundException.class, () -> {
            supplierService.updateSupplier(supplierId, updateSupplierDTO);
        });

        assertEquals("Supplier not found with ID: " + supplierId, exception.getMessage());
        verify(supplierRepository, times(1)).findById(supplierId);
        verify(supplierRepository, never()).save(any(Supplier.class));
    }

    @Test
    void testUpdateSupplier_UpdateOnlyProvidedFields() {
        // Arrange
        String supplierId = "supplier123";
        Supplier existingSupplier = new Supplier();
        existingSupplier.setSupplierId(supplierId);
        existingSupplier.setCompanyName("Old Company");
        existingSupplier.setWebsite("www.oldwebsite.com");
        existingSupplier.setLocation("Old Location");

        UpdateSupplierDTO updateSupplierDTO = new UpdateSupplierDTO();
        updateSupplierDTO.setCompanyName("New Company");
        updateSupplierDTO.setWebsite("www.newwebsite.com");

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(existingSupplier));

        // Act
        Supplier updatedSupplier = supplierService.updateSupplier(supplierId, updateSupplierDTO);

        // Assert
        assertEquals("New Company", updatedSupplier.getCompanyName());
        assertEquals("www.newwebsite.com", updatedSupplier.getWebsite());
        assertEquals("Old Location", updatedSupplier.getLocation()); // Unchanged field
        verify(supplierRepository, times(1)).findById(supplierId);
        verify(supplierRepository, times(1)).save(existingSupplier);
    }

    @Test
    void testUpdateSupplier_NoFieldsToUpdate() {
        // Arrange
        String supplierId = "supplier123";
        Supplier existingSupplier = new Supplier();
        existingSupplier.setSupplierId(supplierId);
        existingSupplier.setCompanyName("Old Company");

        UpdateSupplierDTO updateSupplierDTO = new UpdateSupplierDTO(); // No fields are set

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(existingSupplier));

        // Act
        Supplier updatedSupplier = supplierService.updateSupplier(supplierId, updateSupplierDTO);

        // Assert
        assertEquals("Old Company", updatedSupplier.getCompanyName()); // Unchanged field
        verify(supplierRepository, times(1)).findById(supplierId);
        verify(supplierRepository, times(1)).save(existingSupplier);
    }

    @Test
    void testUpdateSupplier_ExceptionThrown() {
        // Arrange
        String supplierId = "supplier123";
        UpdateSupplierDTO updateSupplierDTO = new UpdateSupplierDTO(); // Fill in fields as needed
        when(supplierRepository.findById(supplierId)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            supplierService.updateSupplier(supplierId, updateSupplierDTO);
        });

        assertEquals("Database error", exception.getMessage());
        verify(supplierRepository, times(1)).findById(supplierId);
        verify(supplierRepository, never()).save(any(Supplier.class));
    }
    @Test
    void testDeleteSupplier_SupplierNotFound() {
        // Arrange
        String supplierId = "supplier123";
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.empty());

        // Act & Assert
        SupplierNotFoundException exception = assertThrows(SupplierNotFoundException.class, () -> {
            supplierService.deleteSupplier(supplierId);
        });

        assertEquals("Supplier not found with ID: " + supplierId, exception.getMessage());
        verify(supplierRepository, times(1)).findById(supplierId);
        verify(supplierRepository, never()).delete(any(Supplier.class));
    }

    @Test
    void testDeleteSupplier_Success() {
        // Arrange
        String supplierId = "supplier123";
        Supplier supplier = new Supplier();
        supplier.setSupplierId(supplierId);
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));

        // Act
        supplierService.deleteSupplier(supplierId);

        // Assert
        verify(supplierRepository, times(1)).findById(supplierId);
        verify(supplierRepository, times(1)).delete(supplier);
    }

    @Test
    void testDeleteSupplier_ExceptionThrown() {
        // Arrange
        String supplierId = "supplier123";
        Supplier supplier = new Supplier();
        supplier.setSupplierId(supplierId);
        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(supplier));
        doThrow(new RuntimeException("Database error")).when(supplierRepository).delete(supplier);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            supplierService.deleteSupplier(supplierId);
        });

        assertEquals("Database error", exception.getMessage());
        verify(supplierRepository, times(1)).findById(supplierId);
        verify(supplierRepository, times(1)).delete(supplier);
    }

}