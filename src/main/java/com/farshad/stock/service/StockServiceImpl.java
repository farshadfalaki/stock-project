package com.farshad.stock.service;

import com.farshad.stock.da.StockRepository;
import com.farshad.stock.dto.CreateStockRequest;
import com.farshad.stock.dto.StockDto;
import com.farshad.stock.dto.UpdateStockPriceRequest;
import com.farshad.stock.exception.StockDataBusinessException;
import com.farshad.stock.mapper.StockMapper;
import com.farshad.stock.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.farshad.stock.constants.ErrorMessages.PRICE_NOT_CHANGED;

@Component
public class StockServiceImpl implements StockService{
    private static final Logger logger = LoggerFactory.getLogger(StockServiceImpl.class);

    private StockRepository     stockRepository;
    private StockMapper         stockMapper;

    @Autowired
    public void setStockRepository(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
    @Autowired
    public void setStockMapper(StockMapper stockMapper) {
        this.stockMapper = stockMapper;
    }

    /**
     * Creates a new stock
     * <br> If createStockRequest is null or any error happens during mapping process null will be returned
     * @param createStockRequest
     * @return stockDto
     * @throws DataAccessException If an exception occurs while saving
     */
    @Override
    @Transactional
    public StockDto create(CreateStockRequest createStockRequest){
        logger.debug("Creating " + createStockRequest);
        StockDto stockDto = null;
        Stock stock = stockMapper.convert(createStockRequest);
        if(stock != null) {
            stock.setLastUpdate(new Date());
            Stock stockPersisted = stockRepository.save(stock);
            logger.debug("Stock created " + stockPersisted);
            stockDto = stockMapper.convert(stockPersisted);
        }else{
            logger.debug("Mapped stock is null, null will be returned");
        }
        return stockDto;
    }

    /**
     * Updates currentPrice of an existing stock
     * <br>First load and then save method of repository will be called
     * @param id
     * @param updateStockPriceRequest
     * @return stockDto
     * @throws StockDataBusinessException If validation fails and no change in price
     * @throws DataAccessException If an exception occurs while saving
     */
    @Override
    @Transactional
    public StockDto updatePrice(Long id,UpdateStockPriceRequest updateStockPriceRequest){
        logger.debug("Update Price " + updateStockPriceRequest + " with Id=" + id);
        StockDto stockDto = null;
            Stock stockFetched = stockRepository.getOne(id);
            logger.debug("stockFetched " + stockFetched);
            /**
             * checking that a stock exists with this id
             */
            if(stockFetched != null){
                /**
                 * check that price is changed or not.
                 * if there is no change, there is no need to update
                 */
                if(stockFetched.getCurrentPrice().doubleValue()!= updateStockPriceRequest.getCurrentPrice().doubleValue()) {
                    stockFetched.setCurrentPrice(updateStockPriceRequest.getCurrentPrice());
                    stockFetched.setLastUpdate(new Date());
                    Stock stockUpdated = stockRepository.save(stockFetched);
                    logger.debug("Updated stock " + stockUpdated);
                    stockDto = stockMapper.convert(stockUpdated);
                }else{
                    /**
                     * No change in price, there is no need to update then  null object will be returned
                     */
                    logger.debug("No need to update , new price [" + updateStockPriceRequest.getCurrentPrice() + "] isn't changed for " + stockFetched );
                    throw new StockDataBusinessException(PRICE_NOT_CHANGED);
                }
            }
        return stockDto;
    }

    /**
     * Delete a stock with id
     * @param id
     * @throws EmptyResultDataAccessException If there is no stock with id to delete
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.debug("Delete by id " + id);
        stockRepository.deleteById(id);
        logger.debug("Deleted ");
    }

    /**
     * Provides a list of all existing stocks
     * @return stockDtoList
     */
    @Override
    public List<StockDto> retrieveAll(){
        logger.debug("Retrieve all stocks");
        List<Stock> stockList = stockRepository.findAll();
        logger.debug("List size is " + stockList.size());
        List<StockDto> stockDtoList = stockList.stream().map(s -> stockMapper.convert(s)).collect(Collectors.toList());
        return stockDtoList;
    }

    /**
     * Provides a stock with id
     * @param id
     * @return stockDto
     */
    @Override
    public StockDto getById(Long id) {
        logger.debug("Get by id " + id);
        Stock stock = stockRepository.findById(id).orElseThrow(()->new EntityNotFoundException(id+""));
        logger.debug("Found stock " + stock);
        StockDto stockDto = stockMapper.convert(stock);
        return stockDto;
    }
}
