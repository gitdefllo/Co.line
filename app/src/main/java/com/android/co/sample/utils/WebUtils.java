package com.android.co.sample.utils;

/****************************************************
 * Co.line
 * -----------
 * @version 0.0.3
 * @author  Fllo (@Gitdefllo) 2015
 *
 * Repository: https://github.com/Gitdefllo/Co.line.git
 *
 *****************************************************
 * Server Utils class:
 * -------------------
 *
 * Prepare utils constants variables to get elements
 * from themoviedb api (cf. https://themoviedb.org/).
 *
 *****************************************************/
public class WebUtils {
    // Base
    private static final String URL_BASE = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "?api_key=9e1143bf079e1ca43547f56fc41bb4dc";

    // URLs
    public static final String URL_DISCOVER = URL_BASE + "discover/movie" + API_KEY;
}

