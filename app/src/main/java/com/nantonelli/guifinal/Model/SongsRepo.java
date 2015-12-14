package com.nantonelli.guifinal.Model;

import android.util.Log;

import com.nantonelli.guifinal.Events.FavoritesRefreshedEvent;
import com.nantonelli.guifinal.FinalApplication;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ndantonelli on 11/19/15.
 */
public class SongsRepo {
    @Inject
    Bus eventBus;

    private List<Song> searchSongs;
    private List<Favorite> favorites;

    public SongsRepo(){
        FinalApplication.getInstance().getObjectGraph().inject(this);
        eventBus.register(this);
        searchSongs = new ArrayList<>();
        favorites = new ArrayList<>();
    }

    public List<Song> getSearchSongs(){return searchSongs;}
    public List<Favorite> getFavorites(){return favorites;}

    public void setSearchSongs(List<Song> songs){
        searchSongs=songs;
    }
    public void setFavorites(List<Favorite> favorites){
        this.favorites = favorites;
        eventBus.post(new FavoritesRefreshedEvent());
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
