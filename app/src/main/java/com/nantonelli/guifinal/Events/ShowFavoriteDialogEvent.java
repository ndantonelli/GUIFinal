package com.nantonelli.guifinal.Events;

import com.nantonelli.guifinal.Model.Favorite;
import com.nantonelli.guifinal.Model.Song;

import java.util.Objects;

/**
 * Created by ndantonelli on 12/13/15.
 */
public class ShowFavoriteDialogEvent {
    private Favorite thing;
    public ShowFavoriteDialogEvent(Favorite obj){
        this.thing = obj;
    }

    public Favorite getThing(){return thing;}

}
