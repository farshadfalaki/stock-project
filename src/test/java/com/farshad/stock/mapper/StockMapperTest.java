package com.farshad.stock.mapper;

import com.farshad.stock.dto.CreateStockRequest;
import com.farshad.stock.dto.StockDto;
import com.farshad.stock.model.Stock;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class StockMapperTest {
    private StockMapper stockMapper;
    @Before
    public void init(){
        stockMapper = new SimpleStockMapperImpl();
    }
    @Test
    public void convert_NullCreateStockRequest_shouldReturnNull(){
        //given
        CreateStockRequest input = null;
        Stock expectedOutput = null;
        //when
        Stock actualOutput = stockMapper.convert(input);
        //then
        assertEquals(expectedOutput,actualOutput);

    }

    @Test
    public void convert_NullStock_shouldReturnNull(){
        //given
        Stock input = null;
        StockDto expectedOutput = null;
        //when
        StockDto actualOutput = stockMapper.convert(input);
        //then
        assertEquals(expectedOutput,actualOutput);

    }

    @Test
    public void convert_convertValidCreateStockRequest_shouldReturnValid(){
        //given
        Double currentPrice = 12.8;
        String name = "Some company";
        CreateStockRequest input = new CreateStockRequest(name,currentPrice);
        Stock expectedOutput = new Stock(name,currentPrice,null);
        //when
        Stock actualOutput = stockMapper.convert(input);
        //then
        assertEquals(expectedOutput,actualOutput);

    }

    @Test
    public void convert_convertValidStock_shouldReturnValid(){
        //given
        Long id = 124l;
        Double currentPrice = 12.8;
        String name = "Some company";
        Date date = new Date();
        Stock input = new Stock(name,currentPrice,date);
        input.setId(id);
        StockDto expectedOutput = new StockDto(id,name,currentPrice,date);
        //when
        StockDto actualOutput = stockMapper.convert(input);
        //then
        assertEquals(expectedOutput,actualOutput);

    }

}