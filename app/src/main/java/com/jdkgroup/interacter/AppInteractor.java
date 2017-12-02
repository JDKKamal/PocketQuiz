package com.jdkgroup.interacter;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;

import com.jdkgroup.constant.RestConstant;
import com.jdkgroup.model.ModelOSInfo;
import com.jdkgroup.utils.AppUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.jdkgroup.connection.RestClient.getPrimaryService;

public class AppInteractor {

    public AppInteractor() {
    }

    private void displayRequestParams(HashMap<String, String> hashMap) {
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            AppUtils.loge(pair.getKey() + " - " + pair.getValue());
        }
    }

    //TODO API CALL
    public void loadJSON(final Context context, final HashMap<String, String> params, final InterActorCallback<String> callback) {
        getPrimaryService().apiPost(RestConstant.BASE_URL + RestConstant.SYNC_COUNTRY, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposingObserver(callback));
    }

    //TODO DEFAULT
    public List<ModelOSInfo> getDeviceInfo(Activity activity) {
        PackageInfo packageInfo;
        String deviceUniqueId, deviceType, deviceName, osVersion, appVersion = "", countryIso, networkOperatorName;

        deviceUniqueId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceType = "Android";
        deviceName = Build.BRAND + Build.MODEL;
        osVersion = Build.VERSION.RELEASE;

        try {
            packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        countryIso = tm.getNetworkCountryIso();
        networkOperatorName = tm.getNetworkOperatorName();

        List<ModelOSInfo> alModelOSInfo = new ArrayList<>();
        alModelOSInfo.add(new ModelOSInfo(deviceUniqueId, deviceType, deviceName, osVersion, appVersion, countryIso, networkOperatorName));

        AppUtils.logD("OS Information " + AppUtils.getToJsonClass(alModelOSInfo));

        return alModelOSInfo;
    }

    //FACEBOOK KEY
    public void FacebookHashKey(Activity activity, String packageName) {
        PackageInfo info;
        try {
            info = activity.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String facebookkeyBase64 = new String(Base64.encode(md.digest(), 0));
                AppUtils.loge(facebookkeyBase64);
                //String facebookkeyBase64new = new String(Base64.encodeBytes(md.digest()));
            }
        } catch (PackageManager.NameNotFoundException e1) {

        } catch (NoSuchAlgorithmException e) {

        } catch (Exception e) {

        }
    }

}

