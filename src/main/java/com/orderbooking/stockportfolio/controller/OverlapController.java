package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.OverlapsInfo;
import com.orderbooking.stockportfolio.service.OverlapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/overlaps")
public class OverlapController {

    private final OverlapService overlapService;

    public OverlapController(OverlapService overlapService) {
        this.overlapService = overlapService;
    }

    @GetMapping("/trader/{id}")
    public ResponseEntity<OverlapsInfo> calculateOverlapInfo(@PathVariable("id") Long traderId) {
        return ResponseEntity.ok(this.overlapService.calculateOverlapInfo(traderId));
    }
}
