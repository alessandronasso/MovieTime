package com.lab.movietime.values;

import java.util.HashMap;

public class Values {
    public static String baseURL = "https://api.myjson.com/bins/";
    public static String[] CATEGORY = {"popular","now_playing","upcoming"};
    public static String LANGUAGE = "en-US";
    public static int[] PAGE = {1,2,3,4,5};
    public static int[] GENRE = {28, 12, 16, 35, 80, 99, 18, 10751, 14, 36, 27, 10402, 9648, 10749, 878, 10770, 53, 10752, 37};
    public static String[] GENRE_BY_NAME = {"Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama",
    "Family", "Fantasy", "History", "Horror", "Music", "Mystery", "Romance", "Science Fiction", "TV Movie", "Thriller", "War", "Western"};
    public static boolean ADULT = false;
    public static String[] SORT_BY = {"popularity.desc","popularity.asc","Fantasy"};
    public static HashMap<Integer, String> MAP_NAME = initializeMap();

    private static HashMap initializeMap() {
        HashMap<Integer, String> tmp = new HashMap<>();
        for (int i = 0; i < GENRE.length; i++)
            tmp.put(GENRE[i], GENRE_BY_NAME[i]);
        return tmp;
    }
}
