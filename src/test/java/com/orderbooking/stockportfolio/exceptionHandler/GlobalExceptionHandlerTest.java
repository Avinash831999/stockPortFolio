package com.orderbooking.stockportfolio.exceptionHandler;

import com.orderbooking.stockportfolio.dto.ErrorResponse;
import com.orderbooking.stockportfolio.exceptions.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    @Test
    void testHandleDataNotFoundException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        DataNotFoundException ex =
                new DataNotFoundException("Data not found");

        ResponseEntity<ErrorResponse> response =
                handler.handleDataNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Data not found", response.getBody().getMessage());
    }

    @Test
    void testHandleDuplicateDataException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        DuplicateDataException ex =
                new DuplicateDataException("Duplicate data");

        ResponseEntity<ErrorResponse> response =
                handler.handleDuplicateDataException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Duplicate data", response.getBody().getMessage());
    }

    @Test
    void testHandleIllegalArgumentException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        IllegalArgumentException ex =
                new IllegalArgumentException("Invalid arg");

        ResponseEntity<ErrorResponse> response =
                handler.handleIllegalArgumentException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid arg", response.getBody().getMessage());
    }

    @Test
    void testHandleValidationExceptions() throws NoSuchMethodException {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        Dummy dummy = new Dummy();

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(dummy, "dummy");

        bindingResult.addError(
                new FieldError("dummy", "name", "name is required"));

        MethodParameter methodParameter =
                new MethodParameter(
                        Dummy.class.getDeclaredMethod("dummyMethod", String.class),
                        0
                );

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(
                        methodParameter,
                        bindingResult
                );

        ResponseEntity<Map<String, String>> response =
                handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals(
                "name is required",
                response.getBody().get("name")
        );
    }

    @Test
    void testHandleDataIntegrityViolationPanNumber() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        SQLException sqlException =
                new SQLException("duplicate pan_number");

        DataIntegrityViolationException ex =
                new DataIntegrityViolationException(
                        "error",
                        sqlException
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleDataIntegrityViolationException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        assertEquals(
                "PAN number already exists",
                response.getBody().getMessage()
        );
    }

    @Test
    void testHandleDataIntegrityViolationEmail() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        SQLException sqlException =
                new SQLException("duplicate email");

        DataIntegrityViolationException ex =
                new DataIntegrityViolationException(
                        "error",
                        sqlException
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleDataIntegrityViolationException(ex);

        assertEquals(
                "Email already exists",
                response.getBody().getMessage()
        );
    }

    @Test
    void testHandleDataIntegrityViolationStockName() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        SQLException sqlException =
                new SQLException("duplicate stock name");

        DataIntegrityViolationException ex =
                new DataIntegrityViolationException(
                        "error",
                        sqlException
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleDataIntegrityViolationException(ex);

        assertEquals(
                "Stock name already exists",
                response.getBody().getMessage()
        );
    }

    @Test
    void testHandleDataIntegrityViolationBasketName() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        SQLException sqlException =
                new SQLException("duplicate basket name");

        DataIntegrityViolationException ex =
                new DataIntegrityViolationException(
                        "error",
                        sqlException
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleDataIntegrityViolationException(ex);

        assertEquals(
                "Basket name already exists",
                response.getBody().getMessage()
        );
    }

    @Test
    void testHandleDataIntegrityViolationSectorName() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        SQLException sqlException =
                new SQLException("duplicate sector name");

        DataIntegrityViolationException ex =
                new DataIntegrityViolationException(
                        "error",
                        sqlException
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleDataIntegrityViolationException(ex);

        assertEquals(
                "Sector name already exists",
                response.getBody().getMessage()
        );
    }

    @Test
    void testHandleDataIntegrityViolationHoldingConstraint() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        SQLException sqlException =
                new SQLException("holding traderid stockid duplicate");

        DataIntegrityViolationException ex =
                new DataIntegrityViolationException(
                        "error",
                        sqlException
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleDataIntegrityViolationException(ex);

        assertEquals(
                "Trader already holds this stock Update the existing holding data",
                response.getBody().getMessage()
        );
    }

    @Test
    void testHandleDataIntegrityViolationDuplicateKey() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        SQLException sqlException =
                new SQLException("duplicate key");

        DataIntegrityViolationException ex =
                new DataIntegrityViolationException(
                        "error",
                        sqlException
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleDataIntegrityViolationException(ex);

        assertEquals(
                "Duplicate data found",
                response.getBody().getMessage()
        );
    }

    @Test
    void testHandleDataIntegrityViolationDefaultMessage() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        SQLException sqlException =
                new SQLException("random db error");

        DataIntegrityViolationException ex =
                new DataIntegrityViolationException(
                        "error",
                        sqlException
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleDataIntegrityViolationException(ex);

        assertEquals(
                "Data integrity violation occurred",
                response.getBody().getMessage()
        );
    }

    @Test
    void testHandleConstraintViolationException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ConstraintViolation<?> violation =
                mock(ConstraintViolation.class);

        Path path = mock(Path.class);

        when(path.toString()).thenReturn("email");

        when(violation.getPropertyPath()).thenReturn(path);

        when(violation.getMessage())
                .thenReturn("must not be blank");

        Set<ConstraintViolation<?>> violations =
                Set.of(violation);

        ConstraintViolationException ex =
                new ConstraintViolationException(violations);

        ResponseEntity<Map<String, String>> response =
                handler.handleConstraintViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        assertEquals(
                "must not be blank",
                response.getBody().get("email")
        );
    }

    @Test
    void testHandleIllegalOrderStateException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        IllegalOrderStateException ex =
                new IllegalOrderStateException("illegal state");

        ResponseEntity<ErrorResponse> response =
                handler.handleGlobalException(ex);

        assertEquals(
                HttpStatus.UNPROCESSABLE_ENTITY,
                response.getStatusCode()
        );

        assertEquals(
                "illegal state",
                response.getBody().getMessage()
        );
    }

    @Test
    void testHandleMaxPendingOrdersCountException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        MaxPendingOrdersCountException ex =
                new MaxPendingOrdersCountException("max orders");

        ResponseEntity<ErrorResponse> response =
                handler.handleGlobalException(ex);

        assertEquals(
                HttpStatus.UNPROCESSABLE_ENTITY,
                response.getStatusCode()
        );

        assertEquals(
                "max orders",
                response.getBody().getMessage()
        );
    }

    @Test
    void testHandleNotEnoughSharesException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        NotEnoughSharesException ex =
                new NotEnoughSharesException("not enough shares");

        ResponseEntity<ErrorResponse> response =
                handler.handleGlobalException(ex);

        assertEquals(
                HttpStatus.UNPROCESSABLE_ENTITY,
                response.getStatusCode()
        );

        assertEquals(
                "not enough shares",
                response.getBody().getMessage()
        );
    }

    @Test
    void testHandleGenericException() {

        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        Exception ex = new Exception("unexpected");

        ResponseEntity<ErrorResponse> response =
                handler.handleGlobalException(ex);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertEquals(
                "An unexpected error occurred",
                response.getBody().getMessage()
        );
    }


    static class Dummy {

        private String name;

        public void dummyMethod(String name) {

        }
    }
}