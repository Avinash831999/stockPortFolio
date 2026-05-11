package com.orderbooking.stockportfolio.service;

import com.orderbooking.stockportfolio.dto.Overlap;
import com.orderbooking.stockportfolio.dto.OverlapsInfo;

public interface OverlapService {
    OverlapsInfo calculateOverlapInfo(Long traderId);
}
