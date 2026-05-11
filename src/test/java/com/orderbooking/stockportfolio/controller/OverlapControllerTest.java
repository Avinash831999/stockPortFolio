package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.OverlapsInfo;
import com.orderbooking.stockportfolio.service.OverlapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OverlapControllerTest {

    @Mock
    private OverlapService overlapService;
    private OverlapController controller;

    @BeforeEach
    void setUp() {
        controller = new OverlapController(overlapService);
    }

    @Test
    void calculateOverlapInfo_returnsOk() {
        when(overlapService.calculateOverlapInfo(1L)).thenReturn(new OverlapsInfo());
        ResponseEntity<OverlapsInfo> response = controller.calculateOverlapInfo(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
