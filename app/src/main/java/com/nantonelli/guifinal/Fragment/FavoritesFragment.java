package com.nantonelli.guifinal.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.nantonelli.guifinal.Adapter.GridAdapter;
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
    @Bind(R.id.results_title) TextView resultsTitle;
    private List<Song> songs;
    private GridAdapter adapter;
    private static final String TAG = "FAVORITES_FRAGMENT";

    public static FavoritesFragment newInstance(){
        FavoritesFragment t = new FavoritesFragment();
        return t;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_grid, container, false);
        ButterKnife.bind(this, v);
        songs = new ArrayList<>();
        adapter = new GridAdapter(getActivity(), songs);
        songGrid.setAdapter(adapter);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
        Call<SongsResponse> call = restService.getSongs("jack johnson", 25);
        call.enqueue(new Callback<SongsResponse>() {
            @Override
            public void onResponse(Response<SongsResponse> response, Retrofit retrofit) {

                List<Song> results = response.body().getResults();
                songs.clear();
                for(int i = 0; i < results.size(); i++) {
                    Song temp = results.get(i);
                    picasso.load(temp.getArtUrl()).priority(Picasso.Priority.LOW).fetch();
                    songs.add(temp);
                }
                adapter.refresh(songs);
                resultsTitle.setText("Results for Jack Johnson"); // results for query
                resultsTitle.setTypeface(typeface);
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}
