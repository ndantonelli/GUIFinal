package com.nantonelli.guifinal.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ndantonelli on 12/13/15.
 */
@Table(name = "Faves")
public class Favorite extends Model {
    @Column(name = "Title")
    public String title;
    @Column(name = "CensorTitle")
    public String censorTitle;
    @Column(name = "Artist")
    public String artist;
    @Column(name = "ArtURL")
    public String artUrl;
    @Column(name = "Explicit")
    public String explicit;
    @Column(name = "Genre")
    public String genre;
    @Column(name = "PreviewURL")
    public String previewUrl;
    @Column(name = "Album")
    public String album;

    @Column(name = "Length")
    public int length;
    @Column(name = "ArtistId")
    public int artistId;
    @Column(name = "TrackId")
    public int trackId;
    @Column(name = "Price")
    public double price;

    public Favorite(){
        super();
    }

    public Favorite(Song s){
        super();
        title = s.getTitle();
        censorTitle = s.getCensorTitle();
        artist = s.getArtist();
        artUrl = s.getArtUrl();
        explicit = s.getExplicit();
        genre = s.getGenre();
        previewUrl = s.getPreviewUrl();
        length = s.getLength();
        artistId = s.getArtistId();
        trackId = s.getTrackId();
        album = s.getAlbum();
        price = s.getTrackPrice();
    }

    public boolean isExplicit(){
        if(explicit != null)
            return explicit.equals("notExplicit")?false:true;
        return false;
    }
}
