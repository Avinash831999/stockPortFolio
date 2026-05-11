package com.orderbooking.stockportfolio.controller;

import com.orderbooking.stockportfolio.dto.SectorDto;
import com.orderbooking.stockportfolio.service.SectorsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sectors")
public class SectorController {

    private final SectorsService sectorsService;

    public SectorController(SectorsService sectorsService) {
        this.sectorsService = sectorsService;
    }

    @GetMapping("/")
    public ResponseEntity<List<SectorDto>> getAllSectors() {
        return ResponseEntity.ok(this.sectorsService.getAllSectors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SectorDto> getSectorDetails(@PathVariable("id") Long sectorId) {
        return ResponseEntity.ok(this.sectorsService.getSectorDetailsById(sectorId));
    }

    @PostMapping
    public ResponseEntity<SectorDto> addSector(@Valid @RequestBody SectorDto sectorDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.sectorsService.addSector(sectorDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeSector(@PathVariable("id") Long sectorId) {
        this.sectorsService.removeSector(sectorId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectorDto> updateSector(@Valid @PathVariable("id") Long sectorId, @RequestBody SectorDto sectorDto) {
       return ResponseEntity.ok(this.sectorsService.updateSector(sectorId,sectorDto));
    }
}
