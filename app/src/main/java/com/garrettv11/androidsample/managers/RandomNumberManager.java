package com.garrettv11.androidsample.managers;

import android.annotation.SuppressLint;
import android.util.Log;

import com.garrettv11.androidsample.RandomNumberApplication;
import com.garrettv11.androidsample.models.RandomNumberResponse;
import com.garrettv11.androidsample.providers.CacheProvider;
import com.garrettv11.androidsample.services.RandomNumberService;

import java.util.List;

import io.reactivex.Single;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by garrett.kim on 9/15/17.
 */

public class RandomNumberManager {
    private static final String TAG = RandomNumberManager.class.getName();
    private static final RandomNumberManager ourInstance = new RandomNumberManager();
    private RandomNumberService randomNumberService;
    private CacheProvider cacheProvider;

    public static RandomNumberManager getInstance() {
        return ourInstance;
    }

    private RandomNumberManager() {
        //we use our retrofit client to take our RandomNumberService interface and build out a concrete object
        //to handle requests
        randomNumberService = NetworkManager.getClient().create(RandomNumberService.class);
        cacheProvider = new RxCache.Builder()
                .useExpiredDataIfLoaderNotAvailable(true)//this forces us to use cached data if the api call fails to get a response
                //it's actually better to just use the evict provider with the network manager as demonstrated below
                .persistence(RandomNumberApplication.sharedApplication().getCacheDir(), new GsonSpeaker())
                .using(CacheProvider.class);
    }

    public static Single<RandomNumberResponse> getRandomNumbers(int numberOfNumbersToGet) {
        @SuppressLint("DefaultLocale") String urlComponent = String.format("?length=%d&type=uint8", numberOfNumbersToGet);
        //we are using the cache provider to get us cached results if the network manager says we're offline
        //we evict cached data if we are online and available to get new results
        Log.v(TAG, "the url component is :" + urlComponent);
        return ourInstance.cacheProvider.getRandomNumbers(ourInstance.randomNumberService.getRandomNumbers(urlComponent), new EvictProvider(NetworkManager.isOnline()));
    }
}
