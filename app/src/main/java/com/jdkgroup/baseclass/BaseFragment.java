package com.jdkgroup.baseclass;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.jdkgroup.constant.AppConstant;
import com.jdkgroup.pocketquiz.R;
import com.jdkgroup.utils.AppUtils;

import org.parceler.Parcels;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class BaseFragment extends Fragment {

    private Dialog progressDialog;
    private Intent intent;
    private HashMap<String, String> params;

    public boolean onParentViewClick(Object parent, View view) {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  setRetainInstance(true);
    }

    protected void isData(LinearLayout llDataPresent, LinearLayout llDataNo, int status) {
        switch (status) {
            case 0:
                llDataPresent.setVisibility(View.GONE);
                llDataNo.setVisibility(View.VISIBLE);
                break;

            case 1:
                llDataPresent.setVisibility(View.VISIBLE);
                llDataNo.setVisibility(View.GONE);
                break;
        }
    }

    protected void sentParcelsLaunchClear(Class<?> classType, String bundleName, List<?> alData, final int status) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(bundleName, Parcels.wrap(alData));
        launchParcel(classType, bundle, status);
    }

    protected void launchParcel(final Class<?> classType, final Bundle data, final int status) {
        intent = new Intent(getActivity(), classType);
        if (status == 0) {
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        intent.putExtras(data);
        startActivity(intent);
    }

    protected void launchParcelable(final Class<?> classType, final Bundle data) {
        intent = new Intent(getActivity(), classType);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(AppConstant.BUNDLE, data);
        startActivity(intent);
    }

    public void showProgressDialog(boolean show) {
        //Show Progress bar here
        if (show) {
            showProgressBar();
        } else {
            hideProgressDialog();
        }
    }

    /*TODO PROGRESSBAR*/
    //SHOW PROGRESSBAR
    protected final void showProgressBar() {
        if (progressDialog == null) {
            progressDialog = new Dialog(getActivity());
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.app_loading_dialog, null, false);

        AppCompatImageView imageView1 = view.findViewById(R.id.appIvProgressBar);
        Animation a1 = AnimationUtils.loadAnimation(getActivity(), R.anim.progress_anim);
        a1.setDuration(1500);
        imageView1.startAnimation(a1);

        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(view);
        Window window = progressDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), android.R.color.transparent));
            //window.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        }
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void showProgressToolBar(boolean show, View view) {
        //      ((BaseActivity)getActivity()).showProgressToolBar(show,view);

    }

    public void onAuthenticationFailure(String msg) {
        // logoutUser(msg);
    }

    //HIDE PROGRESSBAR
    protected final void hideProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    //HIDE PROGRESSBAR
    protected final void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public static void showSnakBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    /*
    public void showProgress() {
        customProgressbar = new CustomProgressbar(this);
        if (customProgressbar != null && customProgressbar.isShowing()) {
            customProgressbar.hide();
        }
        customProgressbar.show(false);
        customProgressbar.isShowing();
    }

    public void hideProgress() {
        if (customProgressbar != null && customProgressbar.isShowing()) {
            customProgressbar.hide();
        }
        customProgressbar = null;
    }*/

    public HashMap<String, String> getDefaultParameter() {
        params = new HashMap<>();
        return params;
    }

    public HashMap<String, String> getDefaultParamWithIdAndToken() {
        params = getDefaultParameter();
        return params;
    }

    public boolean hasInternetWithoutMessage() {
        return hasInternet(getActivity());
    }

    /* TODO LAUNCH ACTIVITY/FRAGMENT */
    protected void launch(final Class<?> classType, final Serializable data) {
        intent = new Intent(getActivity(), classType);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    protected void launch(final Class<?> classType, final Bundle data) {
        intent = new Intent(getActivity(), classType);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("bundle", data);
        startActivity(intent);
    }

    protected void launchParceler(final Class<?> classType, final Bundle data) {
        intent = new Intent(getActivity(), classType);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtras(data);
        startActivity(intent);
    }

    protected void launch(final Class<?> classType, final int flags) {
        intent = new Intent(getActivity(), classType);
        intent.addFlags(flags);
        startActivity(intent);
    }

    protected void launch(final Class<?> classType) {
        intent = new Intent(getActivity(), classType);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    protected void launchClearParceler(final Class<?> classType, final Bundle bundle) {
        intent = new Intent(getActivity(), classType);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    protected void intentOpenBrowser(final String url) {
        if (hasInternet(getActivity())) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } else {
            AppUtils.showToast(getActivity(), getString(R.string.no_internet_message));
        }
    }

    public InputFilter decimalPointAfterBeforeAmount(final int maxDigitsBeforeDecimalPoint, final int maxDigitsAfterDecimalPoint) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source.subSequence(start, end).toString());
                if (!builder.toString().matches("(([1-9]{1})([0-9]{0," + (maxDigitsBeforeDecimalPoint - 1) + "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?")) {
                    if (source.length() == 0)
                        return dest.subSequence(dstart, dend);
                    return "";
                }
                return null;
            }
        };

        return filter;
    }

    protected void sentParcelableListLaunchClear(Class<?> classType, String bundleName, List<?> alData) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(bundleName, Parcels.wrap(alData));
        launchParceler(classType, bundle);
    }

    private <T> T getParcelable(String bundleName)
    {
        return Parcels.unwrap(getActivity().getIntent().getParcelableExtra(bundleName));
    }

    public static boolean hasInternet(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (!(networkInfo != null && networkInfo.isConnectedOrConnecting())) {
            AppUtils.showToast(context, context.getString(R.string.no_internet_message));
            return false;
        }
        return true;
    }
}
