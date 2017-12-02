package com.jdkgroup.pocketquiz.activity;

import android.os.Bundle;

import com.jdkgroup.baseclass.BaseActivity;
import com.jdkgroup.interacter.AppInteractor;
import com.jdkgroup.interacter.DisposableManager;
import com.jdkgroup.interacter.InterActorCallback;
import com.jdkgroup.pocketquiz.R;
import com.jdkgroup.utils.AppUtils;

import java.util.HashMap;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        HashMap<String, String> params = new HashMap<>();
        params.put("timestamp", "0");

        AppInteractor appInteractor = new AppInteractor();
        appInteractor.loadJSON(this, params, new InterActorCallback<String>() {
            @Override
            public void onStart() {
                showProgressDialog();
            }

            @Override
            public void onResponse(String response) {
                System.out.println("Tag" + response);
            }

            @Override
            public void onFinish() {
                hideProgressDialog();
            }

            @Override
            public void onError(String message) {
                System.out.println("Tag " + message);
                hideProgressDialog();
            }
        });
    }

    @Override
    protected void onDestroy() {
        DisposableManager.dispose();
        super.onDestroy();
    }
}
