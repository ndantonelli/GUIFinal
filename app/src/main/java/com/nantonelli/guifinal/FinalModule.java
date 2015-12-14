package com.nantonelli.guifinal;

import android.content.Context;
import android.graphics.Typeface;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nantonelli.guifinal.Adapter.FavoritesAdapter;
import com.nantonelli.guifinal.Adapter.GridAdapter;
import com.nantonelli.guifinal.Adapter.ListAdapter;
import com.nantonelli.guifinal.Fragment.FavoritesFragment;
import com.nantonelli.guifinal.Fragment.SearchFragment;
import com.nantonelli.guifinal.Model.SongsRepo;
import com.nantonelli.guifinal.Service.RestfulApi;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by ndantonelli on 11/18/15.
 *
 * This is the module that deals with creating and sending out the singletons
 *      of our important libraries and datastructures
 *
 * Any class that needs injections for singletons must first be added to the injects
 *      list direction below
 * Then they need to register to the object graph in the constructor/onCreate function.
 *
 * This is taken care of in the base Activities/Fragments so use that base for
 *      fragments and activities
 *
 * Use that code for classes that can't extend base classes
 *
 * You never need to call any of the method names below, Dagger does it all
 *
 * CURRENT LIBRARIES's/DATASTRUCTURES
 *
 * 1. Otto EventBus: returned through getBus
 *      We use this library to send messages in between fragments, activities, and classes.
 *      Use the @Subscribe notation to recieve messages (must have the Event object specified as
 *          a parameter to recieve those messages)
 *
 * 2. Retrofit HTTP Requests: returned through getRestful
 *      Use this for all http requests.  Makes http requests easy.  For our implementation
 *          and endpoints look at RestfulApi.class
 *      Retrofit auto parses the returned json into java classes through the GSON Converter
 *          created and added to the singleton
 *      Requests can either be asynchronous or blocking
 *
 * 3. Picasso Image Loading: returned through getPicasso
 *      Picasso makes image loading a breeze.  Simply use the singleton to load images into
 *          imageviews with priority
 *      Glide is also another option for image loading.  HOWEVER, I like picasso better becasue
 *          it allows prefetching of images
 *      This speeds up the loading of images into imageviews because they are already loaded
 *          when they are needed.
 *      Glide could potentially be used in the future because it allows the loading of
 *          gifs and animations
 *
 * 4. Typeface: returned through getTypeFace
 *      We need to use a singleton for typefaces because they take up resources on creation
 *      Also garbage collection is not always great on them, so a singleton solves that
 *      our typeface is lucida grande because that is the default itunes font
 *
 * 5. SongsRepo: returned through getSongsRepo
 *      We need this to hold all of the songs for either search results or favorites.
 *      This allows for different fragments and activities and classes to all have access
 *          to the same songs information
 */

@Module(
        injects = {
                HomeActivity.class,
                GridAdapter.class,
                ListAdapter.class,
                FavoritesFragment.class,
                SearchFragment.class,
                SongsRepo.class,
                FavoritesAdapter.class,


        }
)
public class FinalModule {
    private Context context;

    public FinalModule(Context context){
        this.context = context;
    }

    @Singleton
    @Provides
    public Bus getBus(){
        return new Bus(ThreadEnforcer.MAIN);
    }

    @Singleton
    @Provides
    public RestfulApi getRestful(){
        String BASE_URL="https://itunes.apple.com/";
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build().create(RestfulApi.class);
    }

    @Singleton
    @Provides
    public Picasso getPicasso(){
        return new Picasso.Builder(context).build();
    }

    @Singleton
    @Provides
    public Typeface getTypeFace(){
        return Typeface.createFromAsset(context.getAssets(), "ludica_grande.ttf");
    }

    @Singleton
    @Provides
    public SongsRepo getSongsRepo(){
        return new SongsRepo();
    }
}
