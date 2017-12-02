package com.jdkgroup.presenter;

import android.app.Activity;
import android.os.Handler;

import com.jdkgroup.baseclass.BasePresenter;
import com.jdkgroup.pocketquiz.LoginActivity;
import com.jdkgroup.utils.AppUtils;
import com.jdkgroup.view.SplashScreenView;

public class SplashScreenPresenter extends BasePresenter<SplashScreenView> {
    public void getSplashScreenWait(int timeOut, final Activity activity) {
        new Handler().postDelayed(() -> {
            AppUtils.startActivity(activity, LoginActivity.class);
            activity.finish();
        }, timeOut);
    }
}