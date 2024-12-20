package com.geodetic_system.configurationLoader;

import com.geodetic_system.singleton.Country;
import com.geodetic_system.singleton.CountryCoordinates;

public class ConfigurationLoader {
    public static Country getCountryFromConfig() {
        String countryName = System.getProperty("target.country", "Slovakia");
        return CountryCoordinates.valueOf(countryName.toUpperCase()).getCountry();
    }
}
