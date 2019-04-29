package com.farshad.stock.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class UpdateStockPriceRequest {
    @NotNull
    private Double  currentPrice;

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public String toString() {
        return "UpdateStockPriceRequest{" +
                "currentPrice=" + currentPrice +
                '}';
    }

    public UpdateStockPriceRequest(Double currentPrice) {
        this.currentPrice = currentPrice;
    }
    public UpdateStockPriceRequest() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateStockPriceRequest that = (UpdateStockPriceRequest) o;
        return Objects.equals(currentPrice, that.currentPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentPrice);
    }
}
