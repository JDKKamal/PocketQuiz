package com.jdkgroup.pocketquiz;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.jdkgroup.baseclass.BaseActivity;
import com.jdkgroup.pocketquiz.activity.MainActivity;
import com.jdkgroup.socialintegration.facebookintegration.FacebookLoginHelper;
import com.jdkgroup.socialintegration.facebookintegration.FacebookLoginListener;
import com.jdkgroup.socialintegration.facebookintegration.FacebookLoginModel;
import com.jdkgroup.socialintegration.googleintegration.GoogleLoginHelper;
import com.jdkgroup.socialintegration.googleintegration.GoogleLoginListener;
import com.jdkgroup.socialintegration.googleintegration.GoogleLoginModel;
import com.jdkgroup.utils.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements FacebookLoginListener, GoogleLoginListener, View.OnClickListener {

    private Gson gson;

    private FacebookLoginHelper facebookLoginHelper;
    private GoogleLoginHelper googleLoginHelper;

    private AppCompatImageView ivAppFacebookLogin, ivAppGoogleLogin;

    /*
    *  https://developers.google.com/identity/sign-in/android/start-integrating
    *  GOOGLE LOGIN DEVELOPERS KEY
    */

    /*
    * TODO
    * EMULATOR GOOGLE LOGIN TOKEN NOT GET ONLY SUPPORT PAY STORE AVAILABLE
    * WHEN RELEASE KEY GENERATE THEN KEYSTORE BOTH FACEBOOK AND GOOGLE
    *
    * */

    /* TODO RELEASE APK KEY GENERATE PUT build.gradle
    signingConfigs {
        release {
            storeFile file("KEY STORE PATH HERE")
            storePassword ""
            keyAlias ""
            keyPassword ""
        }
    }

    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
     */

    @BindView(R.id.appIvLogo)
    AppCompatImageView appIvLogo;
    @BindView(R.id.rvBtnLogin)
    RelativeLayout rvBtnLogin;
    @BindView(R.id.appTvLogin)
    AppCompatTextView appTvLogin;
    @BindView(R.id.llFormLogin)
    LinearLayout llFormLogin;
    @BindView(R.id.llSocialLogin)
    LinearLayout llSocialLogin;
    @BindView(R.id.llSignUp)
    LinearLayout llSignUp;

    @BindView(R.id.appTvForgotPassword)
    AppCompatTextView appTvForgotPassword;

    private DisplayMetrics dm;
    private ValueAnimator va;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        gson = new Gson();

        /* TODO FACEBOOK HASHKEY
        AppInteractor appInteractor = new AppInteractor();
        appInteractor.FacebookHashKey(this, "com.jdkgroup.pocketquiz");
        */

        facebookLoginHelper = new FacebookLoginHelper(this);
        googleLoginHelper = new GoogleLoginHelper(this, LoginActivity.this, getString(R.string.google_client_id)); //TODO GOOGLE-SERVICE.JSON CLIENT_ID LAST HERE SET

        ivAppFacebookLogin = findViewById(R.id.ivAppFacebookLogin);
        ivAppGoogleLogin = findViewById(R.id.ivAppGoogleLogin);

        dm = getResources().getDisplayMetrics();
        rvBtnLogin.setTag(0);

        ivAppFacebookLogin.setOnClickListener(this);
        ivAppGoogleLogin.setOnClickListener(this);

        resetAnim(appIvLogo);
        appIvLogo.animate().setStartDelay(0).setDuration(0).scaleX(0).scaleY(0).start();
        appIvLogo.animate().setStartDelay(100).setDuration(200).setInterpolator(new OvershootInterpolator()).scaleX(1).scaleY(1).start();
        appIvLogo.animate().setStartDelay(1000).setDuration(1000).translationY(0).alpha(1).scaleX(1).scaleY(1).start();

        rvBtnLogin.animate().translationY(0).setDuration(1000).alpha(1).setStartDelay(2000).start();
        llFormLogin.animate().translationY(dm.heightPixels).setStartDelay(0).setDuration(0).start();
        appIvLogo.animate().setStartDelay(2000).setDuration(1000).translationY(0).alpha(1).scaleX(1).scaleY(1).start();

        llFormLogin.animate().translationY(0).setDuration(1000).alpha(1).setStartDelay(2000).start();

        va = new ValueAnimator();
        va.setDuration(1000);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(p1 -> {
            RelativeLayout.LayoutParams button_login_lp = (RelativeLayout.LayoutParams) rvBtnLogin.getLayoutParams();
            button_login_lp.width = Math.round(Float.valueOf(p1.getAnimatedValue().toString()));
            rvBtnLogin.setLayoutParams(button_login_lp);
        });

        va = new ValueAnimator();
        va.setDuration(1000);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(p1 -> {
            LinearLayout.LayoutParams button_login_lp = (LinearLayout.LayoutParams) llSocialLogin.getLayoutParams();
            button_login_lp.width = Math.round(Float.valueOf(p1.getAnimatedValue().toString()));
            llSocialLogin.setLayoutParams(button_login_lp);
        });

        rvBtnLogin.animate().translationX(dm.widthPixels + rvBtnLogin.getMeasuredWidth()).setDuration(0).setStartDelay(0).start();
        rvBtnLogin.animate().translationX(0).setStartDelay(3000).setDuration(1000).setInterpolator(new OvershootInterpolator()).start();
        resetAnim(rvBtnLogin);
        appTvForgotPassword.animate().translationY(0).setDuration(3000).alpha(1).setStartDelay(3000).start();

        llSocialLogin.animate().translationX(dm.widthPixels + llSocialLogin.getMeasuredWidth()).setDuration(0).setStartDelay(0).start();
        llSocialLogin.animate().translationX(0).setStartDelay(3500).setDuration(1000).setInterpolator(new OvershootInterpolator()).start();
        resetAnim(llSocialLogin);

        resetAnim(llSignUp);
        llSignUp.animate().setStartDelay(3000).setDuration(1000).translationY(0).alpha(1).scaleX(1).scaleY(1).start();

        rvBtnLogin.setOnClickListener(p1 -> {
            appTvLogin.animate().setDuration(0).setStartDelay(0).alpha(1).start();
            AppUtils.startActivity(getActivity(), DrawerActivity.class);
            finish();
        });
    }

    private void resetAnim(View v) {
        v.animate().setStartDelay(0).setDuration(1500).translationY(10).scaleX(1).scaleY(1).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookLoginHelper.onActivityResult(requestCode, resultCode, data);
        googleLoginHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFbSignInFail(String errorMessage) {
        AppUtils.showToast(getActivity(), errorMessage);
    }

    @Override
    public void onFbSignInSuccess(FacebookLoginModel facebookLoginModel) {
        AppUtils.loge(gson.toJson(facebookLoginModel));
        AppUtils.startActivity(getActivity(), DrawerActivity.class);
        finish();
    }

    @Override
    public void onFBSignOut() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivAppFacebookLogin:
                facebookLoginHelper.performSignOut();
                facebookLoginHelper.performSignIn(this);
                break;

            case R.id.ivAppGoogleLogin:
                //googleLoginHelper.performSignOut();
                googleLoginHelper.performSignIn(this);
                break;
        }
    }

    @Override
    public void onGoogleAuthSignIn(GoogleLoginModel googleLoginModel) {
        AppUtils.loge(gson.toJson(googleLoginModel));

        AppUtils.startActivity(getActivity(), MainActivity.class);
        finish();
    }

    @Override
    public void onGoogleAuthSignInFailed(String errorMessage) {
        AppUtils.showToast(getActivity(), errorMessage);
    }

    @Override
    public void onGoogleAuthSignOut() {

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
