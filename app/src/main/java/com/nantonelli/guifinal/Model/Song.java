package com.nantonelli.guifinal.Model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by ndantonelli on 11/18/15.
 * The @Parcel tag at the top tells GSON to automatically convert the json into this object
 * The @SerializedName tag allows you to rename key names in the json
 * The variables without the @SerializedName tag are automatically parsed from the json using the variable name as the key name
 */
@Parcel
public class Song {
    @SerializedName("trackName") String title;
    @SerializedName("trackCensoredName") String censorTitle;
    @SerializedName("artistName") String artist;
    @SerializedName("artworkUrl100") String artUrl;
    @SerializedName("trackExplicitness") String explicit;
    @SerializedName("primaryGenreName") String genre;
    @SerializedName("collectionName") String album;
    String previewUrl;

    @SerializedName("trackTimeMillis") int length;
    int artistId;
    int trackId;
    double trackPrice;


    public String getTitle(){return title;}
    public String getCensorTitle(){return censorTitle;}
    public String getArtist(){return artist;}
    public String getPreviewUrl(){return previewUrl;}
    public String getArtUrl(){return artUrl;}
    public String getGenre(){return genre;}
    public String getExplicit(){return explicit;}
    public String getAlbum(){return album;}

    public int getArtistId(){return artistId;}
    public int getTrackId(){return trackId;}
    public int getLength(){return length;}
    public double getTrackPrice(){return trackPrice;}

    public boolean isExplicit(){
        if(explicit != null)
            return explicit.equals("notExplicit")?false:true;
        return false;
    }
}
