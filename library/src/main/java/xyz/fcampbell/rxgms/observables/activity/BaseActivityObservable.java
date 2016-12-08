package xyz.fcampbell.rxgms.observables.activity;

import android.content.Context;

import com.google.android.gms.location.ActivityRecognition;

import xyz.fcampbell.rxgms.observables.BaseObservable;

public abstract class BaseActivityObservable<T> extends BaseObservable<T> {
    protected BaseActivityObservable(Context ctx) {
        super(ctx, ActivityRecognition.API);
    }
}
