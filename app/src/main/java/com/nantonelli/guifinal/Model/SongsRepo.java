package com.nantonelli.guifinal.Model;

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
    private List<Song> favorites;

    public SongsRepo(){
        FinalApplication.getInstance().getObjectGraph().inject(this);
        eventBus.register(this);
        searchSongs = new ArrayList<>();
        favorites = new ArrayList<>();
    }

    public List<Song> getSearchSongs(){return searchSongs;}
    public List<Song> getFavorites(){return favorites;}

    public void setSearchSongs(List<Song> songs){
        searchSongs=songs;
    }
}
