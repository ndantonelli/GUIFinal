package com.nantonelli.guifinal.Events;

import com.nantonelli.guifinal.Model.Song;

/**
 * Created by ndantonelli on 12/13/15.
 */
public class ShowSongDialogEvent {
    private Song song;
    public ShowSongDialogEvent(Song s){
        this.song = s;
    }
    public Song getSong(){
        return song;
    }
}
