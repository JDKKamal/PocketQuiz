package com.jdkgroup.baseclass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jdkgroup.constant.AppConstant;
import com.jdkgroup.model.ModelOSInfo;
import com.jdkgroup.pocketquiz.R;
import com.jdkgroup.utils.AppUtils;

import org.apache.commons.lang3.StringUtils;
import org.parceler.Parcels;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {

    public ProgressBar progressToolbar;
    private Dialog progressDialog;
    private Intent intent;
    private HashMap<String, String> params;

    protected int linearLayoutManager = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        bindViews();
    }

    protected void bindViews() {
        ButterKnife.bind(this);
    }

    protected void setContentViewWithoutInject(int layoutResId) {
        super.setContentView(layoutResId);
    }

    protected void toolBarSetFont(final Toolbar toolBar) {
        for (int i = 0; i < toolBar.getChildCount(); i++) {
            View view = toolBar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                Typeface titleFont = Typeface.createFromAsset(getApplicationContext().getAssets(), AppConstant.FONT_AILERON_SEMIBOLD);
                if (tv.getText().equals(toolBar.getTitle())) {
                    tv.setTypeface(titleFont);
                    break;
                }
            }
        }
    }

    protected void setRecyclerViewSetLayoutManager(int type, RecyclerView recyclerView) {
        setLayoutManagerRecyclerView(type, recyclerView);
    }

    private void setLayoutManagerRecyclerView(int type, RecyclerView recyclerView) {
        switch (type) {
            case 1:
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setHasFixedSize(true);
                break;

            case 2:

                break;
        }
    }

    protected void setRecyclerViewData(LinearLayout llDataPresent, LinearLayout llDataNo, List<?> alData) {
        switchIsRecyclerViewData(llDataPresent, llDataNo, alData.isEmpty() ? 0 : 1);
    }

    private void switchIsRecyclerViewData(LinearLayout llDataPresent, LinearLayout llDataNo, int status) {
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

    protected void hideSoftKeyboard() {
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getApplicationWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public HashMap<String, String> getDefaultParameter() {
        params = new HashMap<>();
        return params;
    }

    public HashMap<String, String> getDefaultParamWithIdAndToken() {
        params = getDefaultParameter();
        return params;
    }

    public void showProgressDialog(final boolean show) {
        //Show Progress bar here
        if (show) {
            showProgressDialog();
        } else {
            hideProgressDialog();
        }
    }

    public void showProgressToolBar(final boolean show, final View view) {
        if (show) {
            progressToolbar.setVisibility(View.VISIBLE);
            if (view != null)
                view.setVisibility(View.GONE);

        } else {
            progressToolbar.setVisibility(View.GONE);
            if (view != null)
                view.setVisibility(View.VISIBLE);
        }
    }

    protected final void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new Dialog(this);
        } else {
            return;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.app_loading_dialog, null, false);

        AppCompatImageView appIvProgressBar = view.findViewById(R.id.appIvProgressBar);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.progress_anim);
        animation.setDuration(1500);
        appIvProgressBar.startAnimation(animation);

        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setContentView(view);
        Window window = progressDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.transparent));
        }
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * hide progress bar
     */
    protected final void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void onAuthenticationFailure(final String message) {

    }

    public boolean hasInternetWithoutMessage() {
        return hasInternet(this);
    }

    public static void showSnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    /* TODO LAUNCH ACTIVITY/FRAGMENT */
    protected void launch(final Class<?> classType, final Serializable data) {
        intent = new Intent(this, classType);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("data", data);
        startActivity(intent);
    }

    protected void launchParcelable(final Class<?> classType, final Bundle data) {
        intent = new Intent(this, classType);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra(AppConstant.BUNDLE, data);
        startActivity(intent);
    }

    protected void launch(final Class<?> classType, final int flags) {
        intent = new Intent(this, classType);
        intent.addFlags(flags);
        startActivity(intent);
    }

    protected void launch(final Class<?> classType) {
        intent = new Intent(this, classType);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    //ACTIVITY CLEAR TO LAUNCH NEW
    protected void launchClear(final Class<?> classType) {
        intent = new Intent(this, classType);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    protected void appPermissionSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void launchIsClearParcelable(final Class<?> classType, final Bundle bundle, final int status) {
        intent = new Intent(this, classType);
        if (status == 0) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    protected void launchParcel(final Class<?> classType, final Bundle data, final int status) {
        intent = new Intent(getActivity(), classType);
        if (status == 0) {
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        intent.putExtras(data);
        startActivity(intent);
    }

    protected void sentParcelsLaunchClear(Class<?> classType, String bundleName, List<?> alData, final int status) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(bundleName, Parcels.wrap(alData));
        launchParcel(classType, bundle, status);
    }

    protected <T> T getParcelable(String bundleName) {
        return Parcels.unwrap(getActivity().getIntent().getParcelableExtra(bundleName));
    }

    protected void passDataBundleSerializable(Bundle bundle, Class classObject, int status) {
        if (status == 0) {

        } else {
            intent = new Intent(this, classObject.getClass());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /* TODO LAUNCH ACTIVITY/FRAGMENT ANIMATION*/
    protected void intentOpenBrowser(final String url) {
        if (hasInternet(getActivity())) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } else {
            AppUtils.showToast(getActivity(), getString(R.string.no_internet_message));
        }
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

    //TODO FILE SAVE
    protected Bitmap getImageFileFromSDCard(String filename) {
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStorageDirectory() + File.separator + filename);
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void getSaveImageSDCard(Bitmap bitmap, String filename) {
        File fileMakeDirectory = new File(Environment.getExternalStorageDirectory() + File.separator + AppConstant.FOLDER_NAME);
        fileMakeDirectory.mkdirs();

        File file = new File(fileMakeDirectory, filename);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void appExist() {
        AlertDialog.Builder alert  = new AlertDialog.Builder(this);
        alert.setTitle(R.string.app_name);
        alert.setMessage(R.string.dialog_message_app_exist);
        alert.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                BaseActivity.this.finish();
            }
        });
        alert.setNegativeButton(R.string.dialog_cancel, null);
        alert.show();
    }

    protected void getFileDelete(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + AppConstant.FOLDER_NAME + File.separator + fileName);
        boolean deleted = file.delete();
        if (deleted == true)
            System.out.println("Tag" + "Delete Successfully");
        System.out.println("Tag" + "Delete no Successfully");
    }

    protected Map<String, String> getLetter() {
        Map<String, String> lettermap = new HashMap() {
            {
                for (char ch = 'A'; ch <= 'Z'; ++ch) {
                    put(String.valueOf(ch), String.valueOf(ch));
                }
            }
        };
        return lettermap;
    }

    protected Date getCurrentDate() {
        return new Date();
    }

    protected UUID getUUIDRandom() {
        return UUID.randomUUID();
    }

    protected String getDateFormatDDMMYYYY(String str) {
        return new SimpleDateFormat("dd-MMM-yyyy").format(new Date(str));
    }

    protected String getCapitalizeFirstChar(String str) {
        return StringUtils.capitalize(str);
    }

    protected String getStrRemoveLastChar(String str) {
        return StringUtils.chop(str);
    }

    protected String readFileFromAssets(final Activity activity, final String fileName, final String extension) throws Exception {
        String str = null;
        try {
            InputStream is = activity.getAssets().open("json/" + fileName + "." + extension);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            str = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return str;
    }

}

