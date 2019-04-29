package com.farshad.stock.mapper;

import com.farshad.stock.dto.CreateStockRequest;
import com.farshad.stock.dto.StockDto;
import com.farshad.stock.model.Stock;
import org.springframework.stereotype.Component;

@Component
public class SimpleStockMapperImpl implements StockMapper {
    @Override
    public StockDto convert(Stock stock) {
        StockDto stockDto = null;
        if(stock != null){
            stockDto = new StockDto(stock.getId(),stock.getName(),stock.getCurrentPrice(),stock.getLastUpdate());
        }
        return stockDto;
    }

    @Override
    public Stock convert(CreateStockRequest createStockRequest) {
        Stock stock = null;
        if(createStockRequest != null){
            stock = new Stock(createStockRequest.getName(), createStockRequest.getCurrentPrice(),null);
        }
        return stock;
    }
}
