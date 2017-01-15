package com.gvozditskiy.ufamenu.ActivitiesAndFragments;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.gvozditskiy.ufamenu.Constants;
import com.gvozditskiy.ufamenu.Interfaces.TouchListener;
import com.gvozditskiy.ufamenu.R;

import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment implements OnMapReadyCallback {

    ScrollView scrollView;
    GoogleMap map;


    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scrollView = (ScrollView) view.findViewById(R.id.frag_contacts_scrollview);
        FrameLayout fl = (FrameLayout) view.findViewById(R.id.frag_contacts_container);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();

        if (map == null) {
            mapFragment.getMapAsync(this);
        }
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_contacts_map_container, mapFragment);
        ft.commit();

        TouchableWrapper touchableWrapper = new TouchableWrapper(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        touchableWrapper.setTouchListener(new TouchListener() {
            @Override
            public void onTouch(Boolean b) {
                scrollView.requestDisallowInterceptTouchEvent(b);
            }
        });
        fl.addView(touchableWrapper, params);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            map.setMyLocationEnabled(true);
        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.PERMISSIONS_REQUEST_CODE);
            }
        }
        map.addMarker(new MarkerOptions()
                .position(new LatLng(51.6734949, 39.2127266))
                .title("Farfor Воронеж")
                .snippet("Ресторан удовольствий Фарфор расположился в райских тропиках тайской деревни Baunty")
                .draggable(false));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(53.1983486,45.013009))
                .title("Farfor Пенза")
                .snippet("Ресторан удовольствий Фарфор")
                .draggable(false));
        map.addMarker(new MarkerOptions()
                .position(new LatLng(53.2392332,50.2771572))
                .title("Farfor Самара")
                .snippet("Ресторан удовольствий Фарфор")
                .draggable(false));
        LatLngBounds bounds = LatLngBounds.builder().include(new LatLng(51.6734949, 39.2127266)
        ).include(new LatLng(53.1983486,45.013009))
                .include(new LatLng(53.2392332,50.2771572)).build();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds.getCenter(), 5));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Constants.PERMISSIONS_REQUEST_CODE:
                if (grantResults.length <= 0) {
                    //
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    map.setMyLocationEnabled(true);
                }
        }
    }

    private class TouchableWrapper extends FrameLayout {
        TouchListener touchListener;

        public TouchableWrapper(@NonNull Context context) {
            super(context);
        }

        public void setTouchListener(TouchListener touchListener) {
            this.touchListener = touchListener;
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case (MotionEvent.ACTION_DOWN):
                    touchListener.onTouch(true);
                    break;
                case (MotionEvent.ACTION_UP):
                    touchListener.onTouch(false);
                    break;
            }
            return super.dispatchTouchEvent(ev);
        }
    }

}
