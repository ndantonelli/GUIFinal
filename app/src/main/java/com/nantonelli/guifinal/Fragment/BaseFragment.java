package com.nantonelli.guifinal.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.nantonelli.guifinal.FinalApplication;
import com.nantonelli.guifinal.Service.RestfulApi;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

/**
 * Created by ndantonelli on 11/18/15.
 * Generic Base fragment with everything our Fragments should need in the base case. Prevents tons of boilerplate code
 * It is a support Fragment, so the fragment manager for this type of fragment is the SupportFragmentManager
 * Use the @Inject notation to have Dagger automatically use the singletons of these items created in the FinalModule
 * EventBus always has to be registered in onResume and unRegistered in onPause.  This prevents unnecessary message handling
 * ANY fragment that extends this class MUST be included in the injects= statement in the FinalModule class or it will crash
 */
public class BaseFragment  extends Fragment{
    @Inject Bus eventBus;
    @Inject RestfulApi restService;
    @Inject Picasso picasso;

    protected android.os.Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new android.os.Handler();
        ((FinalApplication)getActivity().getApplication()).getObjectGraph().inject(this);
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

    //hides a soft keyboard if there is one open on the screen
    public void hide_keyboard() {
        View v = getView();
        if(v == null)
            v = new View(getActivity());
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //post an event to the eventbus and have it block the main thread until completed
    public void postEvent(Object object){
        eventBus.post(object);
    }

    //asynchronously post event to eventbus to prevent blocking the main thread
    public void postEventAsync(final Object object){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                eventBus.post(object);
            }
        });
    }
}
