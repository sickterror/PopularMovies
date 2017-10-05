package com.timelesssoftware.popularmovies.Models;

import java.util.List;

/**
 * Created by Luka on 3. 10. 2017.
 */

public class MoviesListModel {
    public int page;
    public int total_results;
    public int total_pages;
    public List<MovieModel> results;
}
