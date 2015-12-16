package com.nantonelli.guifinal.Model;

import android.util.Log;

import com.nantonelli.guifinal.Events.FavoritesRefreshedEvent;
import com.nantonelli.guifinal.Events.QueriesRefreshedEvent;
import com.nantonelli.guifinal.FinalApplication;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by ndantonelli on 11/19/15.
 */
public class SongsRepo {
    @Inject Bus eventBus;

    private List<Favorite> favorites;
    private List<Query> queries;

    public SongsRepo(){
        FinalApplication.getInstance().getObjectGraph().inject(this);
        eventBus.register(this);
        favorites = new ArrayList<>();
        queries = new ArrayList<>();
    }

    public List<Favorite> getFavorites(){return favorites;}
    public List<Query> getQueries(){return queries;}

    public void setFavorites(List<Favorite> favorites){
        this.favorites = favorites;
        eventBus.post(new FavoritesRefreshedEvent());
    }

    public void setQueries(List<Query> queries){
        this.queries = queries;
        eventBus.post(new QueriesRefreshedEvent());
    }

    public void addFavorite(Favorite fave){
        favorites.add(fave);
        eventBus.post(new FavoritesRefreshedEvent());
    }

    public boolean isFavorite(Song s){
        for(int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).trackId == s.getTrackId())
                return true;
        }
        return false;
    }
}
