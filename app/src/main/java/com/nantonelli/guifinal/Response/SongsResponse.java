package com.nantonelli.guifinal.Response;

import com.google.gson.annotations.SerializedName;
import com.nantonelli.guifinal.Model.Song;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by ndantonelli on 11/18/15.
 * Simply a response object used by Retrofit to auto parse the json returned into java objects
 * The @Parcel tag at the top tells GSON to automatically convert the json into this object
 * The @SerializedName tag allows you to rename key names in the json
 * The variables without the @SerializedName tag are automatically parsed from the json using the variable name as the key name
 */
@Parcel
public class SongsResponse {
    @SerializedName("resultCount") int count;
    List<Song> results;

    public int getCount(){return count;}
    public List<Song> getResults(){return results;}
}
