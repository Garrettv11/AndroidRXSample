package com.garrettv11.androidsample;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by garrett.kim on 9/15/17.
 */

public class RandomNumberApplication extends Application {
    private static final String TAG = RandomNumberApplication.class.getName();
    private static RandomNumberApplication sharedApplication;
    //when the application first launches
    @Override
    public void onCreate() {
        super.onCreate();
        sharedApplication = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static RandomNumberApplication sharedApplication() {
        return sharedApplication;
    }

}
