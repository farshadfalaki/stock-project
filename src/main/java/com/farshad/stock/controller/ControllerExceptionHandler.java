package com.farshad.stock.controller;

import com.farshad.stock.constants.ErrorMessages;
import com.farshad.stock.dto.ErrorResponse;
import com.farshad.stock.exception.StockDataBusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

import static com.farshad.stock.constants.ErrorMessages.DATA_ACCESS_EXCEPTION;
import static com.farshad.stock.constants.ErrorMessages.DATA_VALIDATION_EXCEPTION;
import static com.farshad.stock.constants.ErrorMessages.ENTITY_NOT_FOUND;

@RestControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EntityNotFoundException.class})
    public ErrorResponse entityNotFoundException(EntityNotFoundException e) {
        logger.debug("Caught an exception " + e.getMessage());
        return new ErrorResponse(ENTITY_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({DataAccessException.class})
    public ErrorResponse dataAccessException(DataAccessException e) {
        logger.debug("Caught an exception " + e.getMessage());
        logger.debug("Root cause " + e.getRootCause());
        return new ErrorResponse(DATA_ACCESS_EXCEPTION);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({StockDataBusinessException.class})
    public ErrorResponse stockDataBusinessException(StockDataBusinessException e) {
        logger.debug("Caught an exception " + e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validationException(MethodArgumentNotValidException e) {
        logger.debug("Caught a validation exception " + e.getMessage());
        return new ErrorResponse(DATA_VALIDATION_EXCEPTION +" " + e.getMessage());
    }
}