package com.geodetic_system.singleton;

public enum CountryCoordinates {
    SLOVAKIA(new Country(16.833333, 49.6, 22.566667, 47.75));

    private final Country country;

    CountryCoordinates(Country country) {
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }
}