package com.garrettv11.androidsample.providers;

import com.garrettv11.androidsample.models.RandomNumberResponse;

import java.util.List;

import io.reactivex.Single;
import io.rx_cache2.EvictProvider;

/**
 * Created by garrett.kim on 9/15/17.
 */

public interface CacheProvider {
    //cache provider of the matching function in the random number manager
    public Single<RandomNumberResponse> getRandomNumbers(Single<RandomNumberResponse> getRandomNumbersSingle, EvictProvider evictProvider);
}
