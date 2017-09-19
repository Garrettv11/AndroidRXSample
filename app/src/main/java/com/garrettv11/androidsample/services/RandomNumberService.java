package com.garrettv11.androidsample.services;

import com.garrettv11.androidsample.models.RandomNumberResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by garrett.kim on 9/15/17.
 */

public interface RandomNumberService {

    @GET
    public Single<RandomNumberResponse> getRandomNumbers(@Url String url);
}
