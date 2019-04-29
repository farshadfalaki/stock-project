package com.farshad.stock.exception;

public class StockDataBusinessException extends RuntimeException{
    public StockDataBusinessException(String errorMessage) {
        super(errorMessage);
    }
}
