package com.farshad.stock.service;

import com.farshad.stock.da.StockRepository;
import com.farshad.stock.dto.CreateStockRequest;
import com.farshad.stock.dto.StockDto;
import com.farshad.stock.dto.UpdateStockPriceRequest;
import com.farshad.stock.exception.StockDataBusinessException;
import com.farshad.stock.mapper.StockMapper;
import com.farshad.stock.model.Stock;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StockServiceTest {
    private StockRepository stockRepository;
    private StockMapper     stockMapper;
    private StockService    stockService;

    final static long initSequenceValue = 10;

    @Before
    public void init(){
        stockRepository = mock(StockRepository.class);
        stockMapper = mock(StockMapper.class);
        stockService = new StockServiceImpl();
        ((StockServiceImpl)stockService).setStockRepository(stockRepository);
        ((StockServiceImpl)stockService).setStockMapper(stockMapper);

    }
    @Test
    public void create_OneValidCreateStockRequest_ShouldReturnTheSameStockWithId() {

        //given
        String companyName = "someCompany";
        Double currentPrice = 23.3;
        Date lastUpdateDate = new Date();
        CreateStockRequest createStockRequest = new CreateStockRequest(companyName,currentPrice);
        StockDto stockDtoExpectedOutput = new StockDto(initSequenceValue,companyName,currentPrice,lastUpdateDate);
        Stock stockInput = new Stock(companyName,currentPrice,lastUpdateDate);
        Stock stockOutput = new Stock(companyName,currentPrice,lastUpdateDate);
        stockOutput.setId(initSequenceValue);
        stockDtoExpectedOutput.setId(initSequenceValue);
        when(stockRepository.save(stockInput)).thenReturn(stockOutput);
        when(stockMapper.convert(createStockRequest)).thenReturn(stockInput);
        when(stockMapper.convert(stockOutput)).thenReturn(stockDtoExpectedOutput);

        //when
        StockDto stockDtoActualOutput = stockService.create(createStockRequest);

        //then
        verify(stockRepository,times(1)).save(stockInput);
        verify(stockMapper,times(1)).convert(stockOutput);
        verify(stockMapper,times(1)).convert(createStockRequest);
        assertEquals(stockDtoExpectedOutput,stockDtoActualOutput);
    }

    @Test
    public void create_NullInput_ShouldReturnNull(){
        //given
        CreateStockRequest createStockRequest = null;
        StockDto stockDtoExpectedOutput = null;
        Stock stockInput = null;
        Stock stockOutput = null;
        when(stockMapper.convert(createStockRequest)).thenReturn(stockInput);

        //when
        StockDto stockDtoActualOutput = stockService.create(createStockRequest);

        //then
        verify(stockRepository,never()).save(stockInput);
        verify(stockMapper,never()).convert(stockOutput);
        verify(stockMapper,times(1)).convert(createStockRequest);
        assertEquals(stockDtoExpectedOutput,stockDtoActualOutput);
    }

    @Test(expected = ConstraintViolationException.class)
    public void create_OneStockWithNullName_ShouldRaiseConstraintViolationException(){

        //given
        String companyName = null;
        Double currentPrice = 23.3;
        Date lastUpdateDate = new Date();
        CreateStockRequest createStockRequest = new CreateStockRequest(companyName,currentPrice);
        StockDto stockDtoExpectedOutput = new StockDto(initSequenceValue,companyName,currentPrice,lastUpdateDate);
        Stock stockInput = new Stock(companyName,currentPrice,lastUpdateDate);
        Stock stockOutput = new Stock(companyName,currentPrice,lastUpdateDate);
        stockOutput.setId(initSequenceValue);
        stockDtoExpectedOutput.setId(initSequenceValue);
        when(stockRepository.save(stockInput)).thenThrow(new ConstraintViolationException("Name field is null!",null,""));
        when(stockMapper.convert(createStockRequest)).thenReturn(stockInput);
        //when
        stockService.create(createStockRequest);
    }

    @Test(expected = ConstraintViolationException.class)
    public void create_OneStockWithNullCurrentPrice_ShouldRaiseConstraintViolationException(){

        //given
        String companyName = "Some Company";
        Double currentPrice = null;
        Date lastUpdateDate = new Date();
        CreateStockRequest createStockRequest = new CreateStockRequest(companyName,currentPrice);
        StockDto stockDtoExpectedOutput = new StockDto(initSequenceValue,companyName,currentPrice,lastUpdateDate);
        Stock stockInput = new Stock(companyName,currentPrice,lastUpdateDate);
        Stock stockOutput = new Stock(companyName,currentPrice,lastUpdateDate);
        stockOutput.setId(initSequenceValue);
        stockDtoExpectedOutput.setId(initSequenceValue);
        when(stockRepository.save(stockInput)).thenThrow(new ConstraintViolationException("CurrentPrice field is null!",null,""));
        when(stockMapper.convert(createStockRequest)).thenReturn(stockInput);

        //when
        stockService.create(createStockRequest);


    }

    @Test(expected = ConstraintViolationException.class)
    public void create_OneStockWithNullLastUpdate_ShouldRaiseConstraintViolationException(){

        //given
        String companyName = "Some Company";
        Double currentPrice = 23.3;
        Date creationDate = null;
        CreateStockRequest createStockRequest = new CreateStockRequest(companyName,currentPrice);
        StockDto stockDtoExpectedOutput = new StockDto(initSequenceValue,companyName,currentPrice,creationDate);
        Stock stockInput = new Stock(companyName,currentPrice,creationDate);
        Stock stockOutput = new Stock(companyName,currentPrice,creationDate);
        stockOutput.setId(initSequenceValue);
        stockDtoExpectedOutput.setId(initSequenceValue);
        when(stockRepository.save(stockInput)).thenThrow(new ConstraintViolationException("LastUpdate field is null!",null,""));
        when(stockMapper.convert(createStockRequest)).thenReturn(stockInput);

        //when
        stockService.create(createStockRequest);


    }

    @Test
    public void updatePrice_ValidPrice_ShouldChangeNewValue(){
        //given
        Date updateTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,-1);
        Date lastUpdateTime = calendar.getTime();
        long id = 100l;
        Double lastPrice = 21.3;
        Double newPrice  = 23.3;
        String name = "someCompany";
        UpdateStockPriceRequest updateStockPriceRequest = new UpdateStockPriceRequest(newPrice);
        Stock stockExisting = new Stock(name,lastPrice,lastUpdateTime);
        stockExisting.setId(id);
        Stock stockUpdated = new Stock(name,newPrice, updateTime);
        stockUpdated.setId(id);
        StockDto stockDtoExpectedOutput = new StockDto(id,name,newPrice,updateTime);
        when(stockRepository.getOne(id)).thenReturn(stockExisting);
        when(stockRepository.save(any())).thenReturn(stockUpdated);
        when(stockMapper.convert(stockUpdated)).thenReturn(stockDtoExpectedOutput);
        //when
        StockDto stockDtoActualOutput = stockService.updatePrice(id,updateStockPriceRequest);
        //then
        verify(stockRepository,times(1)).getOne(id);
        verify(stockRepository,times(1)).save(any());
        verify(stockMapper,times(1)).convert(stockUpdated);
        assertEquals(stockDtoExpectedOutput,stockDtoActualOutput);
    }

    @Test(expected = StockDataBusinessException.class)
    public void updatePrice_SamePrice_ShouldRaiseStockDataBusinessException(){
        //given
        Date updateTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,-1);
        Date lastUpdateTime = calendar.getTime();
        long id = 100l;
        Double lastPrice = 21.3;
        Double newPrice  = lastPrice;
        String name = "someCompany";
        UpdateStockPriceRequest updateStockPriceRequest = new UpdateStockPriceRequest(newPrice);
        Stock stockExisting = new Stock(name,lastPrice,lastUpdateTime);
        stockExisting.setId(id);
        Stock stockUpdated = new Stock(name,newPrice, updateTime);
        stockUpdated.setId(id);
        when(stockRepository.getOne(id)).thenReturn(stockExisting);
        //when
        stockService.updatePrice(id,updateStockPriceRequest);
    }

    @Test(expected = NullPointerException.class)
    public void updatePrice_NullPrice_ShouldRaiseNullPointerException(){
        //given
        Date updateTime = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY,-1);
        Date lastUpdateTime = calendar.getTime();
        long id = 100l;
        Double lastPrice = 21.3;
        Double newPrice  = null;
        String name = "someCompany";
        UpdateStockPriceRequest updateStockPriceRequest = new UpdateStockPriceRequest(newPrice);
        Stock stockExisting = new Stock(name,lastPrice,lastUpdateTime);
        stockExisting.setId(id);
        Stock stockUpdated = new Stock(name,newPrice, updateTime);
        stockUpdated.setId(id);
        when(stockRepository.getOne(id)).thenReturn(stockExisting);
        //when
        stockService.updatePrice(id,updateStockPriceRequest);
    }

    @Test
    public void retrieveAll_ShouldReturnEmptyList(){
        //given
        when(stockRepository.findAll()).thenReturn(new ArrayList<>());
        //when
        List<StockDto> stockList = stockService.retrieveAll();
        //then
        verify(stockRepository,times(1)).findAll();
        verify(stockMapper,never()).convert((Stock) any());
        assertEquals(0,stockList.size());
    }

    @Test
    public void retrieveAll_ShouldReturnTwoStockDtos(){
        //given
        Long id1= 1001l;
        String companyName1 = "someCompany1";
        Double currentPrice1 = 23.1;
        Date lastUpdateDate1 = new Date();
        Long id2= 1002l;
        String companyName2 = "someCompany2";
        Double currentPrice2 = 23.2;
        Date lastUpdateDate2 = new Date();
        Stock stock1 = new Stock(companyName1,currentPrice1,lastUpdateDate1);
        Stock stock2 = new Stock(companyName2,currentPrice2,lastUpdateDate2);
        StockDto stockDto1 = new StockDto(id1,companyName1,currentPrice1,lastUpdateDate1);
        stock1.setId(id1);
        StockDto stockDto2 = new StockDto(id2,companyName2,currentPrice2,lastUpdateDate2);
        stock2.setId(id2);
        when(stockRepository.findAll()).thenReturn(Arrays.asList(stock1,stock2));
        when(stockMapper.convert(stock1)).thenReturn(stockDto1);
        when(stockMapper.convert(stock2)).thenReturn(stockDto2);
        //when
        List<StockDto> stockDtoListActual = stockService.retrieveAll();
        //then
        verify(stockRepository,times(1)).findAll();
        verify(stockMapper,times(1)).convert(stock1);
        verify(stockMapper,times(1)).convert(stock2);
        assertEquals(2,stockDtoListActual.size());
    }

    @Test
    public void getById_ExistingId_ShouldReturnStock(){
        //given
        Long id = 1000l;
        String companyName = "some Company";
        Double currentPrice = 23.1;
        Date lastUpdateDate = new Date();
        Stock existingStock = new Stock(companyName,currentPrice,lastUpdateDate);
        existingStock.setId(id);
        StockDto expectedStockDto = new StockDto(id,companyName,currentPrice,lastUpdateDate);
        when(stockRepository.getOne(id)).thenReturn(existingStock);
        when(stockMapper.convert(existingStock)).thenReturn(expectedStockDto);
        //when
        StockDto actualStockDto = stockService.getById(id);
        //then
        verify(stockRepository,times(1)).getOne(id);
        verify(stockMapper,times(1)).convert(existingStock);
        assertEquals(expectedStockDto,actualStockDto);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getById_NonExistingId_ShouldRaiseEntityNotFoundException(){
        //given
        Long id = 1000l;
        when(stockRepository.getOne(id)).thenThrow(new EntityNotFoundException());
        //when
        stockService.getById(id);
    }

    @Test
    public void delete_ExistingId_ShouldRaiseEntityNotFoundException(){
        //given
        Long id = 1l;
        //when
        stockService.deleteById(id);
        verify(stockRepository,times(1)).deleteById(id);

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void delete_NonExistingId_ShouldRaiseEntityNotFoundException(){
        //given
        Long id = 1000l;
        doThrow(new EmptyResultDataAccessException(0)).doNothing().when(stockRepository).deleteById(id);

        //when
        stockService.deleteById(id);
    }
}