package com.nantonelli.guifinal.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.activeandroid.query.Select;
import com.nantonelli.guifinal.Adapter.GridAdapter;
import com.nantonelli.guifinal.Adapter.RecentAdapter;
import com.nantonelli.guifinal.Events.FlipViewEvent;
import com.nantonelli.guifinal.Events.QueriesRefreshedEvent;
import com.nantonelli.guifinal.Model.Query;
import com.nantonelli.guifinal.Model.Song;
import com.nantonelli.guifinal.R;
import com.nantonelli.guifinal.Response.SongsResponse;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by erikgabrielsen on 12/12/15.
 */
public class SearchFragment extends BaseFragment {
    @Bind(R.id.song_grid) GridView songGrid;
    @Bind(R.id.results_title) TextView resultsTitle;
    @Bind(R.id.search_query) EditText searchQuery;
    @Bind(R.id.search_button) Button searchButton;
    @Bind(R.id.search_bar) Spinner attributeType;
    @Bind(R.id.list_flipper) ViewFlipper flipper;
    @Bind(R.id.recent_search_list) ListView recents;


    private List<Song> songs;
    private GridAdapter adapter;
    private RecentAdapter recentsAdapter;
    private static final String TAG = "SEARCH_FRAGMENT";
    private static HashMap<String, String> attribute_table = new HashMap<String, String>();

    public static SearchFragment newInstance(){
        SearchFragment t = new SearchFragment();
        attribute_table.put("Mix","mixTerm");
        attribute_table.put("Genre", "genreIndex");
        attribute_table.put("Artist", "artistTerm");
        attribute_table.put("Composer", "composerTerm");
        attribute_table.put("Album", "albumTerm");
        attribute_table.put("Rating", "ratingIndex");
        attribute_table.put("Song", "songTerm");
        return t;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_grid, container, false);
        ButterKnife.bind(this, v);
        resultsTitle.setTypeface(typeface);

        songs = new ArrayList<Song>();
        adapter = new GridAdapter(getActivity(), songs);
        songGrid.setAdapter(adapter);

        recentsAdapter = new RecentAdapter(getActivity(), repo.getQueries());
        recents.setAdapter(recentsAdapter);
        recents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hide_keyboard();
                final String query = repo.getQueries().get(position).item;
                final String attr = repo.getQueries().get(position).item;
                searchQuery.setText("");
                Call<SongsResponse> call = restService.getSongs(query, 25, attribute_table.get(attr));
                call.enqueue(new Callback<SongsResponse>() {
                    @Override
                    public void onResponse(Response<SongsResponse> response, Retrofit retrofit) {
                        flipper.setDisplayedChild(1);
                        List<Song> results = response.body().getResults();
                        songs.clear();
                        for (int i = 0; i < results.size(); i++) {
                            Song temp = results.get(i);
                            picasso.load(temp.getArtUrl()).priority(Picasso.Priority.LOW).fetch();
                            songs.add(temp);
                        }
                        adapter.refresh(songs);
                        resultsTitle.setText("Results for " + query); // results for query
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();

        ArrayAdapter<CharSequence> arr_adapter = ArrayAdapter.createFromResource(getContext(), R.array.attributes_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        attributeType.setAdapter(arr_adapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide_keyboard();
                final String query = String.valueOf(searchQuery.getText());
                final String attr = attributeType.getSelectedItem().toString();
                searchQuery.setText("");
                Call<SongsResponse> call = restService.getSongs(query, 25, attribute_table.get(attr));
                call.enqueue(new Callback<SongsResponse>() {
                    @Override
                    public void onResponse(Response<SongsResponse> response, Retrofit retrofit) {
                        flipper.setDisplayedChild(1);//display the results gridview
                        new Query(query, attr).save();// save the query to the database
                        List<Query> recents = new Select().all().from(Query.class).orderBy("Time DESC").limit(5).execute();
                        repo.setQueries(recents);
                        List<Song> results = response.body().getResults();
                        songs.clear();
                        for (int i = 0; i < results.size(); i++) {
                            Song temp = results.get(i);
                            picasso.load(temp.getArtUrl()).priority(Picasso.Priority.LOW).fetch();
                            songs.add(temp);
                        }
                        adapter.refresh(songs);
                        resultsTitle.setText("Results for " + query); // results for query
                        resultsTitle.setTypeface(typeface);
                    }

                    @Override
                    public void onFailure(Throwable t) {

                    }
                });
            }
        });
    }

    @Subscribe
    //we have added new queries from the database
    public void newQueries(QueriesRefreshedEvent event){
        recentsAdapter.clear();
        recentsAdapter.addAll(repo.getQueries());
    }

    @Subscribe
    //the search tab has been clicked again so return to the recents list
    public void flipBack(FlipViewEvent event){
        resultsTitle.setText("Recent Searches");
        flipper.setDisplayedChild(0);
    }

}
