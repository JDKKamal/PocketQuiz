package com.jdkgroup.pocketquiz;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.jdkgroup.baseclass.SimpleMVPActivity;
import com.jdkgroup.interacter.AppInteractor;
import com.jdkgroup.model.ModelOSInfo;
import com.jdkgroup.presenter.SplashScreenPresenter;
import com.jdkgroup.utils.PreferenceUtils;
import com.jdkgroup.view.SplashScreenView;

import java.util.List;

public class SplashScreen extends SimpleMVPActivity<SplashScreenPresenter, SplashScreenView> implements SplashScreenView {

    private final int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);

        if (PreferenceUtils.getInstance(this).getDeviceId().isEmpty()) {
            AppInteractor appInteractor = new AppInteractor();

            List<ModelOSInfo> alModelOSInfo = appInteractor.getDeviceInfo(getActivity());
            PreferenceUtils.getInstance(this).setDeviceId(alModelOSInfo.get(0).getDeviceuniqueid());
            PreferenceUtils.getInstance(this).setIsLogin(true);
        }

        getPresenter().getSplashScreenWait(SPLASH_TIME_OUT, this);
    }

    @NonNull
    @Override
    public SplashScreenPresenter createPresenter() {
        return new SplashScreenPresenter();
    }

    @NonNull
    @Override
    public SplashScreenView attachView() {
        return this;
    }

    @Override
    public void setSplashScreenWait() {

    }

    @Override
    public void onFailure(String message) {

    }
}