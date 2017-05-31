package com.codiansoft.foodtruck.Models;


import java.util.Arrays;
import java.util.List;

public class GenreDataFactory {

    public static List<USERSORDERS> makeGenres() {
        return Arrays.asList(
                makeJazzGenre()
        );
    }


    public static USERSORDERS makeJazzGenre() {
        return new USERSORDERS("", "ist", "2nd", "third", "", "", "", "", makeJazzArtists());
    }

    public static List<USERORDERMENUITEMS> makeJazzArtists() {
        USERORDERMENUITEMS milesDavis = new USERORDERMENUITEMS("Miles Davis", "5", "10");
        USERORDERMENUITEMS ellaFitzgerald = new USERORDERMENUITEMS("Ella Fitzgerald", "5", "4");
        USERORDERMENUITEMS billieHoliday = new USERORDERMENUITEMS("Billie Holiday", "7", "5");

        return Arrays.asList(milesDavis, ellaFitzgerald, billieHoliday);
    }

}

