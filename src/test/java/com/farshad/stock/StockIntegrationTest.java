package com.farshad.stock;

import com.farshad.stock.constants.ErrorMessages;
import com.farshad.stock.dto.CreateStockRequest;
import com.farshad.stock.dto.StockDto;
import com.farshad.stock.dto.UpdateStockPriceRequest;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static com.farshad.stock.constants.Constants.INIT_RECORDS_COUNT;
import static com.farshad.stock.constants.URlConstants.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StockIntegrationTest {
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
    public void retrieveAll_shouldReturnListWithFiveStockDto() throws Exception {
        //given
        //then
        mockMvc.perform(get(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(INIT_RECORDS_COUNT)))
        ;
    }

    @Test
    public void create_OneValidCreateStockRequest_shouldReturnStockDto() throws Exception {
        //given
        String companyName = "some Company";
        Double currentPrice = 23.1;
        CreateStockRequest createStockRequest = new CreateStockRequest(companyName,currentPrice);

        //then
        MvcResult mvcResult = mockMvc.perform(post(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(createStockRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",notNullValue()))
                .andExpect(jsonPath("$.name",is(createStockRequest.getName())))
                .andExpect(jsonPath("$.current_price",is(createStockRequest.getCurrentPrice())))
                .andExpect(jsonPath("$.last_update",notNullValue()))
                .andReturn()
        ;
        StockDto stockDtoCreated = gson.fromJson(mvcResult.getResponse().getContentAsString(),StockDto.class);
        //Delete created record
        mockMvc.perform(delete(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + stockDtoCreated.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent())
        ;
    }
    @Test
    public void create_OneCreateStockRequestWithNullName_shouldReturnBadRequest() throws Exception {
        //given
        String companyName = null;
        Double currentPrice = 23.1;
        CreateStockRequest createStockRequest = new CreateStockRequest(companyName,currentPrice);
        //then
        mockMvc.perform(post(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(createStockRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.startsWith(ErrorMessages.DATA_VALIDATION_EXCEPTION)))
        ;
    }

    @Test
    public void create_OneCreateStockRequestWithNullPrice_shouldReturnBadRequest() throws Exception {
        //given
        String companyName ="some Company";
        Double currentPrice = null;
        CreateStockRequest createStockRequest = new CreateStockRequest(companyName,currentPrice);

        //then
        mockMvc.perform(post(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(createStockRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.startsWith(ErrorMessages.DATA_VALIDATION_EXCEPTION)))
        ;
    }

    @Test
    public void create_oneNullCreateStockRequest_shouldReturnBadRequest() throws Exception {
        //given
        String emptyRequestBody = "{}";
        //then
        mockMvc.perform(post(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(emptyRequestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.startsWith(ErrorMessages.DATA_VALIDATION_EXCEPTION)))
        ;
    }

    @Test
    public void getById_OneNonExistingId_shouldReturnBadRequest() throws Exception {
        //given
        Long id = 146l;
        //then
        mockMvc.perform(get(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + id).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorMessages.ENTITY_NOT_FOUND)))
        ;
    }


    @Test
    public void getById_createOneStockThenGetIt_shouldReturnStockDto() throws Exception {
        //given
        String companyName = "some Company";
        Double currentPrice = 23.1;
        CreateStockRequest createStockRequest = new CreateStockRequest(companyName,currentPrice);

        //then
        MvcResult mvcResult =mockMvc.perform(post(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(createStockRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",notNullValue()))
                .andExpect(jsonPath("$.name",is(createStockRequest.getName())))
                .andExpect(jsonPath("$.current_price",is(createStockRequest.getCurrentPrice())))
                .andExpect(jsonPath("$.last_update",notNullValue())).andReturn()
        ;
        StockDto stockDtoCreated = gson.fromJson(mvcResult.getResponse().getContentAsString(),StockDto.class);


        //then
        mockMvc.perform(get(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + stockDtoCreated.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(stockDtoCreated.getId().intValue())))
                .andExpect(jsonPath("$.name",is(createStockRequest.getName())))
                .andExpect(jsonPath("$.current_price",is(createStockRequest.getCurrentPrice())))
                .andExpect(jsonPath("$.last_update",notNullValue()))
        ;
    }
    @Test
    public void updatePrice_OneUpdateStockPriceRequestWithPriceNotChanged_shouldBadRequest() throws Exception {
        //given
        Long id = 1l;
        Double newPrice = 23.3;
        UpdateStockPriceRequest updateStockPriceRequest = new UpdateStockPriceRequest(newPrice);
        //then
        mockMvc.perform(put(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + id).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(updateStockPriceRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorMessages.PRICE_NOT_CHANGED)))
        ;
    }

    @Test
    public void updatePrice_OneUpdateStockPriceRequestWithNullPrice_shouldReturnBadRequest() throws Exception {
        //given
        Long id = 146l;
        Double newPrice = null;
        UpdateStockPriceRequest updateStockPriceRequest = new UpdateStockPriceRequest(newPrice);
        //then
        mockMvc.perform(put(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + id).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(updateStockPriceRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", org.hamcrest.Matchers.startsWith(ErrorMessages.DATA_VALIDATION_EXCEPTION)))
        ;
    }

    @Test
    public void allMethods_retrieveAllThenCreateOneStockThenUpdatePriceThenGetItThenRetrieveAll_shouldReturnSixStockDto() throws Exception {
        //given
        String companyName = "my Company";
        Double currentPrice = 10.7;
        Double newPrice = 12.4;

        //then

        //retrieveAll
        mockMvc.perform(get(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(INIT_RECORDS_COUNT)))
        ;

        //create new
        CreateStockRequest createStockRequest = new CreateStockRequest(companyName,currentPrice);
        MvcResult mvcResult =mockMvc.perform(post(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(createStockRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id",notNullValue()))
                .andExpect(jsonPath("$.name",is(createStockRequest.getName())))
                .andExpect(jsonPath("$.current_price",is(createStockRequest.getCurrentPrice())))
                .andExpect(jsonPath("$.last_update",notNullValue()))
                .andReturn()
                ;
        StockDto stockDtoCreated = gson.fromJson(mvcResult.getResponse().getContentAsString(),StockDto.class);

        //updatePrice
        UpdateStockPriceRequest updateStockPriceRequest = new UpdateStockPriceRequest(newPrice);
        mvcResult =mockMvc.perform(put(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH+ stockDtoCreated.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(gson.toJson(updateStockPriceRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(stockDtoCreated.getId().intValue())))
                .andExpect(jsonPath("$.name",is(stockDtoCreated.getName())))
                .andExpect(jsonPath("$.current_price",is(updateStockPriceRequest.getCurrentPrice())))
                .andExpect(jsonPath("$.last_update",notNullValue()))
                .andReturn()

        ;
        StockDto stockDtoUpdated = gson.fromJson(mvcResult.getResponse().getContentAsString(),StockDto.class);

        //getById
        mockMvc.perform(get(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + "/" + stockDtoUpdated.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(stockDtoUpdated.getId().intValue())))
                .andExpect(jsonPath("$.name",is(stockDtoUpdated.getName())))
                .andExpect(jsonPath("$.current_price",is(stockDtoUpdated.getCurrentPrice())))
                .andExpect(jsonPath("$.last_update",notNullValue()))
        ;

        //retrieveAll
        mockMvc.perform(get(STOCK_CONTROLLER_FULL_METHOD_PATH).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(INIT_RECORDS_COUNT+1)))
        ;

        mockMvc.perform(delete(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + stockDtoUpdated.getId()).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent())
        ;

    }
    @Test
    public void deleteById_NonExistingId_ShouldBeBadRequest() throws Exception {
        //given
        Long id = 1000l;
        //when then
        mockMvc.perform(delete(STOCK_CONTROLLER_FULL_METHOD_PATH_FOLLOWING_SLASH + id).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorMessages.DATA_ACCESS_EXCEPTION)))
        ;
    }

}