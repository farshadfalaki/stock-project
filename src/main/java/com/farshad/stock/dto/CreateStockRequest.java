package com.farshad.stock.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Objects;

public class CreateStockRequest {
    @NotNull(message = "stock name can not be empty")
    @Size(min = 1)
    private String  name;
    @NotNull
    @Positive
    private Double  currentPrice;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    @Override
    public String toString() {
        return "CreateStockRequest{" +
                "name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                '}';
    }

    public CreateStockRequest(String name,  Double currentPrice) {
        this.name = name;
        this.currentPrice = currentPrice;
    }

    public CreateStockRequest() { }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateStockRequest that = (CreateStockRequest) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(currentPrice, that.currentPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, currentPrice);
    }
}
