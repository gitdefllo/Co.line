package com.fllo.co.line.sample.utils;

/****************************************************
 * Co.line
 * -----------
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Repository: https://github.com/Gitdefllo/Co.line.git
 *
 *****************************************************
 * Server Utils class:
 * -------------------
 *
 * Prepare utils constants variables to get elements
 * from TMDb API (cf. https://themoviedb.org/).
 *
 *****************************************************/
public class WebUtils {
    // Base
    private static final String URL_BASE = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "?api_key=9e1143bf079e1ca43547f56fc41bb4dc";

    // URLs
    public static final String URL_DISCOVER = URL_BASE + "discover/movie" + API_KEY;
    public static final String URL_SINGLE_MOVIE = URL_BASE + "movie/9693" + API_KEY;
    public static final String URL_FAKE_URL = URL_BASE + "error/" + API_KEY;
}

