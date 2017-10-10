package com.timelesssoftware.popularmovies.UI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.timelesssoftware.popularmovies.Activities.MovieDeatiledActivity;
import com.timelesssoftware.popularmovies.Models.MovieModel;
import com.timelesssoftware.popularmovies.Models.MoviesListModel;
import com.timelesssoftware.popularmovies.PopularMoviesApp;
import com.timelesssoftware.popularmovies.R;
import com.timelesssoftware.popularmovies.Utils.Adapters.MovieListAdapter;
import com.timelesssoftware.popularmovies.Utils.Network.ApiHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link MostViewdMoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MostViewdMoviesFragment extends Fragment implements MovieListAdapter.OnMovieSelectListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String API_ENDPOINT = "apiEndpoint";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View mRootVew;

    @Inject
    ApiHandler apiHandler;
    private RecyclerView movieListRv;
    private List<MovieModel> movieModelList;
    private MovieListAdapter movieListAdapter;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private GridLayoutManager mLayoutManager;
    private int currentPage = 1;
    private int previousTotal = 0;

    private int currentSelectedFilter = R.id.order_most_viewed;
    private HashMap<String, String> params;
    private Parcelable mListState;
    private String mApiEndoint;


    public MostViewdMoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MostViewdMoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MostViewdMoviesFragment newInstance(String apiEndPoint) {
        MostViewdMoviesFragment fragment = new MostViewdMoviesFragment();
        Bundle args = new Bundle();
        args.putString(API_ENDPOINT, apiEndPoint);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mApiEndoint = getArguments().getString(API_ENDPOINT);
        }
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_most_viewd_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getResources().getConfiguration().orientation == OrientationHelper.VERTICAL) {
            mLayoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            mLayoutManager = new GridLayoutManager(getContext(), 3);
        }

        movieListRv = view.findViewById(R.id.most_viewed_rv);
        movieListRv.setAnimation(null);
        params = new HashMap<>();
        if (savedInstanceState == null) {
            params.put("sort_by", "popularity.desc");
            params.put("page", Integer.toString(currentPage));
            apiHandler.get(mApiEndoint, params, MoviesListModel.class).observeOn(AndroidSchedulers.mainThread()).subscribe(moviesListModelConsumer);
        }

        if (savedInstanceState != null) {
            movieModelList = savedInstanceState.getParcelableArrayList("movies");
        } else {
            movieModelList = new ArrayList<>();
        }

        movieListRv.setLayoutManager(mLayoutManager);
        movieListRv.addOnScrollListener(onScrollListener);
        movieListAdapter = new MovieListAdapter(movieModelList, getContext());
        movieListAdapter.setmOnMovieSelectListener(this);
        movieListRv.setAdapter(movieListAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        PopularMoviesApp.getNetComponent().inject(this);
    }

    public void resetRvAdapter() {
        movieModelList.clear();
        movieListAdapter.notifyDataSetChanged();
        currentPage = 1;
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) //check for scroll down
            {
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    currentPage++;
                    params.put("page", Integer.toString(currentPage));
                    apiHandler.get("discover/movie", params, MoviesListModel.class).observeOn(AndroidSchedulers.mainThread()).subscribe(moviesListModelConsumer);
                    loading = true;
                }
            }
        }
    };

    private Consumer<MoviesListModel> moviesListModelConsumer = new Consumer<MoviesListModel>() {
        @Override
        public void accept(MoviesListModel moviesListModel) throws Exception {
            List<MovieModel> movieModels = moviesListModel.results;
            if (movieModels == null) {
                Snackbar.make(movieListRv, "Can't find any movies?!?", Snackbar.LENGTH_LONG).show();
                return;
            }

            int count = movieModelList.size();
            for (MovieModel _movieModel : movieModels) {
                movieModelList.add(_movieModel);
                movieListAdapter.notifyItemInserted(count);
                count++;
            }
        }
    };

    @Override
    public void onSelectMovie(int position) {
        Log.d("MainActivity", position + "");
    }

    @Override
    public void onSelectMovieWithTransition(int position, ImageView imageView) {
        MovieModel movieModel = movieModelList.get(position);
        Intent intent = new Intent(getContext(), MovieDeatiledActivity.class);
        intent.putExtra("movieModel", movieModel);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(getActivity(),
                        imageView,
                        ViewCompat.getTransitionName(imageView));
        startActivity(intent, options.toBundle());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movies", new ArrayList<>(movieModelList));
        outState.putInt("position", currentPage);
    }

    /*@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("listState", mListState);
        outState.putInt("currentPage", currentPage);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("MostViewed", "onViewStateRestored");
        if (savedInstanceState != null) {
            Log.d("MostViewed", "onViewStateRestored != null");
            mListState = savedInstanceState.getParcelable("listState");
            currentPage = savedInstanceState.getInt("currentPage");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MostViewed", "onResume");
        if (mListState != null) {
            Log.d("MostViewed", "onResume != null");
            mLayoutManager.onRestoreInstanceState(mListState);
            movieListAdapter.notifyDataSetChanged();
        }
    }*/
}
