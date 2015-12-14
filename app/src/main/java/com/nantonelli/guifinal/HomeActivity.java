package com.nantonelli.guifinal;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.nantonelli.guifinal.Adapter.PagerAdapter;
import com.nantonelli.guifinal.Events.FlipViewEvent;
import com.nantonelli.guifinal.Events.ShowFavoriteDialogEvent;
import com.nantonelli.guifinal.Events.ShowSongDialogEvent;
import com.nantonelli.guifinal.Fragment.DialogFrag;
import com.nantonelli.guifinal.Model.Favorite;
import com.nantonelli.guifinal.Model.Query;
import com.nantonelli.guifinal.Model.Song;
import com.nantonelli.guifinal.Model.SongsRepo;
import com.nantonelli.guifinal.Response.SongsResponse;
import com.squareup.otto.Subscribe;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by ndantonelli on 11/18/15.
 * This activity is the entry point for fragments and activities in the app
 * FinalApplication is technically called first so that the application context is created
 *      before any activities or fragments
 *
 */

public class HomeActivity extends BaseActivity {
    @Inject SongsRepo repo;

    @Bind(R.id.tabLayout)TabLayout tabs;
    @Bind(R.id.pager) ViewPager pager;

    private PagerAdapter adapter;

    private static final String TAG = "HOME_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setupToolbar();
        setupTabs();

        adapter = new PagerAdapter(getSupportFragmentManager(), tabs.getTabCount());

        pager.setAdapter(adapter);

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                Log.d(TAG, String.valueOf(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                postEvent(new FlipViewEvent());
            }
        });
        List<Favorite> faves = new Select().all().from(Favorite.class).execute();
        repo.setFavorites(faves);
        List<Query> recents = new Select().all().from(Query.class).orderBy("Time DESC").limit(5).execute();
        repo.setQueries(recents);
    }

    private void setupTabs(){
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        RelativeLayout tabOne = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.component_tab_left, null);
        TextView text = (TextView)tabOne.findViewById(R.id.tab_title);
        text.setText("Search");
        text.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.search, 0, 0);
        text.setTypeface(typeface);
        tabs.addTab(tabs.newTab().setCustomView(tabOne));

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.component_tab, null);
        tabTwo.setText("Favorites");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.heart, 0, 0);
        tabTwo.setTypeface(typeface);
        tabs.addTab(tabs.newTab().setCustomView(tabTwo));
    }

    @Subscribe
    public void somethingSong(ShowSongDialogEvent event){
        new DialogFrag(event.getSong()).show(getSupportFragmentManager(), "SongDialogFrag");
    }

    @Subscribe
    public void somethingFavorite(ShowFavoriteDialogEvent event){
        new DialogFrag(event.getThing()).show(getSupportFragmentManager(), "SongDialogFrag");
    }
}
