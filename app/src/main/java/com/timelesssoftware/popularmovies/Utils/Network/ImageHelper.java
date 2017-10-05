package com.timelesssoftware.popularmovies.Utils.Network;

/**
 * Created by Luka on 3. 10. 2017.
 */

public class ImageHelper {
    //http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
    public static String generateImageUrl(String image, String imageSize) {
        return "http://image.tmdb.org/t/p/" + imageSize + "/" + image;
    }

    public static class ImageSizes {
        public static final String w92 = "w92";
        public static final String w154 = "w154";
        public static final String w185 = "w185";
        public static final String w342 = "w342";
        public static final String w500 = "w500";
        public static final String w780 = "w500";
        public static final String original = "original";
    }
}
