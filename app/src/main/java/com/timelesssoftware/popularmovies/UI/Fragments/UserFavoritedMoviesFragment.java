package com.timelesssoftware.popularmovies.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.timelesssoftware.popularmovies.Data.PopularMoviesHelper;
import com.timelesssoftware.popularmovies.Data.PopularMoviesProvider;
import com.timelesssoftware.popularmovies.Models.MovieModel;
import com.timelesssoftware.popularmovies.Models.MoviesListModel;
import com.timelesssoftware.popularmovies.PopularMoviesApp;
import com.timelesssoftware.popularmovies.R;
import com.timelesssoftware.popularmovies.UI.Adapters.MovieListAdapter;
import com.timelesssoftware.popularmovies.UI.Adapters.MovieReviewAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link UserFavoritedMoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFavoritedMoviesFragment extends Fragment implements MovieListAdapter.OnMovieSelectListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mMovieFavoritedRv;
    private List<MovieModel> mFavoritedList;
    private MovieListAdapter mFavoritedAdapter;

    @Inject
    public PopularMoviesHelper popularMoviesHelper;

    public UserFavoritedMoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserFavoritedMoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFavoritedMoviesFragment newInstance() {
        UserFavoritedMoviesFragment fragment = new UserFavoritedMoviesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_favorited_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMovieFavoritedRv = view.findViewById(R.id.user_favorited_rv);
        mMovieFavoritedRv.setLayoutManager(new GridLayoutManager(getContext(), 2));

        if (savedInstanceState == null) {
            mFavoritedList = new ArrayList<>();
            mMovieFavoritedRv.setAdapter(mFavoritedAdapter);
            mFavoritedList = popularMoviesHelper.getFavoritedMoves();

        }
        mFavoritedAdapter = new MovieListAdapter(mFavoritedList, getContext());
        mFavoritedAdapter.setmOnMovieSelectListener(this);
        mMovieFavoritedRv.setAdapter(mFavoritedAdapter);
        mFavoritedAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        PopularMoviesApp.getNetComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onSelectMovie(int position) {

    }

    @Override
    public void onSelectMovieWithTransition(int position, ImageView imageView) {

    }

    @Override
    public void onMarkMovieFavorited(boolean state, MovieModel movieModel) {
        if (state) {
            popularMoviesHelper.markMovieAsFavorited(movieModel);
        } else {
            popularMoviesHelper.removeMovieModel(String.valueOf(movieModel.getId()));
            resetAdapter(movieModel.getId());
        }
    }

    public void resetAdapter(int movieId) {
        List<MovieModel> _tempList = mFavoritedList;

        for (int i = 0; i != _tempList.size(); i++) {
            if (movieId == _tempList.get(i).getId()) {
                mFavoritedList.remove(i);
                mFavoritedAdapter.notifyItemRemoved(i);
            }
        }
    }
}
