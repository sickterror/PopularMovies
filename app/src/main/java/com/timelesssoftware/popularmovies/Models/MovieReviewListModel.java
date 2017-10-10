package com.timelesssoftware.popularmovies.Models;

import java.util.List;

/**
 * Created by Luka on 10. 10. 2017.
 */

public class MovieReviewListModel<T> {
    public int page;
    public int total_results;
    public int total_pages;
    public List<T> results;
}
