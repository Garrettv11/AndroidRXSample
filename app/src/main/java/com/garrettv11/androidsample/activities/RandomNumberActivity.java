package com.garrettv11.androidsample.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.garrettv11.androidsample.R;
import com.garrettv11.androidsample.databinding.ActivityRandomNumberBinding;
import com.garrettv11.androidsample.managers.NetworkManager;
import com.garrettv11.androidsample.managers.RandomNumberManager;
import com.garrettv11.androidsample.models.RandomNumberResponse;
import com.garrettv11.androidsample.services.RandomNumberService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class RandomNumberActivity extends AppCompatActivity {
    private static final String TAG = RandomNumberActivity.class.getName();

    private ActivityRandomNumberBinding binding;
    private EditText numberEntryEditText;
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_random_number);
        numberEntryEditText = findViewById(R.id.numberEntryEditText);
        //this is used to keep track of all our network operations
        //each network operation gets added to this composite disposable
        compositeDisposable = new CompositeDisposable();

    }
    @Override
    protected void onStop() {
        super.onStop();
        //clear out any network calls - we don't want any results to come back of this activity is closed
        compositeDisposable.clear();
    }


    public void randomNumberButtonPressed(View view) {
        Log.v(TAG, "random number button pressed");
        final boolean isOnline = NetworkManager.isOnline();
        Log.v(TAG, "are we online? : " + isOnline);
        String randomNumberEntryText = numberEntryEditText.getText().toString();
        if (randomNumberEntryText != null && randomNumberEntryText.length() > 0) {
            int numberOfNumbers = Integer.parseInt(randomNumberEntryText);
            compositeDisposable.add(RandomNumberManager.getRandomNumbers(numberOfNumbers)
                    .subscribeOn(Schedulers.computation())//perform this call on the background thread
                    .observeOn(AndroidSchedulers.mainThread())//get the results back on the main thread
                    .subscribe(new Consumer<RandomNumberResponse>() {
                        @Override
                        public void accept(RandomNumberResponse randomNumberResponse) throws Exception {
                            Log.v(TAG, "we got our response back");
                            //we got our random number response back
                            StringBuilder builder = new StringBuilder();
                            List<Integer>randomNumbers = randomNumberResponse.data;
                            for (Integer i: randomNumbers) {
                                builder.append(String.format("[%d]", i.intValue()));
                            }
                            binding.setCurrentRandomNumberString(builder.toString());
                            if (isOnline) {
                                binding.setWasResultCached(false);
                            } else {
                                binding.setWasResultCached(true);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            //we got an error here
                            String errorMessage = "";
                            if (throwable instanceof HttpException) {
                                HttpException exception = (HttpException)throwable;
                                if (exception.code() == 401) {
                                    //bad password and email combination
                                    errorMessage = getString(R.string.error_unauthorized);
                                }
                            }
                            if (errorMessage.isEmpty()) {
                                errorMessage = throwable.getLocalizedMessage();
                            }
                            Toast.makeText(RandomNumberActivity.this, errorMessage, Toast.LENGTH_LONG);
                        }
                    })
            );
        }
    }
}
