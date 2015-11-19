package com.nantonelli.guifinal;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.nantonelli.guifinal.Service.RestfulApi;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * Created by ndantonelli on 11/18/15.
 * Generic Base Activity with everything our Activities should need in the base case. Prevents tons of boilerplate code
 * Use the @Inject notation to have Dagger automatically use the singletons of these items created in the FinalModule
 * EventBus always has to be registered in onResume and unRegistered in onPause.  This prevents unnecessary message handling
 * ANY Activity that extends this class MUST be included in the injects= statement in the FinalModule class or it will crash
 */
public class BaseActivity extends AppCompatActivity {
    @Inject RestfulApi restService;
    @Inject Bus eventBus;
    @Inject Picasso picasso;
    @Inject Typeface typeface;

    protected android.os.Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((FinalApplication)getApplication()).getObjectGraph().inject(this);
        mHandler = new android.os.Handler();
    }

    @Override
    public void onResume(){
        super.onResume();
        eventBus.register(this);
    }

    @Override
    public void onPause(){
        super.onPause();
        eventBus.unregister(this);
    }

    //posts an event to the event bus to be sent to subscribers (blocks the main thread)
    public void postEvent(Object object){
        eventBus.post(object);
    }

    //posts an event to the eventbus asynchronously to prevent blocking the main thread
    public void postEventAsync(final Object object){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                eventBus.post(object);
            }
        });
    }

    //function used to hide the soft keyboard from the users view
    public void hide_keyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.addView(getLayoutInflater().inflate(R.layout.component_toolbar, (ViewGroup) findViewById(android.R.id.content), false));
        TextView text = (TextView) toolbar.findViewById(R.id.logo_title);
        text.setTypeface(typeface);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
