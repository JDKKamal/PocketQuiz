package com.jdkgroup.interacter;

import android.content.ActivityNotFoundException;
import android.support.annotation.CallSuper;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Lakhani on 12/1/2017.
 * https://medium.com/@CodyEngel/managing-disposables-in-rxjava-2-for-android-388722ae1e8a
 */

public class DisposingObserver<T> implements Observer<T> {
    private InterActorCallback<String> callback;

    public DisposingObserver(InterActorCallback<String> callback) {
        this.callback = callback;
    }

    @Override
    @CallSuper
    public void onSubscribe(Disposable d) {
        callback.onStart();
        DisposableManager.add(d);
    }

    @Override
    public void onNext(T response) {
        callback.onResponse((String) response);
    }

    @Override
    public void onError(Throwable e) {
        callback.onError(exceptionHandle(e));
    }

    @Override
    public void onComplete() {
        callback.onFinish();
    }

    private String exceptionHandle(Throwable e) {
        if (e instanceof SocketTimeoutException) {
            return "Connection time out, please try again";
        } else if (e instanceof ActivityNotFoundException) {
            return "No activity found to handle this action.";
        } else if (e instanceof UnknownHostException || e instanceof ConnectException) {
            return "Server is not responding, please try again";
        } else {
            return "Something went wrong, please try again";
        }
    }
}