package xyz.fcampbell.rxgms.observables.location;

import android.app.PendingIntent;
import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;

import xyz.fcampbell.rxgms.observables.BaseLocationObservable;
import xyz.fcampbell.rxgms.observables.StatusException;
import rx.Observable;
import rx.Observer;

public class RemoveLocationIntentUpdatesObservable extends BaseLocationObservable<Status> {
    private final PendingIntent intent;

    public static Observable<Status> createObservable(Context ctx, PendingIntent intent) {
        return Observable.create(new RemoveLocationIntentUpdatesObservable(ctx, intent));
    }

    private RemoveLocationIntentUpdatesObservable(Context ctx, PendingIntent intent) {
        super(ctx);
        this.intent = intent;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, final Observer<? super Status> observer) {
        LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, intent)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            observer.onNext(status);
                            observer.onCompleted();
                        } else {
                            observer.onError(new StatusException(status));
                        }
                    }
                });
    }
}
