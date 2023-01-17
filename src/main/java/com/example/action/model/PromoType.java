package com.example.action.model;

public enum PromoType {
    FIRST_TRIP(50, 0),
    TENTH_TRIP(30,9);

    private final Integer discount;
    private final Integer numberTrips;

    PromoType(Integer discount, Integer numberTrips) {
        this.discount = discount;
        this.numberTrips = numberTrips;
    }

    public Integer getDiscount() {
        return discount;
    }

    public Integer getNumberTrips() {
        return numberTrips;
    }
}