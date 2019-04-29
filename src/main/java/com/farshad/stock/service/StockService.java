package com.farshad.stock.service;

import com.farshad.stock.dto.CreateStockRequest;
import com.farshad.stock.dto.StockDto;
import com.farshad.stock.dto.UpdateStockPriceRequest;

import java.util.List;

public interface StockService {
    /**
     * Creates a new stock
     * if any validation rule or persisting process fails a null object will be returned
     * @param createStockRequest
     * @return stock
     */
    StockDto create(CreateStockRequest createStockRequest);

    /**
     * Returns all the stocks
     * @return
     */
    List<StockDto> retrieveAll();

    /**
     * Return stock with id , returns null if not found
     * @param id
     * @return
     */
    StockDto getById(Long id);

    /**
     * Updates an existing stock
     * if any validation rule or persisting process fails or there isn't any existing stock, a null object will be returned.
     * @param updateStockPriceRequest
     * @return
     */
    StockDto updatePrice(Long id,UpdateStockPriceRequest updateStockPriceRequest);

    void deleteById(Long id);
}
