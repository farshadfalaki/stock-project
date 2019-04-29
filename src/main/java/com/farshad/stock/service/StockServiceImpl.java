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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
     * <br> if createStockRequest is null or any error in mapping processes null will be returned
     * @param createStockRequest
     * @return stockDto
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
     * Updates an existing stock
     * <br> First validation phase should be done
     * then load and then save method of repository will be called
     * @param updateStockPriceRequest
     * @return stockDto
     * @throws StockDataBusinessException if validation fails and no change in price
     */
    @Override
    @Transactional
    public StockDto updatePrice(Long stockId,UpdateStockPriceRequest updateStockPriceRequest){
        logger.debug("Update Price " + updateStockPriceRequest + " with Id=" + stockId);
        StockDto stockDto = null;
            Stock stockFetched = stockRepository.getOne(stockId);
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

    @Override
    @Transactional
    public void deleteById(Long id) {
        logger.debug("Delete by id " + id);
        stockRepository.deleteById(id);
        logger.debug("Deleted ");
    }

    @Override
    public List<StockDto> retrieveAll(){
        logger.debug("Retrieve all stocks");
        List<Stock> stockList = stockRepository.findAll();
        logger.debug("List size is " + stockList.size());
        List<StockDto> stockDtoList = stockList.stream().map(s -> stockMapper.convert(s)).collect(Collectors.toList());
        return stockDtoList;
    }

    @Override
    public StockDto getById(Long id) {
        logger.debug("Get by id " + id);
        Stock stock = stockRepository.getOne(id);
        logger.debug("Found stock " + stock);
        StockDto stockDto = stockMapper.convert(stock);
        return stockDto;
    }




}
