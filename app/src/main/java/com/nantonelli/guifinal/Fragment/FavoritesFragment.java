package com.nantonelli.guifinal.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.nantonelli.guifinal.Adapter.FavoritesAdapter;
import com.nantonelli.guifinal.Adapter.GridAdapter;
import com.nantonelli.guifinal.Events.FavoritesRefreshedEvent;
import com.nantonelli.guifinal.Model.Favorite;
import com.nantonelli.guifinal.Model.Song;
import com.nantonelli.guifinal.Model.SongsRepo;
import com.nantonelli.guifinal.R;
import com.nantonelli.guifinal.Response.SongsResponse;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ndantonelli on 11/19/15.
 */
public class FavoritesFragment extends BaseFragment{

    @Bind(R.id.song_grid) GridView songGrid;
    private FavoritesAdapter adapter;
    private static final String TAG = "FAVORITES_FRAGMENT";

    public static FavoritesFragment newInstance(){
        FavoritesFragment t = new FavoritesFragment();
        return t;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_faves, container, false);
        ButterKnife.bind(this, v);
        adapter = new FavoritesAdapter(getActivity(), repo.getFavorites());
        songGrid.setAdapter(adapter);
        return v;
    }

    @Subscribe
    public void newFavorite(FavoritesRefreshedEvent event){
        adapter.refresh(repo.getFavorites());
    }
}
