package com.farshad.stock.service;

import com.farshad.stock.dto.CreateStockRequest;
import com.farshad.stock.dto.StockDto;
import com.farshad.stock.dto.UpdateStockPriceRequest;

import java.util.List;

public interface StockService {

    StockDto create(CreateStockRequest createStockRequest);
    List<StockDto> retrieveAll();
    StockDto getById(Long id);
    StockDto updatePrice(Long id,UpdateStockPriceRequest updateStockPriceRequest);
    void deleteById(Long id);
}
