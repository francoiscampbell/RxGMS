package xyz.fcampbell.rxgms.sample;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

import xyz.fcampbell.rxgms.ReactiveLocationProvider;
import xyz.fcampbell.rxgms.sample.utils.DisplayTextOnViewAction;
import xyz.fcampbell.rxgms.sample.utils.LocationToStringFunc;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import xyz.fcampbell.rxgms.sample.utils.UnsubscribeIfPresent;

public class GeofenceActivity extends BaseActivity {
    private static final String TAG = "GeofenceActivity";

    private ReactiveLocationProvider reactiveLocationProvider;
    private EditText latitudeInput;
    private EditText longitudeInput;
    private EditText radiusInput;
    private TextView lastKnownLocationView;
    private Subscription lastKnownLocationSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reactiveLocationProvider = new ReactiveLocationProvider(this);
        setContentView(xyz.fcampbell.android.rxgms.sample.R.layout.activity_geofence);
        initViews();
    }

    private void initViews() {
        lastKnownLocationView = (TextView) findViewById(xyz.fcampbell.android.rxgms.sample.R.id.last_known_location_view);
        latitudeInput = (EditText) findViewById(xyz.fcampbell.android.rxgms.sample.R.id.latitude_input);
        longitudeInput = (EditText) findViewById(xyz.fcampbell.android.rxgms.sample.R.id.longitude_input);
        radiusInput = (EditText) findViewById(xyz.fcampbell.android.rxgms.sample.R.id.radius_input);
        findViewById(xyz.fcampbell.android.rxgms.sample.R.id.add_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGeofence();
            }
        });
        findViewById(xyz.fcampbell.android.rxgms.sample.R.id.clear_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearGeofence();
            }
        });
    }

    @Override
    protected void onLocationPermissionGranted() {
        lastKnownLocationSubscription = reactiveLocationProvider
                .getLastKnownLocation()
                .map(new LocationToStringFunc())
                .subscribe(new DisplayTextOnViewAction(lastKnownLocationView));
    }

    @Override
    protected void onStop() {
        super.onStop();
        UnsubscribeIfPresent.unsubscribe(lastKnownLocationSubscription);
    }

    private void clearGeofence() {
        reactiveLocationProvider.removeGeofences(createNotificationBroadcastPendingIntent()).subscribe(new Action1<Status>() {
            @Override
            public void call(Status status) {
                toast("Geofences removed");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                toast("Error removing geofences");
                Log.d(TAG, "Error removing geofences", throwable);
            }
        });
    }

    private void toast(String text) {
        Toast.makeText(GeofenceActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private PendingIntent createNotificationBroadcastPendingIntent() {
        return PendingIntent.getBroadcast(this, 0, new Intent(this, GeofenceBroadcastReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void addGeofence() {
        final GeofencingRequest geofencingRequest = createGeofencingRequest();
        if (geofencingRequest == null) return;

        final PendingIntent pendingIntent = createNotificationBroadcastPendingIntent();
        reactiveLocationProvider
                .removeGeofences(pendingIntent)
                .flatMap(new Func1<Status, Observable<Status>>() {
                    @Override
                    public Observable<Status> call(Status pendingIntentRemoveGeofenceResult) {
                        return reactiveLocationProvider.addGeofences(pendingIntent, geofencingRequest);
                    }
                })
                .subscribe(new Action1<Status>() {
                    @Override
                    public void call(Status addGeofenceResult) {
                        toast("Geofence added, success: " + addGeofenceResult.isSuccess());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        toast("Error adding geofence.");
                        Log.d(TAG, "Error adding geofence.", throwable);
                    }
                });
    }

    private GeofencingRequest createGeofencingRequest() {
        try {
            double longitude = Double.parseDouble(longitudeInput.getText().toString());
            double latitude = Double.parseDouble(latitudeInput.getText().toString());
            float radius = Float.parseFloat(radiusInput.getText().toString());
            Geofence geofence = new Geofence.Builder()
                    .setRequestId("GEOFENCE")
                    .setCircularRegion(latitude, longitude, radius)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build();
            return new GeofencingRequest.Builder().addGeofence(geofence).build();
        } catch (NumberFormatException ex) {
            toast("Error parsing input.");
            return null;
        }
    }
}
