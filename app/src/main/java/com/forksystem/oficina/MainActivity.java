package com.forksystem.oficina;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cs.googlemaproute.DrawRoute;
import com.forksystem.oficina.databinding.ActivityMainBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.socks.library.KLog;
import com.tbruyelle.rxpermissions.RxPermissions;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, DrawRoute.onDrawRoute {

    public String TAG = MainActivity.class.getCanonicalName();

    GoogleMap googleMap;
    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        SupportMapFragment mapa = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapa.getMapAsync(this);
    }

    public void desenhaPontos() {
        DrawRoute.getInstance(this, MainActivity.this)
                .setGmapAndKey(getString(R.string.google_server_key), googleMap)
                .setFromLatLong(-2.57341, -44.3127)
                .setToLatLong(-2.5657157, -44.31474924)
                .setZoomLevel(15.0f)
                .setLoaderMsg("Distancia entre os pontos")
                .run();

    }


    public void getLastLocalizacao() {

        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        locationProvider.getLastKnownLocation().subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {

                KLog.d("ELONE",location);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap mapa) {

        googleMap=mapa;
        desenhaPontos();



    }

    @Override
    public void afterDraw(String result) {

        KLog.json(TAG,result);
    }
}
