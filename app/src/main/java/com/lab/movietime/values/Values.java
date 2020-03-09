package com.lab.movietime.values;

public class Values {
    public static String baseURL = "https://api.myjson.com/bins/";
    public static String[] CATEGORY = {"popular","now_playing","upcoming"};
    public static String LANGUAGE = "en-US";
    public static int[] PAGE = {1,2,3,4,5};
    public static int[] GENRE = {28, 12, 16, 18, 35, 80, 99, 10751, 14, 36, 27, 10402, 9648, 10749, 878, 10770, 53, 10752, 37};
    public static boolean ADULT = false;
    public static String[] SORT_BY = {"popularity.desc","popularity.asc","Fantasy"};

    public static String identifyGenre(int id) {
        switch (id) {
            case 28:
                return "Action";
            case 12:
                return "Adventure";
            case 16:
                return "Animation";
            case 35:
                return "Comedy";
            case 80:
                return "Crime";
            case 99:
                return "Documentary";
            case 18:
                return "Drama";
            case 10751:
                return "Family";
            case 14:
                return "Fantasy";
            case 36:
                return "History";
            case 27:
                return "Horror";
            case 10402:
                return "Music";
            case 9648:
                return "Mystery";
            case 10749:
                return "Romance";
            case 878:
                return "Science Fiction";
            case 10770:
                return "TV Movie";
            case 53:
                return "Thriller";
            case 10752:
                return "War";
            case 37:
                return "Western";
            default:
                return "Unknown";
        }
    }

}
