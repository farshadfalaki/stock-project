package com.farshad.stock.controller;

import com.farshad.stock.dto.CreateStockRequest;
import com.farshad.stock.dto.StockDto;
import com.farshad.stock.dto.UpdateStockPriceRequest;
import com.farshad.stock.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.farshad.stock.constants.URlConstants.STOCK_CONTROLLER_BASE_PATH;
import static com.farshad.stock.constants.URlConstants.STOCK_CONTROLLER_METHOD_PATH;
import static com.farshad.stock.constants.URlConstants.STOCK_CONTROLLER_METHOD_PATH_FOLLOWING_SLASH;


@RestController
@RequestMapping(path = STOCK_CONTROLLER_BASE_PATH)
public class StockController {
    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    @Autowired
    private StockService stockService;

    @RequestMapping(value = STOCK_CONTROLLER_METHOD_PATH,method = RequestMethod.GET)
    public ResponseEntity<List<StockDto>> retrieveAllStocks(){
        logger.debug("##New request : retrieveAllStocks");
        List<StockDto> stockDtoList = stockService.retrieveAll();
        logger.debug("##Returned list's size is " + stockDtoList.size());
        return new ResponseEntity<>(stockDtoList,HttpStatus.OK);
    }

    @RequestMapping(value = STOCK_CONTROLLER_METHOD_PATH,method = RequestMethod.POST)
    public ResponseEntity<StockDto> createStock(@Valid @RequestBody CreateStockRequest createStockRequest){
        logger.debug("##New request : create " + createStockRequest);
        StockDto stockDto = stockService.create(createStockRequest);
        logger.debug("##Returned stock " + stockDto);
        return new ResponseEntity<>(stockDto,HttpStatus.CREATED);
    }

    @RequestMapping(value = STOCK_CONTROLLER_METHOD_PATH_FOLLOWING_SLASH + "{id}",method = RequestMethod.GET)
    public ResponseEntity<StockDto> getStockById(@PathVariable Long id){
        logger.debug("##New request : getStockById " + id);
        StockDto stockDto = stockService.getById(id);
        logger.debug("##Returned stock " + stockDto);
        return new ResponseEntity<>(stockDto,HttpStatus.OK);
    }


    @RequestMapping(value = STOCK_CONTROLLER_METHOD_PATH_FOLLOWING_SLASH + "{id}",method = RequestMethod.PUT)
    public ResponseEntity<StockDto> updateStock(@PathVariable Long id,@Valid @RequestBody UpdateStockPriceRequest updateStockPriceRequest){
        logger.debug("##New request : updateStock id=" + id, " new price " + updateStockPriceRequest);
        StockDto stockUpdated = stockService.updatePrice(id, updateStockPriceRequest);
        logger.debug("##Returned stock " + stockUpdated);
        return new ResponseEntity<>(stockUpdated,HttpStatus.OK);
    }

    @RequestMapping(value = STOCK_CONTROLLER_METHOD_PATH_FOLLOWING_SLASH + "{id}",method = RequestMethod.DELETE)
    public ResponseEntity deleteById(@PathVariable Long id){
        logger.debug("##New request : deleteById " + id);
        stockService.deleteById(id);
        logger.debug("##Returned stock " + stockService);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
