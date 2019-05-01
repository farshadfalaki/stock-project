package com.farshad.stock.da;

import com.farshad.stock.model.Stock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;

import static com.farshad.stock.constants.Constants.INIT_RECORDS_COUNT;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@DataJpaTest
public class StockRepositoryTest {
    @Autowired
    private StockRepository stockRepository;

    @Test
    public void save_OneCompleteStock_ShouldBeSaved(){
        //given
        String companyName = "some Company";
        Double currentPrice = 23.1;
        Date lastUpdateDate = new Date();
        Stock stock = new Stock(companyName,currentPrice,lastUpdateDate);
        //when
        Stock persistedStock = stockRepository.save(stock);
        stockRepository.flush();
        //then
        assertEquals(persistedStock,stock);
    }

    @Test
    public void getOne_OneCompleteStock_ShouldBeFound(){
        //given
        String companyName = "some Company";
        Double currentPrice = 23.1;
        Date lastUpdateDate = new Date();
        Stock stock = new Stock(companyName,currentPrice,lastUpdateDate);
        //when
        Stock persistedStock = stockRepository.save(stock);
        stockRepository.flush();
        Stock foundedStock = stockRepository.findById(persistedStock.getId()).orElseThrow(()->new EntityNotFoundException(persistedStock.getId()+""));
        //then
        assertEquals(foundedStock,stock);
    }

    @Test(expected = EntityNotFoundException.class)
    public void getOne_NonExistingId_ShouldRaiseEntityNotFoundException(){
        //given
        Long id = 1009l;
        //when
        stockRepository.findById(id).orElseThrow(()->new EntityNotFoundException(id+""));
    }

    @Test(expected = DataAccessException.class)
    public void save_NullNameStock_ShouldRaiseDataAccessException(){
        //given
        String companyName = null;
        Double currentPrice = 23.1;
        Date lastUpdateDate = new Date();
        Stock stock = new Stock(companyName,currentPrice,lastUpdateDate);
        //when
        stockRepository.save(stock);
        stockRepository.flush();
    }

    @Test(expected = DataAccessException.class)
    public void save_NullCurrentPriceStock_ShouldRaiseDataAccessException(){
        //given
        String companyName = "some Company";
        Double currentPrice = null;
        Date lastUpdateDate = new Date();
        Stock stock = new Stock(companyName,currentPrice,lastUpdateDate);
        //when
        stockRepository.save(stock);
        stockRepository.flush();
    }

    @Test(expected = DataAccessException.class)
    public void save_NullLastUpdateStock_ShouldRaiseDataAccessException(){
        //given
        String companyName = "some Company";
        Double currentPrice = 23.1;
        Date lastUpdateDate = null;
        Stock stock = new Stock(companyName,currentPrice,lastUpdateDate);
        //when
        stockRepository.save(stock);
        stockRepository.flush();
    }


    @Test
    public void findAll_ShouldReturnInitialRecordsCount(){
        //given
        //when
        List<Stock> stockList = stockRepository.findAll();
        int firstFetchSize = stockList.size();
        //then
        assertEquals(INIT_RECORDS_COUNT,firstFetchSize);
    }

    @Test
    public void findAll_AfterSavingTwoStocks_ShouldReturnInitialRecordsCountPlusTwo(){
        //given
        String companyName1 = "some Company";
        Double currentPrice1 = 23.1;
        Date lastUpdateDate1 = new Date();
        String companyName2 = "some Company";
        Double currentPrice2 = 23.2;
        Date lastUpdateDate2 = new Date();
        Stock stock1 = new Stock(companyName1,currentPrice1,lastUpdateDate1);
        Stock stock2 = new Stock(companyName2,currentPrice2,lastUpdateDate2);
        //when
        stockRepository.save(stock1);
        stockRepository.save(stock2);
        stockRepository.flush();
        List<Stock> stockList = stockRepository.findAll();
        //then
        assertEquals(INIT_RECORDS_COUNT+2,stockList.size());
    }

    @Test
    public void save_SaveOneStockAndUpdateNameAndCurrentPrice_ShouldBeUpdated(){
        //given
        String companyName = "some Company";
        String companyNameNew = "some Company new";
        Double currentPrice = 23.1;
        Double newPrice = 25.1;
        Date lastUpdateDate = new Date();
        Date lastUpdateDateNew = new Date();
        Stock stock = new Stock(companyName,currentPrice,lastUpdateDate);

        //when
        stockRepository.save(stock);
        stockRepository.flush();
        stock.setName(companyNameNew);
        stock.setCurrentPrice(newPrice);
        stock.setLastUpdate(lastUpdateDateNew);
        stockRepository.flush();
        Stock stockFetched = stockRepository.getOne(stock.getId());
        //then
        assertEquals(stockFetched,stock);
    }

    @Test(expected = DataAccessException.class)
    public void save_SaveOneStockAndUpdateWithNullCurrentPrice_ShouldRaiseDataAccessException(){
        //given
        String companyName = "some Company";
        String companyNameNew = "some Company new";
        Double currentPrice = 23.1;
        Double newPrice = null;
        Date lastUpdateDate = new Date();
        Date lastUpdateDateNew = new Date();
        Stock stock = new Stock(companyName,currentPrice,lastUpdateDate);
        //when
        stockRepository.save(stock);
        stockRepository.flush();
        stock.setName(companyNameNew);
        stock.setCurrentPrice(newPrice);
        stock.setLastUpdate(lastUpdateDateNew);
        stockRepository.flush();

    }

    @Test(expected = DataAccessException.class)
    public void save_SaveOneStockAndUpdateWithNullName_ShouldRaiseDataAccessException(){
        //given
        String companyName = "some Company";
        String companyNameNew = null;
        Double currentPrice = 23.1;
        Double newPrice = 25.1;
        Date lastUpdateDate = new Date();
        Date lastUpdateDateNew = new Date();
        Stock stock = new Stock(companyName,currentPrice,lastUpdateDate);

        //when
        stockRepository.save(stock);
        stockRepository.flush();
        stock.setName(companyNameNew);
        stock.setCurrentPrice(newPrice);
        stock.setLastUpdate(lastUpdateDateNew);
        stockRepository.flush();

    }

    @Test(expected = DataAccessException.class)
    public void save_SaveOneStockAndUpdateWithNullLastUpdate_ShouldRaiseDataAccessException(){
        //given
        String companyName = "some Company";
        String companyNameNew = "some Company new";
        Double currentPrice = 23.1;
        Double newPrice = 25.1;
        Date lastUpdateDate = new Date();
        Date lastUpdateDateNew = null;
        Stock stock = new Stock(companyName,currentPrice,lastUpdateDate);
        //when
        stockRepository.save(stock);
        stockRepository.flush();
        stock.setName(companyNameNew);
        stock.setCurrentPrice(newPrice);
        stock.setLastUpdate(lastUpdateDateNew);
        stockRepository.flush();
    }
    @Test
    public void delete_DeleteAnExistingStock_ShouldBeDeleted(){
        //given
        String companyName = "some Company";
        Double currentPrice = 23.1;
        Date lastUpdateDate = new Date();
        Stock stock = new Stock(companyName,currentPrice,lastUpdateDate);
        //when
        stockRepository.save(stock);
        stockRepository.flush();
        stockRepository.deleteById(stock.getId());
        stockRepository.flush();
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void delete_DeleteNonExistingStock_ShouldBeDeleted(){
        //given
        Long id =349l;
        //when
        stockRepository.deleteById(id);
        stockRepository.flush();
    }





}