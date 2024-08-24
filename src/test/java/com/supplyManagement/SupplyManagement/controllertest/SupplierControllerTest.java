package com.supplyManagement.SupplyManagement.controllertest;
import com.supplyManagement.SupplyManagement.controller.SupplierController;
import com.supplyManagement.SupplyManagement.exception.SupplierNotFoundException;
import com.supplyManagement.SupplyManagement.model.Supplier;
import com.supplyManagement.SupplyManagement.model.dto.AddSupplierDTO;
import com.supplyManagement.SupplyManagement.model.dto.UpdateSupplierDTO;
import com.supplyManagement.SupplyManagement.model.enums.ManufacturingProcess;
import com.supplyManagement.SupplyManagement.model.enums.NatureOfBusiness;
import com.supplyManagement.SupplyManagement.service.SupplierService;
import jakarta.validation.ConstraintViolationException;
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
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@Profile("dev")
@ExtendWith(MockitoExtension.class)
class SupplierControllerTest {

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private SupplierController supplierController;

    private Supplier supplier;
    private UpdateSupplierDTO updateSupplierDTO;
    private List<Supplier> supplierList;
    private Page<Supplier> supplierPage;
    private AddSupplierDTO addSupplierDTO;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        updateSupplierDTO = new UpdateSupplierDTO();
        addSupplierDTO = new AddSupplierDTO();
        supplierList = new ArrayList<>();
        Supplier supplier = new Supplier();
        supplierList.add(supplier);
        supplier = new Supplier();

        supplierPage = new PageImpl<>(supplierList);
    }

    @Test
    void testQuerySuppliers_Success() {
        // Arrange
        when(supplierService.querySuppliers("New York", NatureOfBusiness.SMALL_SCALE, ManufacturingProcess.MOULDING, 0, 10))
                .thenReturn(supplierPage);

        // Act
        ResponseEntity<List<Supplier>> response = supplierController.querySuppliers(
                "New York", "SMALL_SCALE", "MOULDING", 0, 10);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        verify(supplierService, times(1)).querySuppliers("New York", NatureOfBusiness.SMALL_SCALE, ManufacturingProcess.MOULDING, 0, 10);
    }

    @Test
    void testQuerySuppliers_NoSuppliersFound() {
        // Arrange
        when(supplierService.querySuppliers("New York", NatureOfBusiness.SMALL_SCALE, ManufacturingProcess.MOULDING, 0, 10))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        // Act
        ResponseEntity<List<Supplier>> response = supplierController.querySuppliers(
                "New York", "SMALL_SCALE", "MOULDING", 0, 10);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(supplierService, times(1)).querySuppliers("New York", NatureOfBusiness.SMALL_SCALE, ManufacturingProcess.MOULDING, 0, 10);
    }

    @Test
    void testQuerySuppliers_InvalidNatureOfBusiness() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                supplierController.querySuppliers("New York", "INVALID", "ASSEMBLY", 0, 10));

        assertEquals("Invalid NatureOfBusiness value: INVALID", exception.getMessage());
        verify(supplierService, never()).querySuppliers(anyString(), any(), any(), anyInt(), anyInt());
    }

    @Test
    void testQuerySuppliers_InvalidManufacturingProcess() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                supplierController.querySuppliers("New York", "SMALL_SCALE", "INVALID", 0, 10));

        assertEquals("Invalid ManufacturingProcess value: INVALID", exception.getMessage());
        verify(supplierService, never()).querySuppliers(anyString(), any(), any(), anyInt(), anyInt());
    }

    @Test
    void testAddSupplier_Success() {
        // Arrange
        when(supplierService.addSupplier(any(AddSupplierDTO.class))).thenReturn("1");

        // Act
        ResponseEntity<String> response = supplierController.addSupplier(addSupplierDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Supplier added successfully", response.getBody());
        verify(supplierService, times(1)).addSupplier(addSupplierDTO);
    }



    @Test
    void testAddSupplier_ValidationFailure() {
        AddSupplierDTO invalidSupplierDTO = new AddSupplierDTO();
        doThrow(new ConstraintViolationException("Validation failed", null))
                .when(supplierService).addSupplier(any(AddSupplierDTO.class));
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            supplierController.addSupplier(invalidSupplierDTO);
        });

        assertEquals("Validation failed", exception.getMessage());
        verify(supplierService, times(1)).addSupplier(invalidSupplierDTO);
    }
    @Test
    void testGetAllSuppliers_Success() {
        // Arrange
        when(supplierService.getAllSuppliers()).thenReturn(supplierList);

        // Act
        ResponseEntity<List<Supplier>> response = supplierController.getAllSuppliers();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(supplierList, response.getBody());
        verify(supplierService, times(1)).getAllSuppliers();
    }

    @Test
    void testGetAllSuppliers_EmptyList() {
        // Arrange
        when(supplierService.getAllSuppliers()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<Supplier>> response = supplierController.getAllSuppliers();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
        verify(supplierService, times(1)).getAllSuppliers();
    }

    @Test
    void testGetAllSuppliers_Exception() {
        // Arrange
        when(supplierService.getAllSuppliers()).thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            supplierController.getAllSuppliers();
        });

        assertEquals("Database connection failed", exception.getMessage());
        verify(supplierService, times(1)).getAllSuppliers();
    }
    @Test
    void testUpdateSupplier_Success() {
        // Arrange
        String supplierId = "1";
        when(supplierService.updateSupplier(supplierId, updateSupplierDTO)).thenReturn(supplier);

        // Act
        ResponseEntity<Supplier> response = supplierController.updateSupplier(supplierId, updateSupplierDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(supplier, response.getBody());
        verify(supplierService, times(1)).updateSupplier(supplierId, updateSupplierDTO);
    }

    @Test
    void testUpdateSupplier_SupplierNotFound() {
        // Arrange
        String supplierId = "1";
        when(supplierService.updateSupplier(supplierId, updateSupplierDTO))
                .thenThrow(new SupplierNotFoundException("Supplier not found"));

        // Act & Assert
        SupplierNotFoundException exception = assertThrows(SupplierNotFoundException.class, () -> {
            supplierController.updateSupplier(supplierId, updateSupplierDTO);
        });

        assertEquals("Supplier not found", exception.getMessage());
        verify(supplierService, times(1)).updateSupplier(supplierId, updateSupplierDTO);
    }

    @Test
    void testUpdateSupplier_ValidationFailure() {
        // Arrange
        String supplierId = "1";
        // Assuming validation would fail, mock service to throw a ConstraintViolationException
        doThrow(new ConstraintViolationException("Validation failed", null))
                .when(supplierService).updateSupplier(supplierId, updateSupplierDTO);

        // Act & Assert
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            supplierController.updateSupplier(supplierId, updateSupplierDTO);
        });

        assertEquals("Validation failed", exception.getMessage());
        verify(supplierService, times(1)).updateSupplier(supplierId, updateSupplierDTO);
    }
}
