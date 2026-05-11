package com.orderbooking.stockportfolio.service;

import com.orderbooking.stockportfolio.dto.SectorDto;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SectorsService {
    List<SectorDto> getAllSectors();
    SectorDto getSectorDetailsById(Long sectorId);
    SectorDto addSector(SectorDto sectorDto);
    void removeSector(Long sectorId);
    SectorDto updateSector(Long sectorId, SectorDto sectorDto);
}
