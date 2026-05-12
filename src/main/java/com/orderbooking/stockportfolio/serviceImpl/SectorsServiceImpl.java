package com.orderbooking.stockportfolio.serviceImpl;

import com.orderbooking.stockportfolio.cacheMap.CacheDataMap;
import com.orderbooking.stockportfolio.dto.SectorDto;
import com.orderbooking.stockportfolio.entity.Sector;
import com.orderbooking.stockportfolio.entity.Stock;
import com.orderbooking.stockportfolio.exceptions.DataNotFoundException;
import com.orderbooking.stockportfolio.exceptions.DuplicateDataException;
import com.orderbooking.stockportfolio.repository.SectorRepository;
import com.orderbooking.stockportfolio.repository.StockRepository;
import com.orderbooking.stockportfolio.service.SectorsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectorsServiceImpl implements SectorsService {

    private final SectorRepository sectorRepository;
    private final CacheDataMap cacheDataMap;
    private final Logger logger = LoggerFactory.getLogger(SectorsServiceImpl.class);
    public SectorsServiceImpl(SectorRepository sectorRepository,  CacheDataMap cacheDataMap) {
         this.cacheDataMap = cacheDataMap;
        this.sectorRepository = sectorRepository;
    }

    @Override
    public List<SectorDto> getAllSectors() {
        logger.info("Fetching all sectors");
        return sectorRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public SectorDto getSectorDetailsById(Long sectorId) {
        Sector sector = sectorRepository.findById(sectorId).orElseThrow(() -> {
            logger.error("Sector with id {} not found", sectorId);
            return new DataNotFoundException("Sector not found");});
        return convertToDto(sector);
    }

    @Override
    public SectorDto addSector(SectorDto sectorDto) {

        if(this.cacheDataMap.getSectorIdNameMap().containsValue(sectorDto.getName())) {
           throw new DuplicateDataException("Sector " + sectorDto.getName() + " already exists");
        }
        else{
            Sector sector = getSectorFromSectorDto(sectorDto);
            sector.setCreatedAt(new Date());
            sector.setUpdatedAt(new Date());
            sector = sectorRepository.save(sector);
            cacheDataMap.getSectorIdNameMap().put(sector.getId(), sector.getName());
            logger.info("Sector with id {} added successfully", sector.getId());
            return convertToDto(sector);
        }
    }

    @Override
    public void removeSector(Long sectorId) {
        if (cacheDataMap.getSectorIdNameMap().containsKey(sectorId)) {
            sectorRepository.deleteById(sectorId);
            cacheDataMap.getSectorIdNameMap().remove(sectorId);
        }
        else{
            throw new DataNotFoundException("Sector not found");
        }
    }

    @Override
    public SectorDto updateSector(Long sectorId, SectorDto sectorDto) {
        if (!cacheDataMap.getSectorIdNameMap().containsKey(sectorId)) {
            logger.error("sector with id {} not found", sectorId);
            throw new DataNotFoundException("Sector not found");
        }
        Sector sector = this.sectorRepository.findById(sectorId).get();

        if (sectorDto.getName() != null) {
            sector.setName(sectorDto.getName());
        }
        sector.setUpdatedAt(new Date());
        sector = sectorRepository.save(sector);
        cacheDataMap.getSectorIdNameMap().put(sector.getId(), sector.getName());
        logger.info("sector with name {} updated successfully", sector.getName());
        return convertToDto(sector);
    }

    private Sector getSectorFromSectorDto(SectorDto sectorDto) {
        Sector sector = new Sector();
        sector.setId(sectorDto.getId());
        sector.setName(sectorDto.getName());
        return sector;
    }

    private SectorDto convertToDto(Sector sector) {
        SectorDto sectorDto = new SectorDto();
        sectorDto.setId(sector.getId());
        sectorDto.setName(sector.getName());
        sectorDto.setCreatedAt(sector.getCreatedAt());
        sectorDto.setUpdatedAt(sector.getUpdatedAt());
        return sectorDto;
    }
}
