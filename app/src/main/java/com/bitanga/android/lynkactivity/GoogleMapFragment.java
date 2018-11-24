package com.bitanga.android.lynkactivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
//not anymore
/**from: http://www.exceptionbound.com/programming-tut/android-tutorial/get-current-gps-location-in-android**/
public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;

    public GoogleMapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //error here for some reason
        View v = inflater.inflate(R.layout.activity_maps, container, false);
        mView = v;
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

        //gives error that framelayout cannot be cast to mapview
        //so is mView.findViewById(R.id.map) a framelayout??
        //it's supposed to be a fragment
//        mMapView = (MapView) mView.findViewById(R.id.map);
//        if (mMapView != null) {
//            mMapView.onCreate(null);
//            mMapView.onResume();
//            mMapView.getMapAsync(this);
//
//        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());

        mGoogleMap = googleMap;
        //look at the other types they have besides normal
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //maybe add polygon instead to make it more readable
        //add school coordinates instead (this one's random)
//        googleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247, -74.044502))
//        .title("Statue of Liberty").snippet("example"));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(33.928503, -117.425580))
        .title("California Baptist University").snippet("Awesome campus!"));

        //changes viewpoint to view statue of liberty
        //can change to focus on school
//        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(40.689247, -74.044502))
//                .zoom(16).bearing(0).tilt(45).build();
//
        CameraPosition Campus = CameraPosition.builder().target(new LatLng(33.928503, -117.425580))
                .zoom(10).bearing(0).tilt(3).build();

//        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Campus));
    }

}
