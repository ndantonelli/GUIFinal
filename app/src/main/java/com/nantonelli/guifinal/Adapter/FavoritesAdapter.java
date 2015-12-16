package com.nantonelli.guifinal.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.nantonelli.guifinal.Events.ShowFavoriteDialogEvent;
import com.nantonelli.guifinal.Events.ShowSongDialogEvent;
import com.nantonelli.guifinal.FinalApplication;
import com.nantonelli.guifinal.Model.Favorite;
import com.nantonelli.guifinal.Model.Song;
import com.nantonelli.guifinal.R;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ndantonelli on 11/19/15.
 * duplicate of the GridAdapter.  Due to time constraints, it is the same code, slightly adapter
 * used for favorites rather than songs
 */
public class FavoritesAdapter extends BaseAdapter {
    private static class ViewHolder{
        ImageView image;
        TextView text;
        ViewFlipper flipper;
        ProgressBar progress;
        ImageView pause;
        ImageView play;
        MediaRunnable runnable;
        ImageView star;
    }
    @Inject Picasso picasso;
    @Inject Typeface typeface;
    @Inject Bus eventBus;
    
    private Context mContext;
    private List<Favorite> songs;
    private Handler mHandler;
    private MediaPlayer player;

    int playingPos = -1;

    public FavoritesAdapter(Context context, List<Favorite> events){
        this.songs = events;
        mContext = context;
        FinalApplication.getInstance().getObjectGraph().inject(this);
        mHandler = new Handler();
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public void refresh(List<Favorite> songs){
        this.songs = songs;
    }
    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vHold;
        final Favorite temp = songs.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            vHold = new ViewHolder();
            convertView = inflater.inflate(R.layout.component_grid_item, parent, false);
            vHold.text= (TextView) convertView.findViewById(R.id.title);
            vHold.text.setTypeface(typeface);
            vHold.image= (ImageView)convertView.findViewById(R.id.back_img);
            vHold.flipper = (ViewFlipper) convertView.findViewById(R.id.play_pause_flipper);
            vHold.progress = (ProgressBar) convertView.findViewById(R.id.progressBar);
            vHold.pause = (ImageView) convertView.findViewById(R.id.pause_button);
            vHold.play = (ImageView) convertView.findViewById(R.id.play_button);
            vHold.runnable = new MediaRunnable(vHold);
            vHold.star = (ImageView) convertView.findViewById(R.id.favorite_star);
            convertView.setTag(vHold);
        } else {
            vHold = (ViewHolder)convertView.getTag();
            vHold.runnable = new MediaRunnable(vHold);
            vHold.runnable.progress = 0;
        }
        vHold.text.setText(temp.censorTitle);
        picasso.load(temp.artUrl).fit().priority(Picasso.Priority.HIGH).into(vHold.image);

        vHold.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventBus.post(new ShowFavoriteDialogEvent(temp));
            }
        });
        vHold.text.setTextColor(Color.WHITE);
        if (temp.isExplicit()) {
            vHold.text.setTextColor(Color.RED);
        }

        vHold.star.setVisibility(View.GONE);
        if(position == playingPos) {
            vHold.flipper.setDisplayedChild(1);
            mHandler.post(vHold.runnable);
            vHold.pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHandler.removeCallbacksAndMessages(null);
                    player.stop();
                    player.reset();
                    vHold.flipper.setDisplayedChild(0);
                    vHold.progress.setProgress(0);
                    vHold.runnable.progress = 0;
                    playingPos = -1;
                    vHold.pause.setOnClickListener(null);
                    notifyDataSetChanged();
                }
            });
        }
        else {
            vHold.flipper.setDisplayedChild(0);
            vHold.runnable.progress = 0;
            vHold.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(playingPos != -1) {
                        mHandler.removeCallbacksAndMessages(null);
                        player.stop();
                        player.reset();
                        notifyDataSetChanged();
                    }
                    playingPos = position;
                    vHold.flipper.setDisplayedChild(1);
                    vHold.pause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mHandler.removeCallbacksAndMessages(null);
                            player.stop();
                            player.reset();
                            vHold.flipper.setDisplayedChild(0);
                            vHold.progress.setProgress(0);
                            vHold.runnable.progress = 0;
                            playingPos = -1;
                            vHold.pause.setOnClickListener(null);
                            notifyDataSetChanged();
                        }
                    });
                    try {
                        player.setDataSource(temp.previewUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler.postDelayed(vHold.runnable, 1000);
                        }
                    });
                    player.prepareAsync();
                }
            });
        }
        return convertView;
    }

    private class MediaRunnable implements Runnable {

        private ViewHolder vHold;
        int progress;
        public MediaRunnable(ViewHolder vHold){
            this.vHold = vHold;
            progress = 0;
        }

        @Override
        public void run(){
            progress = player.getCurrentPosition();
            vHold.progress.setProgress(progress);
            if(progress < 28477)
                mHandler.postDelayed(this, 1000);
            else{
                player.stop();
                player.reset();
                vHold.flipper.setDisplayedChild(0);
                progress = 0;
                playingPos = -1;
            }

        }
    }
}