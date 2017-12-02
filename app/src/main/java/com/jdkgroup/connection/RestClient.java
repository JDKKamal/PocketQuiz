package com.jdkgroup.connection;

import android.util.Base64;

import com.jdkgroup.constant.RestConstant;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * TODO: Add a class header comment!
 */
public class RestClient {

    private static final int TIME = 50;
    private static final String TAG = RestClient.class.getSimpleName();
    private static RestService restService;
    //    public static BaseApplication application;
    private static String CREDENTIALS = "admin:admin@123";
    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE);

    private static OkHttpClient httpClient = new OkHttpClient().newBuilder()
            .connectTimeout(TIME, TimeUnit.SECONDS)
            .readTimeout(TIME, TimeUnit.SECONDS)
            .writeTimeout(TIME, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(chain -> {
                Request original = chain.request();
                final String basic = "Basic " + Base64.encodeToString(CREDENTIALS.getBytes(), Base64.NO_WRAP);
                Request.Builder requestBuilder = original.newBuilder().header("Authorization", basic);
                requestBuilder.header("Accept", "application/json");
                requestBuilder.method(original.method(), original.body());
                Request request = requestBuilder.build();
                Response response = chain.proceed(request);

                if (response.isSuccessful()) {
                    String data = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.has("authentication") && !jsonObject.optBoolean("authentication")) {
                            logout();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return response.newBuilder().body(ResponseBody.create(response.body().contentType(), data)).build();
                }
                return response;

            })
            .build();

    private static OkHttpClient httpLogoutClient = new OkHttpClient().newBuilder()
            .connectTimeout(TIME, TimeUnit.SECONDS)
            .readTimeout(TIME, TimeUnit.SECONDS)
            .writeTimeout(TIME, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                Request request = requestBuilder.build();
                Response response = chain.proceed(request);
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    /*try {
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.has("authentication") && !jsonObject.optBoolean("authentication")) {
                            //logout();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    return response.newBuilder().body(ResponseBody.create(response.body().contentType(), data)).build();
                }
                return response;
            })
            .build();

    private static OkHttpClient httpClientOther = new OkHttpClient().newBuilder()
            .connectTimeout(TIME, TimeUnit.SECONDS)
            .readTimeout(TIME, TimeUnit.SECONDS)
            .writeTimeout(TIME, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build();

    private static OkHttpClient httpUploadClient = new OkHttpClient().newBuilder()
            .connectTimeout(TIME, TimeUnit.SECONDS)
            .readTimeout(TIME, TimeUnit.SECONDS)
            .writeTimeout(TIME, TimeUnit.SECONDS)
            .build();

    public static void logout() {
       /* String deviceToken = PreferenceUtils.getInstance(BaseApplication.getBaseApplication()).getDeviceToken();
        PreferenceUtils.getInstance(BaseApplication.getBaseApplication()).clearAllPrefs();
        Intent intent = new Intent(BaseApplication.getBaseApplication(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        BaseApplication.getBaseApplication().startActivity(intent);
        PreferenceUtils.getInstance(BaseApplication.getBaseApplication()).setDeviceToken(deviceToken);*/
    }

    public static RestService getPrimaryService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(RestConstant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new ToStringConverterFactory())
                .client(httpClient)
                .build();
        return retrofit.create(RestService.class);
    }

    public static RestService getPrimaryService(int time) {
        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(time, TimeUnit.SECONDS)
                .readTimeout(time, TimeUnit.SECONDS)
                .writeTimeout(time, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        final String basic =
                                "Basic " + Base64.encodeToString(CREDENTIALS.getBytes(), Base64.NO_WRAP);
                        Request.Builder requestBuilder = original.newBuilder().header("Authorization", basic);
                        requestBuilder.header("Accept", "application/json");
                        requestBuilder.method(original.method(), original.body());
                        Request request = requestBuilder.build();
                        Response response = chain.proceed(request);

                        if (response.isSuccessful()) {
                            String data = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                if (jsonObject.has("authentication") && !jsonObject.optBoolean("authentication")) {
                                    logout();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), data)).build();
                        }
                        return response;

                    }
                })
                .build();


        Retrofit retrofit = new Retrofit.Builder().baseUrl(RestConstant.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(new ToStringConverterFactory())
                .client(httpClient)
                .build();
        return retrofit.create(RestService.class);
    }


    public static RestService getService() {
        if (restService == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl(RestConstant.BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(new ToStringConverterFactory())
                    .client(httpLogoutClient)
                    .build();
            restService = retrofit.create(RestService.class);
        }
        return restService;
    }
}