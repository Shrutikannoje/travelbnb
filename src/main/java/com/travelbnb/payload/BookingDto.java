package com.travelbnb.payload;


import com.travelbnb.entity.AppUser;
import com.travelbnb.entity.Property;
import lombok.Getter;
import lombok.Setter;


public class BookingDto {

    @Getter
    @Setter
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private Integer price;
    private Integer totalNights;
    private AppUser appUser;
    private Property property;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTotalNights() {
        return totalNights;
    }

    public void setTotalNights(Integer totalNights) {
        this.totalNights = totalNights;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}

