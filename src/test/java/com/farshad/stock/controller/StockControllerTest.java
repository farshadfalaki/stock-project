package com.farshad.stock.controller;

import com.farshad.stock.constants.ErrorMessages;
import com.farshad.stock.dto.CreateStockRequest;
import com.farshad.stock.dto.StockDto;
import com.farshad.stock.dto.UpdateStockPriceRequest;
import com.farshad.stock.exception.StockDataBusinessException;
import com.farshad.stock.service.StockService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Date;
import static com.farshad.stock.constants.ErrorMessages.PRICE_NOT_CHANGED;
import static com.farshad.stock.constants.URlConstants.STOCK_CONTROLLER_FULL_METHOD_PATH;
import static com.farshad.stock.constants.URlConstants.STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = StockController.class)
public class StockControllerTest {
    @MockBean
    private StockService stockService;
    @Autowired
    private MockMvc mockMvc;

    private Gson gson;

    @Before
    public void init(){
        gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Test
    public void retrieveAll_ShouldReturnListWithOneStockDto() throws Exception {
        //given
        Long id = 1000l;
        String companyName = "some Company";
        Double currentPrice = 23.1;
        Date lastUpdateDate = new Date();
        StockDto expectedStockDto = new StockDto(id,companyName,currentPrice,lastUpdateDate);
        when(stockService.retrieveAll()).thenReturn(Arrays.asList(expectedStockDto));

        //when then
        mockMvc.perform(get(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id",is(new Long(expectedStockDto.getId()).intValue())))
        .andExpect(jsonPath("$[0].name",is(expectedStockDto.getName())))
        .andExpect(jsonPath("$[0].current_price",is(expectedStockDto.getCurrentPrice())))
        ;
    }

    @Test
    public void retrieveAll_ShouldReturnListWithTwoStockDto() throws Exception {
        //given
        Long id = 1000l;
        String companyName = "some Company";
        Double currentPrice = 23.1;
        Date lastUpdateDate = new Date();
        Long id2 = 1001l;
        String companyName2 = "some Company2";
        Double currentPrice2 = 23.2;
        Date lastUpdateDate2 = new Date();
        StockDto expectedStockDto = new StockDto(id,companyName,currentPrice,lastUpdateDate);
        StockDto expectedStockDto2 = new StockDto(id2,companyName2,currentPrice2,lastUpdateDate2);
        when(stockService.retrieveAll()).thenReturn(Arrays.asList(expectedStockDto,expectedStockDto2));

        mockMvc.perform(get(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
        ;
    }

    @Test
    public void create_OneValidCreateStockRequest_ShouldReturnStockDto() throws Exception {
        //given
        Long id = 146l;
        String companyName = "some Company";
        Double currentPrice = 23.1;
        Date lastUpdateDate = new Date();
        CreateStockRequest createStockRequest = new CreateStockRequest(companyName,currentPrice);
        StockDto expectedStockDto = new StockDto(id,companyName,currentPrice,lastUpdateDate);
        when(stockService.create(createStockRequest)).thenReturn(expectedStockDto);

        //when then
        mockMvc.perform(post(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(createStockRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",is(expectedStockDto.getId().intValue())))
                .andExpect(jsonPath("$.name",is(createStockRequest.getName())))
                .andExpect(jsonPath("$.current_price",is(createStockRequest.getCurrentPrice())))
                .andExpect(jsonPath("$.last_update",notNullValue()))
        ;
        verify(stockService).create(createStockRequest);
    }

    @Test
    public void create_OneCreateStockRequestWithNullName_ShouldReturnBadRequest() throws Exception {
        //given
        String companyName = null;
        Double currentPrice = 23.1;
        CreateStockRequest createStockRequest = new CreateStockRequest(companyName,currentPrice);
        //when then
        mockMvc.perform(post(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(createStockRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.startsWith(ErrorMessages.DATA_VALIDATION_EXCEPTION)))
        ;
        verify(stockService,never()).create(createStockRequest);
    }

    @Test
    public void create_OneCreateStockRequestWithNullPrice_ShouldReturnBadRequest() throws Exception {
        //given
        String companyName ="some Company";
        Double currentPrice = null;
        CreateStockRequest createStockRequest = new CreateStockRequest(companyName,currentPrice);

        //when then
        mockMvc.perform(post(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(createStockRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.startsWith(ErrorMessages.DATA_VALIDATION_EXCEPTION)))
        ;
        verify(stockService,never()).create(createStockRequest);
    }

    @Test
    public void create_oneNullCreateStockRequest_ShouldReturnBadRequest() throws Exception {
        //given
        String requestBody = "{}";
        //when then
        mockMvc.perform(post(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.startsWith(ErrorMessages.DATA_VALIDATION_EXCEPTION)))
        ;
        verify(stockService,never()).create(any());
    }

    @Test
    public void getById_OneExistingId_ShouldReturnStockDto() throws Exception {
        //given
        Long id = 146l;
        String companyName = "some Company";
        Double currentPrice = 23.1;
        Date lastUpdateDate = new Date();
        StockDto expectedStockDto = new StockDto(id,companyName,currentPrice,lastUpdateDate);
        when(stockService.getById(id)).thenReturn(expectedStockDto);

        //when then
        mockMvc.perform(get(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + id).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(expectedStockDto.getId().intValue())))
                .andExpect(jsonPath("$.name",is(expectedStockDto.getName())))
                .andExpect(jsonPath("$.current_price",is(expectedStockDto.getCurrentPrice())))
                .andExpect(jsonPath("$.last_update",notNullValue()))
        ;
        verify(stockService).getById(id);
    }

    @Test
    public void getById_OneNonExistingId_ShouldReturnBadRequest() throws Exception {
        //given
        Long id = 146l;
        when(stockService.getById(id)).thenThrow(new EntityNotFoundException());

        //when then
        mockMvc.perform(get(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + id).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.startsWith(ErrorMessages.ENTITY_NOT_FOUND)))
        ;
        verify(stockService).getById(id);
    }


    @Test
    public void updatePrice_OneValidUpdateStockPriceRequest_ShouldReturnStockDto() throws Exception {
        //given
        Long id = 146l;
        String companyName = "some Company";
        Double newPrice = 23.2;
        Date lastUpdateDate = new Date();
        UpdateStockPriceRequest updateStockPriceRequest = new UpdateStockPriceRequest(newPrice);
        StockDto expectedStockDto = new StockDto(id,companyName,newPrice,lastUpdateDate);
        when(stockService.updatePrice(id,updateStockPriceRequest)).thenReturn(expectedStockDto);

        //when then
        mockMvc.perform(put(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + id).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(updateStockPriceRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(expectedStockDto.getId().intValue())))
                .andExpect(jsonPath("$.name",is(expectedStockDto.getName())))
                .andExpect(jsonPath("$.current_price",is(expectedStockDto.getCurrentPrice())))
                .andExpect(jsonPath("$.last_update",notNullValue()))
        ;
        verify(stockService).updatePrice(id,updateStockPriceRequest);
    }


    @Test
    public void updatePrice_OneUpdateStockPriceRequestWithPriceNotChanged_ShouldReturnStockDto() throws Exception {
        //given
        Long id = 146l;
        Double newPrice = 23.2;
        UpdateStockPriceRequest updateStockPriceRequest = new UpdateStockPriceRequest(newPrice);
        when(stockService.updatePrice(id,updateStockPriceRequest)).thenThrow(new StockDataBusinessException(PRICE_NOT_CHANGED));

        //when then
        mockMvc.perform(put(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + id).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(updateStockPriceRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorMessages.PRICE_NOT_CHANGED)))
        ;
        verify(stockService).updatePrice(id,updateStockPriceRequest);
    }


    @Test
    public void updatePrice_OneUpdateStockPriceRequestWithNullPrice_ShouldReturnStockDto() throws Exception {
        //given
        Long id = 146l;
        Double newPrice = null;
        UpdateStockPriceRequest updateStockPriceRequest = new UpdateStockPriceRequest(newPrice);
        when(stockService.updatePrice(id,updateStockPriceRequest)).thenThrow(new StockDataBusinessException(PRICE_NOT_CHANGED));

        //when then
        mockMvc.perform(put(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + id).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(updateStockPriceRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
        verify(stockService,never()).updatePrice(id,updateStockPriceRequest);
    }


    @Test
    public void deleteById_OneExistingId_ShouldBeDeleted() throws Exception {
        //given
        Long id = 1l;
        //then
        mockMvc.perform(delete(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + id).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent())
        ;
        verify(stockService).deleteById(id);
    }

    @Test
    public void deleteById_NonExistingId_ShouldBeBadRequest() throws Exception {
        //given
        Long id = 1000l;
        doThrow(new EmptyResultDataAccessException(0)).doNothing().when(stockService).deleteById(id);
        //then
        mockMvc.perform(delete(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + id).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorMessages.DATA_ACCESS_EXCEPTION)))
        ;
        verify(stockService).deleteById(id);
    }
}