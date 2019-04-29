package com.farshad.stock.dto;

import java.util.Date;
import java.util.Objects;

public class StockDto {
    private Long    id;
    private String  name;
    private Double  currentPrice;
    private Date    lastUpdate;

    public StockDto(Long id, String name, Double currentPrice, Date lastUpdate) {
        this.id = id;
        this.name = name;
        this.currentPrice = currentPrice;
        this.lastUpdate = lastUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "StockDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockDto stockDto = (StockDto) o;
        return Objects.equals(id, stockDto.id) &&
                Objects.equals(name, stockDto.name) &&
                Objects.equals(currentPrice, stockDto.currentPrice) &&
                Objects.equals(lastUpdate, stockDto.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
