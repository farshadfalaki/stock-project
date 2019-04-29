package com.farshad.stock.mapper;

import com.farshad.stock.dto.CreateStockRequest;
import com.farshad.stock.dto.StockDto;
import com.farshad.stock.model.Stock;

public interface StockMapper {
    StockDto    convert(Stock stock);
    Stock       convert(CreateStockRequest createStockRequest);

}
