package com.nantonelli.guifinal;

import android.app.Application;
import android.graphics.Typeface;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

import dagger.ObjectGraph;

/**
 * Created by ndantonelli on 11/18/15.
 * Custom application context for the app
 * Needed to allow us to create an ObjectGraph for Dagger.  This allows us to inject singletons into classes
 * Works in tandem with FinalModule to manage injections of singletons
 * THIS IS THE ENTRY POINT OF THE APP! (this is the very first code executed when the app is opened)
 */
public class FinalApplication extends com.activeandroid.app.Application{
    private ObjectGraph objectGraph;
    private static FinalApplication instance;
    private Typeface typeface;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        objectGraph = ObjectGraph.create(new FinalModule(this));
        typeface=Typeface.createFromAsset(getAssets(), "ludica_grande.ttf");
        Configuration dbConfiguration = new Configuration.Builder(this).setDatabaseName("favorites.db").create();
        ActiveAndroid.initialize(dbConfiguration);
    }

    //used to allow non activity/fragment classes access to the application context and singleton injections
    public static FinalApplication getInstance(){return instance;}

    //used for binding in classes to allow injections
    //should be all taken care of in base fragments/activities
    public ObjectGraph getObjectGraph(){
        return objectGraph;
    }

}
