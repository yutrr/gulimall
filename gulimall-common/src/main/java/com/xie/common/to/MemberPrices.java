/**
  * Copyright 2022 bejson.com 
  */
package com.xie.common.to;



import java.math.BigDecimal;

/**
 * Auto-generated: 2022-08-13 11:53:22
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */

public class MemberPrices {

    private Long id;
    private String name;
    private BigDecimal price;

    public MemberPrices() {
    }

    public MemberPrices(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "MemberPrice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}