package com.nantonelli.guifinal.Fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nantonelli.guifinal.Model.Favorite;
import com.nantonelli.guifinal.Model.Song;
import com.nantonelli.guifinal.R;

/**
 * Created by ndantonelli on 12/10/15.
 */
public class DialogFrag extends DialogFragment {
    private String title;
    private String album;
    private String artist;
    private String genre;
    private double price;

    public DialogFrag(){
        super();
    }

    public DialogFrag(Song song){
        super();
        title = song.getTitle();
        album = song.getAlbum();
        artist = song.getArtist();
        genre = song.getGenre();
        price = song.getTrackPrice();
    }

    public DialogFrag(Favorite favorite){
        super();
        title = favorite.title;
        album =favorite.album;
        artist = favorite.artist;
        genre = favorite.genre;
        price = favorite.price;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder createProjectAlert = new AlertDialog.Builder(getActivity());

        createProjectAlert.setTitle(title);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_dialog, null);
        ((TextView) v.findViewById(R.id.dia_album)).setText("Album: " + album);
        ((TextView) v.findViewById(R.id.dia_artist)).setText("Artist: " + artist);
        ((TextView) v.findViewById(R.id.dia_genre)).setText("Genre: " + genre);
        ((TextView) v.findViewById(R.id.dia_price)).setText("Price: $" + price);

        createProjectAlert.setView(v)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return createProjectAlert.create();

    }

}