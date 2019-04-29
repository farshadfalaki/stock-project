package com.farshad.stock.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Stock {
    @Id
    @GeneratedValue(generator = "stockSeq" , strategy = GenerationType.AUTO)
    @SequenceGenerator(name = "stockSeq" ,sequenceName = "stock_seq" , initialValue = 10)
    private Long    id;
    @Column(nullable = false)
    private String  name;
    @Column(nullable = false)
    private Double  currentPrice;
    @Column(nullable = false)
    private Date    lastUpdate;

    //serialize
    public Stock(){}

    public Stock(String name, Double currentPrice, Date lastUpdate) {
        this.name = name;
        this.currentPrice = currentPrice;
        this.lastUpdate = lastUpdate;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id) &&
                Objects.equals(name, stock.name) &&
                Objects.equals(currentPrice, stock.currentPrice) &&
                Objects.equals(lastUpdate, stock.lastUpdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currentPrice=" + currentPrice +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static void main(String[] args) {
        Date r= new Date();
        Double d = new Double(34.1);
        Double d2 = new Double(34.1);
        Stock s1 = new Stock("s",d,r);
        Stock s2 = new Stock("s",d2,r);
        System.out.println(s1.equals(s2));
    }


}
